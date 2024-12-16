package li.cil.ocreloaded.core.graphics.imp;

import li.cil.ocreloaded.core.graphics.TextModeBuffer;
import li.cil.ocreloaded.core.graphics.color.ColorMode;
import li.cil.ocreloaded.core.graphics.color.FourBitColorMode;

public class TextModeBufferImp implements TextModeBuffer {

    private static final int ELEMENTS_PER_CELL = 2;

    private ColorMode colorMode;

    private int[] buffer;
    private int currentColor;
    private int width;

    public TextModeBufferImp(int width, int height) {
        this.width = width;
        buffer = new int[width * height * ELEMENTS_PER_CELL];
        setDepth(4);
    }

    @Override
    public void setTextCell(int x, int y, int codepoint) {
        int index = (y * width + x) * ELEMENTS_PER_CELL;
        buffer[index] = codepoint;
        buffer[index + 1] = currentColor;
    }

    @Override
    public CellInfo getTextCell(int x, int y) {
        int index = (y * width + x) * ELEMENTS_PER_CELL;
        int packedColor = buffer[index + 1];
        int foregroundIndex = packedColor & 0xFFFF;
        int backgroundIndex = packedColor >> 16;
        boolean isForegroundPaletteIndex = colorMode.isPaletteIndex(foregroundIndex);
        boolean isBackgroundPaletteIndex = colorMode.isPaletteIndex(backgroundIndex);
        int foreground = colorMode.unpack(foregroundIndex);
        int background = colorMode.unpack(backgroundIndex);
        return new CellInfo(
            buffer[index], foreground, background,
            isForegroundPaletteIndex ? foregroundIndex : -1,
            isBackgroundPaletteIndex ? backgroundIndex : -1);
    }

    @Override
    public void copy(int x, int y, int width, int height, int deltaX, int deltaY) {
        int sourceStart = (y * this.width + x) * ELEMENTS_PER_CELL;
        int destStart = ((y + deltaY) * this.width + x + deltaX) * ELEMENTS_PER_CELL;
        // Make sure we don't overwrite ourselves
        boolean doReverseCopy = destStart > sourceStart;
        if (doReverseCopy) {
            for (int i = height - 1; i >= 0; i--) {
                for (int j = width - 1; j >= 0; j--) {
                    System.arraycopy(buffer, sourceStart + j * ELEMENTS_PER_CELL, buffer, destStart + j * ELEMENTS_PER_CELL, ELEMENTS_PER_CELL);
                }
                sourceStart -= this.width * ELEMENTS_PER_CELL;
                destStart -= this.width * ELEMENTS_PER_CELL;
            }
        } else {
            for (int i = 0; i < height; i++) {
                System.arraycopy(buffer, sourceStart, buffer, destStart, width * ELEMENTS_PER_CELL);
                sourceStart += this.width * ELEMENTS_PER_CELL;
                destStart += this.width * ELEMENTS_PER_CELL;
            }
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return buffer.length / width / ELEMENTS_PER_CELL;
    }

    public void setDepth(int depth) {
        colorMode = new FourBitColorMode();
        currentColor = colorMode.pack(0x000000, false) << 16 | colorMode.pack(0xFFFFFF, false);
        for (int i = 0; i < buffer.length; i += ELEMENTS_PER_CELL) {
            buffer[i] = 32;
            buffer[i + 1] = currentColor;
        }
    }
    
}
