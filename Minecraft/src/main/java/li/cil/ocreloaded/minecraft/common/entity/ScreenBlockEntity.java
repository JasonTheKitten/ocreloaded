package li.cil.ocreloaded.minecraft.common.entity;

import java.util.List;
import java.util.Optional;

import io.netty.buffer.ByteBuf;
import li.cil.ocreloaded.core.graphics.TextModeBuffer;
import li.cil.ocreloaded.core.network.NetworkNode;
import li.cil.ocreloaded.core.network.NetworkNode.Visibility;
import li.cil.ocreloaded.minecraft.common.component.ComponentNetworkNode;
import li.cil.ocreloaded.minecraft.common.network.NetworkUtil;
import li.cil.ocreloaded.minecraft.common.network.screen.NetworkedTextModeBufferProxy;
import li.cil.ocreloaded.minecraft.common.network.screen.ScreenNetworkMessage;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import li.cil.ocreloaded.minecraft.server.component.ScreenComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;


public class ScreenBlockEntity extends BlockEntityWithTick implements ComponentTileEntity {

    private final NetworkNode networkNode = new ComponentNetworkNode(
        Optional.of(new ScreenComponent(this::getScreenBuffer)), Visibility.NETWORK
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

        screenBuffer = level.isClientSide() ?
            TextModeBuffer.create() :
            new NetworkedTextModeBufferProxy(TextModeBuffer.create());
        
        if (!level.isClientSide()) {
            level.addBlockEntityTicker(new BlockEntityTicker(this));
        }
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
            new ScreenNetworkMessage(worldPosition, changeBuffer),
            chunkTrackingPlayers
        );
    }

    public TextModeBuffer getScreenBuffer() {
        return screenBuffer;
    }

}
