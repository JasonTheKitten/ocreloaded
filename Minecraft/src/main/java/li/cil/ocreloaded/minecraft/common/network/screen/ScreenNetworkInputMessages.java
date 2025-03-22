package li.cil.ocreloaded.minecraft.common.network.screen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import li.cil.ocreloaded.minecraft.common.entity.ScreenBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

import java.nio.charset.StandardCharsets;

public final class ScreenNetworkInputMessages {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenNetworkInputMessages.class);

    private static final int KEY_PRESSED = 1;
    private static final int KEY_RELEASED = 2;

    public static final int MOUSE_PRESSED = 3;
    public static final int MOUSE_RELEASED = 4;
    public static final int MOUSE_DRAGGED = 5;
    public static final int MOUSE_SCROLLED = 6;
    public static final int CLIPBOARD_PASTE = 7;
    
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

    public static ScreenNetworkMessage createMouseMessage(int type, BlockPos blockPos, int button, double x, double y) {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(type);
        buffer.writeInt(button);
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        return new ScreenNetworkMessage(blockPos, buffer, ScreenNetworkMessage.INPUT_CHANNEL);
    }

    public static ScreenNetworkMessage createClipboardPasteMessage(BlockPos blockPos, String text) {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(CLIPBOARD_PASTE);

        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
        buffer.writeInt(textBytes.length);
        buffer.writeBytes(textBytes);
        
        return new ScreenNetworkMessage(blockPos, buffer, ScreenNetworkMessage.INPUT_CHANNEL);
    }

    public static void handleInput(ScreenBlockEntity entity, ByteBuf changeBuffer, Player player) {
        int type = changeBuffer.readInt();
        switch (type) {
            case KEY_PRESSED -> entity.onKeyPressed(changeBuffer.readInt(), changeBuffer.readInt(), player);
            case KEY_RELEASED -> entity.onKeyReleased(changeBuffer.readInt(), player);
            case MOUSE_PRESSED, MOUSE_RELEASED, MOUSE_DRAGGED, MOUSE_SCROLLED ->
                entity.onMouseInput(type, changeBuffer.readInt(), changeBuffer.readDouble(), changeBuffer.readDouble(), player);
            case CLIPBOARD_PASTE -> {
                int length = changeBuffer.readInt();
                byte[] textBytes = new byte[length];
                changeBuffer.readBytes(textBytes);
                String text = new String(textBytes, StandardCharsets.UTF_8);
                entity.onClipboardPaste(text, player);
            }
            default -> LOGGER.warn("Received unknown input type: {}", type);
        }
    }
}
