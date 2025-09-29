package li.cil.ocreloaded.minecraft.common.entity;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import li.cil.ocreloaded.core.component.ScreenComponentBase;
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
import li.cil.ocreloaded.minecraft.common.network.screen.ScreenNetworkInputMessages;
import li.cil.ocreloaded.minecraft.common.network.screen.ScreenNetworkMessage;
import li.cil.ocreloaded.minecraft.common.persistence.NBTPersistenceHolder;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;


public class ScreenBlockEntity extends BlockEntity implements TickableEntity, ComponentTileEntity {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenBlockEntity.class);

    private final NetworkNode networkNode = new ComponentNetworkNode(
        node -> new ScreenComponentBase(node, this::getScreenBuffer), Visibility.NETWORK
    );

    private boolean initialized = false;
    private TextModeBuffer screenBuffer;

    public ScreenBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CommonRegistered.SCREEN_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public NetworkNode networkNode() {
        return networkNode;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        networkNode.remove();
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if (level == null) return;

        ScreenBlock block = (ScreenBlock) getBlockState().getBlock();
        int[] maxResolution = GraphicsCardItem.TIER_RESOLUTIONS[block.getTier() - 1];
        int maxDepth = GraphicsCardItem.TIER_DEPTHS[block.getTier() - 1];
        screenBuffer = level.isClientSide() ?
            TextModeBuffer.create(maxResolution, maxDepth) :
            new NetworkedTextModeBufferProxy(TextModeBuffer.create(maxResolution, maxDepth));
        
        if (!level.isClientSide()) {
            level.addBlockEntityTicker(new BlockEntityTicker(this));
        }
    }

    @Override
    public void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider registries) {
        super.loadAdditional(compoundTag, registries);
        networkNode.load(new NBTPersistenceHolder(compoundTag, SettingsConstants.namespace));

        if (this.level == null || level.isClientSide) return;
        ComponentNetworkUtil.connectToNeighbors(level, worldPosition);
    }

    @Override
    public void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider registries) {
        super.saveAdditional(compoundTag, registries);
        networkNode.save(new NBTPersistenceHolder(compoundTag, SettingsConstants.namespace));
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide()) return;
        if (!initialized) {
            ComponentNetworkUtil.connectToNeighbors(level, worldPosition);
            initialized = true;
        }

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
        networkNode.sendToNeighbors(new NetworkMessage("keyboard.keyDown", player.getName().getString(), charCode, keyCode));
    }

    public void onKeyReleased(int keyCode, Player player) {
        networkNode.sendToNeighbors(new NetworkMessage("keyboard.keyUp", player.getName().getString(), keyCode));
    }

    public void onClipboardPaste(String text, Player player) {
        networkNode.sendToNeighbors(new NetworkMessage("keyboard.clipboard", player.getName().getString(), text));
    }

    public void onMouseInput(int type, int button, double x, double y, Player player) {
        String name = switch (type) {
            case ScreenNetworkInputMessages.MOUSE_PRESSED -> "touch";
            case ScreenNetworkInputMessages.MOUSE_RELEASED -> "drop";
            case ScreenNetworkInputMessages.MOUSE_DRAGGED -> "drag";
            case ScreenNetworkInputMessages.MOUSE_SCROLLED -> "scroll";
            default -> null;
        };

        if (name == null) {
            LOGGER.warn("Received unknown input type: {}", type);
            return;
        }

        // TODO: Include player name
        boolean isPrecise = networkNode.component().map(c -> ((ScreenComponentBase) c).isPrecise()).orElse(false);
        if (isPrecise) {
            networkNode.sendToNeighbors(new NetworkMessage("computer.checked_signal", player, name, x, y, button));
        } else {
            networkNode.sendToNeighbors(new NetworkMessage("computer.checked_signal", player, name, (int) (x + 1), (int) (y + 1), button));
        }
    }

    public TextModeBuffer getScreenBuffer() {
        return screenBuffer;
    }

}
