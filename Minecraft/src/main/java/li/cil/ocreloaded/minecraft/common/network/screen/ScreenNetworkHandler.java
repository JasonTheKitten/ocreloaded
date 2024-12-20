package li.cil.ocreloaded.minecraft.common.network.screen;

import io.netty.buffer.ByteBuf;
import li.cil.ocreloaded.core.graphics.TextModeBuffer;
import li.cil.ocreloaded.minecraft.common.entity.ScreenBlockEntity;
import li.cil.ocreloaded.minecraft.common.network.ClientNetworkMessageContext;
import li.cil.ocreloaded.minecraft.common.network.NetworkHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public class ScreenNetworkHandler implements NetworkHandler<ScreenNetworkMessage> {

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
        buffer.writeBytes(message.getChangeBuffer());
    }

    @Override
    public ScreenNetworkMessage decode(FriendlyByteBuf buffer) {
        BlockPos blockPos = buffer.readBlockPos();
        return new ScreenNetworkMessage(blockPos, buffer);
    }
    
    @Override
    public void handleClient(ScreenNetworkMessage message, ClientNetworkMessageContext context) {
        BlockPos position = message.position();

        if (context.player() == null) return;
        Level level = context.player().level();
        if (!level.isLoaded(position)) return;
        if (level.getBlockEntity(position) instanceof ScreenBlockEntity entity) {
            writeScreenChanges(entity.getScreenBuffer(), message.getChangeBuffer());
        }
    }

    private void writeScreenChanges(TextModeBuffer screenBuffer, ByteBuf changeBuffer) {
        NetworkedTextModeBufferProxy.writeTextModeBuffer(screenBuffer, changeBuffer);
    }

}
