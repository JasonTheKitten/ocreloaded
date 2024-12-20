package li.cil.ocreloaded.minecraft.common.network.screen;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import li.cil.ocreloaded.core.graphics.TextModeBuffer;

public class NetworkedTextModeBufferProxy implements TextModeBuffer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkedTextModeBufferProxy.class);

    private static final int SYNC = 1;
    private static final int SET_TEXT_CELL = 2;
    private static final int COPY = 3;
    private static final int WRITE_TEXT = 4;

    private final TextModeBuffer targetBuffer;
    private final ByteBuf buffer;

    public NetworkedTextModeBufferProxy(TextModeBuffer innerBuffer) {
        this.targetBuffer = innerBuffer;
        this.buffer = Unpooled.buffer();
    }

    @Override
    public void setTextCell(int x, int y, int codepoint) {
        targetBuffer.setTextCell(x, y, codepoint);
        buffer.writeInt(SET_TEXT_CELL);
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(codepoint);
    }

    @Override
    public CellInfo getTextCell(int x, int y) {
        return targetBuffer.getTextCell(x, y);
    }

    @Override
    public void copy(int x, int y, int width, int height, int deltaX, int deltaY) {
        targetBuffer.copy(x, y, width, height, deltaX, deltaY);
        buffer.writeInt(COPY);
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(width);
        buffer.writeInt(height);
        buffer.writeInt(deltaX);
        buffer.writeInt(deltaY);
    }

    @Override
    public void writeText(int x, int y, String text, boolean vertical) {
        targetBuffer.writeText(x, y, text, vertical);
        buffer.writeInt(WRITE_TEXT);
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(text.length());
        buffer.writeBytes(text.getBytes());
        buffer.writeBoolean(vertical);
    }

    @Override
    public int getWidth() {
        return targetBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return targetBuffer.getHeight();
    }

    public boolean hasChanges() {
        return buffer.readableBytes() > 0;
    }

    public ByteBuf getBuffer() {
        buffer.writeInt(0);
        ByteBuf newBuf = buffer.copy();
        buffer.clear();

        return newBuf;
    }

    public ByteBuf sync() {
        ByteBuf syncBuffer = Unpooled.buffer();
        syncBuffer.writeInt(SYNC);  
        int width = targetBuffer.getWidth();
        int height = targetBuffer.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                CellInfo cell = targetBuffer.getTextCell(x, y);
                syncBuffer.writeInt(cell.codepoint());
                syncBuffer.writeInt(cell.foreground());
                syncBuffer.writeInt(cell.background());
                syncBuffer.writeInt(cell.foregroundIndex());
                syncBuffer.writeInt(cell.backgroundIndex());
            }
        }

        return syncBuffer;
    }

    public static void writeTextModeBuffer(TextModeBuffer buffer, ByteBuf byteBuf) {
        while (true) {
            int command = byteBuf.readInt();
            if (command == 0) break;
            
            boolean recognized = true;
            switch (command) {
                case SYNC -> handleSync(buffer, byteBuf);
                case SET_TEXT_CELL -> buffer.setTextCell(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt());
                case COPY -> buffer.copy(
                    byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt(),
                    byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt());
                case WRITE_TEXT -> handleWriteText(buffer, byteBuf);
                default -> recognized = false;
            }

            if (!recognized) {
                LOGGER.warn("Received unrecognized command for text mode buffer: {}", command);
                break;
            }
        }
    }

    private static void handleSync(TextModeBuffer buffer, ByteBuf byteBuf) {
        int width = buffer.getWidth();
        int height = buffer.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                buffer.setTextCell(x, y, byteBuf.readInt());
                // TODO
            }
        }
    }

    private static void handleWriteText(TextModeBuffer buffer, ByteBuf byteBuf) {
        int x = byteBuf.readInt();
        int y = byteBuf.readInt();
        int length = byteBuf.readInt();
        byte[] text = new byte[length];
        byteBuf.readBytes(text);
        String textString = new String(text);
        boolean vertical = byteBuf.readBoolean();
        buffer.writeText(x, y, textString, vertical);
    }
    
}
