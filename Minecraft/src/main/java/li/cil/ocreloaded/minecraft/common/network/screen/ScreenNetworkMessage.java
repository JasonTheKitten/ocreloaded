package li.cil.ocreloaded.minecraft.common.network.screen;

import io.netty.buffer.ByteBuf;
import li.cil.ocreloaded.minecraft.common.network.NetworkMessage;
import net.minecraft.core.BlockPos;

public class ScreenNetworkMessage implements NetworkMessage {

    public static final String TYPE = "screen";

    private BlockPos position;
    private ByteBuf changeBuffer;

    public ScreenNetworkMessage(BlockPos blockPos, ByteBuf changeBuffer) {
        this.position = blockPos;
        this.changeBuffer = changeBuffer;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public BlockPos position() {
        return position;
    }

    public ByteBuf getChangeBuffer() {
        return changeBuffer;
    }
    
}
