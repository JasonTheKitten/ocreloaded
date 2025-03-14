package li.cil.ocreloaded.minecraft.common.network.sound;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import li.cil.ocreloaded.minecraft.common.network.ClientNetworkMessageContext;
import li.cil.ocreloaded.minecraft.common.network.NetworkHandler;
import li.cil.ocreloaded.minecraft.common.registry.ClientBridge;
import li.cil.ocreloaded.minecraft.common.sound.SoundPlayerProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public class SoundNetworkHandler implements NetworkHandler<SoundNetworkMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoundNetworkHandler.class);

    @Override
    public String getType() {
        return SoundNetworkMessage.TYPE;
    }

    @Override
    public Class<SoundNetworkMessage> getMessageType() {
        return SoundNetworkMessage.class;
    }

    @Override
    public void encode(FriendlyByteBuf buffer, SoundNetworkMessage message) {
        buffer.writeBlockPos(message.position());
        buffer.writeInt(message.getChannel());
        buffer.writeBytes(message.getChangeBuffer());
    }

    @Override
    public SoundNetworkMessage decode(FriendlyByteBuf buffer) {
        return new SoundNetworkMessage(buffer.readBlockPos(), buffer, buffer.readInt());
    }

    @Override
    public void handleClient(SoundNetworkMessage message, ClientNetworkMessageContext context) {
        BlockPos position = message.position();

        if (context.player() == null) return;
        Level level = context.player().level();
        if (!level.isLoaded(position)) return;
        if (message.getChannel() != SoundNetworkMessage.BEEP_CHANNEL) {
            LOGGER.warn("Received unknown channel: {}", message.getChannel());
            return;
        }

        ByteBuffer buffer = message.getChangeBuffer().nioBuffer();
        short frequency = buffer.getShort();
        short duration = buffer.getShort();
        ClientBridge.getInstance().getClient(SoundPlayerProvider.class).ifPresent(soundPlayerProvider ->
            soundPlayerProvider.playBeepSound(position, frequency, duration));
    }
    
}
