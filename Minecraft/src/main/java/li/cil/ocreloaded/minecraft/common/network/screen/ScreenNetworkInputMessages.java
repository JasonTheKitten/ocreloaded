package li.cil.ocreloaded.minecraft.common.network.screen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import li.cil.ocreloaded.minecraft.common.entity.ScreenBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public final class ScreenNetworkInputMessages {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenNetworkInputMessages.class);

    private static final int KEY_PRESSED = 1;
    private static final int KEY_RELEASED = 2;
    
    private ScreenNetworkInputMessages() {}

    public static ScreenNetworkMessage createKeyPressedMessage(BlockPos blockPos, int charCode, int keyCode) {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(KEY_PRESSED);
        buffer.writeInt(charCode);
        buffer.writeInt(keyCode);
        return new ScreenNetworkMessage(blockPos, buffer, ScreenNetworkMessage.INPUT_CHANNEL);
    }

    public static ScreenNetworkMessage createKeyReleasedMessage(BlockPos blockPos, int keyCode) {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(KEY_RELEASED);
        buffer.writeInt(keyCode);
        return new ScreenNetworkMessage(blockPos, buffer, ScreenNetworkMessage.INPUT_CHANNEL);
    }

    public static void handleInput(ScreenBlockEntity entity, ByteBuf changeBuffer, Player player) {
        int type = changeBuffer.readInt();
        switch (type) {
            case KEY_PRESSED -> entity.onKeyPressed(changeBuffer.readInt(), changeBuffer.readInt(), player);
            case KEY_RELEASED -> entity.onKeyReleased(changeBuffer.readInt(), player);
            default -> LOGGER.warn("Received unknown input type: {}", type);
        }
    }

}
