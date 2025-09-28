package li.cil.ocreloaded.minecraft.common.network.sound;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import li.cil.ocreloaded.minecraft.common.network.NetworkMessage;
import net.minecraft.core.BlockPos;

public class SoundNetworkMessage implements NetworkMessage {
    
    public static final String TYPE = "sound";

    public static final int BEEP_CHANNEL = 0;

    private final BlockPos position;
    private final ByteBuf changeBuffer;
    private final int channel;

    public SoundNetworkMessage(BlockPos blockPos, ByteBuf changeBuffer, int channel) {
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

    public static SoundNetworkMessage createBeepMessage(BlockPos position, short frequency, short duration) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(4);
        buffer.writeShort(frequency);
        buffer.writeShort(duration);
        return new SoundNetworkMessage(position, buffer, BEEP_CHANNEL);
    }

}
