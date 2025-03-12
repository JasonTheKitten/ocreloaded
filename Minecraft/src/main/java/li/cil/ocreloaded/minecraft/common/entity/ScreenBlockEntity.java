package li.cil.ocreloaded.minecraft.common.entity;

import java.util.List;

import io.netty.buffer.ByteBuf;
import li.cil.ocreloaded.core.component.ScreenComponent;
import li.cil.ocreloaded.core.graphics.TextModeBuffer;
import li.cil.ocreloaded.core.network.NetworkMessage;
import li.cil.ocreloaded.core.network.NetworkNode;
import li.cil.ocreloaded.core.network.NetworkNode.Visibility;
import li.cil.ocreloaded.minecraft.common.SettingsConstants;
import li.cil.ocreloaded.minecraft.common.block.ScreenBlock;
import li.cil.ocreloaded.minecraft.common.component.ComponentNetworkNode;
import li.cil.ocreloaded.minecraft.common.component.ComponentNetworkUtil;
import li.cil.ocreloaded.minecraft.common.item.GraphicsCardItem;
import li.cil.ocreloaded.minecraft.common.network.NetworkUtil;
import li.cil.ocreloaded.minecraft.common.network.screen.NetworkedTextModeBufferProxy;
import li.cil.ocreloaded.minecraft.common.network.screen.ScreenNetworkMessage;
import li.cil.ocreloaded.minecraft.common.persistence.NBTPersistenceHolder;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;


public class ScreenBlockEntity extends BlockEntityWithTick implements ComponentTileEntity {

    private final NetworkNode networkNode = new ComponentNetworkNode(
        node -> new ScreenComponent(node, this::getScreenBuffer), Visibility.NETWORK
    );
    private TextModeBuffer screenBuffer;

    public ScreenBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CommonRegistered.SCREEN_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public NetworkNode networkNode() {
        return networkNode;
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if (level == null) return;

        ScreenBlock block = (ScreenBlock) getBlockState().getBlock();
        int[] maxResolution = GraphicsCardItem.TIER_RESOLUTIONS[block.getTier() - 1];
        screenBuffer = level.isClientSide() ?
            TextModeBuffer.create(maxResolution) :
            new NetworkedTextModeBufferProxy(TextModeBuffer.create(maxResolution));
        
        if (!level.isClientSide()) {
            level.addBlockEntityTicker(new BlockEntityTicker(this));
        }
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        networkNode.load(new NBTPersistenceHolder(compoundTag, SettingsConstants.namespace));

        if (this.level == null || level.isClientSide) return;
        ComponentNetworkUtil.connectToNeighbors(level, worldPosition);
    }

    @Override
    public void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        networkNode.save(new NBTPersistenceHolder(compoundTag, SettingsConstants.namespace));
    }

    @Override
    public void tick() {
        if (level.isClientSide()) return;

        ChunkPos chunkPos = new ChunkPos(worldPosition);
        List<ServerPlayer> chunkTrackingPlayers = ((ServerLevel) level).getPlayers(
            player -> player.getChunkTrackingView().contains(chunkPos)
        );

        NetworkedTextModeBufferProxy proxy = (NetworkedTextModeBufferProxy) screenBuffer;
        if (!proxy.hasChanges()) return;
        ByteBuf changeBuffer = proxy.getBuffer();
        NetworkUtil.getInstance().messageManyClients(
            new ScreenNetworkMessage(worldPosition, changeBuffer, ScreenNetworkMessage.TEXT_MODE_BUFFER_CHANNEL),
            chunkTrackingPlayers
        );
    }

    public void onKeyPressed(int charCode, int keyCode, Player player) {
        networkNode.sendToNeighbors(new NetworkMessage("keyboard.keyDown", player.getName(), charCode, keyCode));
    }

    public void onKeyReleased(int keyCode, Player player) {
        networkNode.sendToNeighbors(new NetworkMessage("keyboard.keyUp", player.getName(), keyCode));
    }

    public TextModeBuffer getScreenBuffer() {
        return screenBuffer;
    }

}
