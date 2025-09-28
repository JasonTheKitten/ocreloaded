package li.cil.ocreloaded.minecraft.common.network.screen;

import io.netty.buffer.ByteBuf;
import li.cil.ocreloaded.minecraft.common.network.NetworkMessage;
import net.minecraft.core.BlockPos;

public class ScreenNetworkMessage implements NetworkMessage {

    public static final String TYPE = "screen";

    public static final int TEXT_MODE_BUFFER_CHANNEL = 0;
    public static final int INPUT_CHANNEL = 1;

    private final BlockPos position;
    private final ByteBuf changeBuffer;
    private final int channel;

    public ScreenNetworkMessage(BlockPos blockPos, ByteBuf changeBuffer, int channel) {
        this.position = blockPos;
        this.changeBuffer = changeBuffer;
        this.channel = channel;
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

    public int getChannel() {
        return this.channel;
    }
    
}
