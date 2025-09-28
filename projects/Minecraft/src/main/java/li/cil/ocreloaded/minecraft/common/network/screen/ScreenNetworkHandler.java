package li.cil.ocreloaded.minecraft.common.network.screen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import li.cil.ocreloaded.core.graphics.TextModeBuffer;
import li.cil.ocreloaded.minecraft.common.entity.ScreenBlockEntity;
import li.cil.ocreloaded.minecraft.common.network.ClientNetworkMessageContext;
import li.cil.ocreloaded.minecraft.common.network.NetworkHandler;
import li.cil.ocreloaded.minecraft.common.network.ServerNetworkMessageContext;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public class ScreenNetworkHandler implements NetworkHandler<ScreenNetworkMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenNetworkHandler.class);

    @Override
    public String getType() {
        return ScreenNetworkMessage.TYPE;
    }

    @Override
    public Class<ScreenNetworkMessage> getMessageType() {
        return ScreenNetworkMessage.class;
    }

    @Override
    public void encode(FriendlyByteBuf buffer, ScreenNetworkMessage message) {
        buffer.writeBlockPos(message.position());
        buffer.writeInt(message.getChannel());
        buffer.writeBytes(message.getChangeBuffer());
    }

    @Override
    public ScreenNetworkMessage decode(FriendlyByteBuf buffer) {
        BlockPos blockPos = buffer.readBlockPos();
        int channel = buffer.readInt();
        return new ScreenNetworkMessage(blockPos, buffer, channel);
    }
    
    @Override
    public void handleClient(ScreenNetworkMessage message, ClientNetworkMessageContext context) {
        BlockPos position = message.position();

        if (context.player() == null) return;
        Level level = context.player().level();
        if (!level.isLoaded(position)) return;
        if (level.getBlockEntity(position) instanceof ScreenBlockEntity entity) {
            if (message.getChannel() == ScreenNetworkMessage.TEXT_MODE_BUFFER_CHANNEL) {
                writeScreenChanges(entity.getScreenBuffer(), message.getChangeBuffer());
            } else {
                LOGGER.warn("Received unknown channel: {}", message.getChannel());
            }
        }
    }

    @Override
    public void handleServer(ScreenNetworkMessage message, ServerNetworkMessageContext context) {
        BlockPos position = message.position();

        if (context.sender() == null) return;
        Level level = context.sender().level();
        if (!level.isLoaded(position)) return;
        if (level.getBlockEntity(position) instanceof ScreenBlockEntity entity) {
            if (message.getChannel() == ScreenNetworkMessage.INPUT_CHANNEL) {
                ScreenNetworkInputMessages.handleInput(entity, message.getChangeBuffer(), context.sender());
            } else {
                LOGGER.warn("Received unknown channel: {}", message.getChannel());
            }
        }
    }

    private void writeScreenChanges(TextModeBuffer screenBuffer, ByteBuf changeBuffer) {
        NetworkedTextModeBufferProxy.writeTextModeBuffer(screenBuffer, changeBuffer);
    }

}
