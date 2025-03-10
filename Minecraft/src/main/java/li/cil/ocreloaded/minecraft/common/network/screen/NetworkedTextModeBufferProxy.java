package li.cil.ocreloaded.minecraft.common.network.screen;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import li.cil.ocreloaded.core.graphics.TextModeBuffer;
import li.cil.ocreloaded.core.graphics.color.ColorMode.ColorData;

public class NetworkedTextModeBufferProxy implements TextModeBuffer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkedTextModeBufferProxy.class);

    private static final int RESET = 1;
    private static final int SYNC = 2;
    private static final int SET_TEXT_CELL = 3;
    private static final int COPY = 4;
    private static final int FILL = 5;
    private static final int WRITE_TEXT = 6;
    private static final int SET_BACKGROUND = 7;
    private static final int SET_FOREGROUND = 8;
    private static final int SET_RESOLUTION = 9;
    private static final int SET_VIEWPORT = 10;

    private final TextModeBuffer targetBuffer;
    private final ByteBuf buffer;

    public NetworkedTextModeBufferProxy(TextModeBuffer innerBuffer) {
        this.targetBuffer = innerBuffer;
        this.buffer = Unpooled.buffer();
    }

    @Override
    public void reset() {
        targetBuffer.reset();
        buffer.writeInt(RESET);
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
    public void rawSetTextCell(int x, int y, int codepoint, int packedColors) {
        throw new UnsupportedOperationException("Unimplemented method 'rawSetTextCell'");
    }

    @Override
    public CellInfo getTextCell(int x, int y) {
        return targetBuffer.getTextCell(x, y);
    }

    @Override
    public ReducedCellInfo getReducedTextCell(int x, int y) {
        return targetBuffer.getReducedTextCell(x, y);
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
    public void fill(int x, int y, int width, int height, int codepoint) {
        targetBuffer.fill(x, y, width, height, codepoint);
        buffer.writeInt(FILL);
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(width);
        buffer.writeInt(height);
        buffer.writeInt(codepoint);
    }

    @Override
    public void writeText(int x, int y, String text, boolean vertical) {
        byte[] textBytes = text.getBytes();
        targetBuffer.writeText(x, y, text, vertical);
        buffer.writeInt(WRITE_TEXT);
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(textBytes.length);
        buffer.writeBytes(textBytes);
        buffer.writeBoolean(vertical);
    }

    @Override
    public void setBackgroundColor(int color, boolean isPaletteIndex) {
        targetBuffer.setBackgroundColor(color, isPaletteIndex);
        buffer.writeInt(SET_BACKGROUND);
        buffer.writeInt(color);
        buffer.writeBoolean(isPaletteIndex);
    }

    @Override
    public ColorData getBackgroundColor() {
        return targetBuffer.getBackgroundColor();
    }

    @Override
    public void setForegroundColor(int color, boolean isPaletteIndex) {
        targetBuffer.setForegroundColor(color, isPaletteIndex);
        buffer.writeInt(SET_FOREGROUND);
        buffer.writeInt(color);
        buffer.writeBoolean(isPaletteIndex);
    }

    @Override
    public ColorData getForegroundColor() {
        return targetBuffer.getForegroundColor();
    }

    @Override
    public int getPaletteColor(int index) {
        return targetBuffer.getPaletteColor(index);
    }

    @Override
    public int getDepth() {
        return targetBuffer.getDepth();
    }

    @Override
    public int[] resolution() {
        return targetBuffer.resolution();
    }

    @Override
    public int[] viewport() {
        return targetBuffer.viewport();
    }

    @Override
    public int[] maxResolution() {
        return targetBuffer.maxResolution();
    }

    @Override
    public void setResolution(int width, int height) {
        targetBuffer.setResolution(width, height);
        buffer.writeInt(SET_RESOLUTION);
        buffer.writeInt(width);
        buffer.writeInt(height);
    }

    @Override
    public void setViewport(int width, int height) {
        targetBuffer.setViewport(width, height);
        buffer.writeInt(SET_VIEWPORT);
        buffer.writeInt(width);
        buffer.writeInt(height);
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
        int[] size = targetBuffer.resolution();
        int width = size[0];
        int height = size[1];
        syncBuffer.writeInt(width);
        syncBuffer.writeInt(height);
        int[] viewport = targetBuffer.viewport();
        syncBuffer.writeInt(viewport[0]);
        syncBuffer.writeInt(viewport[1]);
        ColorData backgroundColor = targetBuffer.getBackgroundColor();
        syncBuffer.writeInt(backgroundColor.color());
        syncBuffer.writeBoolean(backgroundColor.isPaletteIndex());
        ColorData foregroundColor = targetBuffer.getForegroundColor();
        syncBuffer.writeInt(foregroundColor.color());
        syncBuffer.writeBoolean(foregroundColor.isPaletteIndex());
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ReducedCellInfo cell = targetBuffer.getReducedTextCell(x, y);
                syncBuffer.writeInt(cell.codepoint());
                syncBuffer.writeInt(cell.packedColors());
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
                case RESET -> buffer.reset();
                case SYNC -> handleSync(buffer, byteBuf);
                case SET_TEXT_CELL -> buffer.setTextCell(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt());
                case COPY -> buffer.copy(
                    byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt(),
                    byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt());
                case FILL -> buffer.fill(
                    byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt(),
                    byteBuf.readInt(), byteBuf.readInt());
                case WRITE_TEXT -> handleWriteText(buffer, byteBuf);
                case SET_BACKGROUND -> buffer.setBackgroundColor(byteBuf.readInt(), byteBuf.readBoolean());
                case SET_FOREGROUND -> buffer.setForegroundColor(byteBuf.readInt(), byteBuf.readBoolean());
                case SET_RESOLUTION -> buffer.setResolution(byteBuf.readInt(), byteBuf.readInt());
                case SET_VIEWPORT -> buffer.setViewport(byteBuf.readInt(), byteBuf.readInt());
                default -> recognized = false;
            }

            if (!recognized) {
                LOGGER.warn("Received unrecognized command for text mode buffer: {}", command);
                break;
            }
        }
    }

    private static void handleSync(TextModeBuffer buffer, ByteBuf byteBuf) {
        // TODO: Do we have to bother syncing non-viewport data?
        int width = byteBuf.readInt();
        int height = byteBuf.readInt();
        buffer.setResolution(width, height);
        buffer.setViewport(byteBuf.readInt(), byteBuf.readInt());
        buffer.setBackgroundColor(byteBuf.readInt(), byteBuf.readBoolean());
        buffer.setForegroundColor(byteBuf.readInt(), byteBuf.readBoolean());
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                buffer.rawSetTextCell(x, y, byteBuf.readInt(), byteBuf.readInt());
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
