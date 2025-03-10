package li.cil.ocreloaded.core.graphics.imp;

import li.cil.ocreloaded.core.graphics.TextModeBuffer;
import li.cil.ocreloaded.core.graphics.color.ColorMode;
import li.cil.ocreloaded.core.graphics.color.ColorMode.ColorData;
import li.cil.ocreloaded.core.graphics.color.FourBitColorMode;

public class TextModeBufferImp implements TextModeBuffer {

    private static final int ELEMENTS_PER_CELL = 2;

    private ColorMode colorMode;

    private int[] maxResolution;
    private int[] viewport;
    private int[] buffer;
    private ColorData currentForeground;
    private ColorData currentBackground;
    private int width;

    public TextModeBufferImp(int[] maxResolution) {
        this.maxResolution = maxResolution;
        this.viewport = new int[] { maxResolution[0], maxResolution[1] };
        this.width = maxResolution[0];
        buffer = new int[width * maxResolution[1] * ELEMENTS_PER_CELL];
        setDepth(4);
    }

    @Override
    public void reset() {
        for (int i = 0; i < buffer.length; i += ELEMENTS_PER_CELL) {
            buffer[i] = 32;
            buffer[i + 1] = packColors();
        }
    }

    @Override
    public void setTextCell(int x, int y, int codepoint) {
        int index = (y * width + x) * ELEMENTS_PER_CELL;
        if (index < 0 || index >= buffer.length || x < 0 || x >= width) {
            return;
        }
        buffer[index] = codepoint;
        buffer[index + 1] = colorMode.pack(currentBackground) << 16 | colorMode.pack(currentForeground);
    }

    @Override
    public void rawSetTextCell(int x, int y, int codepoint, int packedColors) {
        int index = (y * width + x) * ELEMENTS_PER_CELL;
        buffer[index] = codepoint;
        buffer[index + 1] = packedColors;
    }

    @Override
    public CellInfo getTextCell(int x, int y) {
        int index = (y * width + x) * ELEMENTS_PER_CELL;
        if (index < 0 || index >= buffer.length || x < 0 || x >= width) {
            return new CellInfo(32, 0xFFFFFF, 0x000000, -1, -1);
        }
        int packedColor = buffer[index + 1];
        int foregroundIndex = packedColor & 0xFFFF;
        int backgroundIndex = packedColor >> 16;
        int foreground = colorMode.unpack(foregroundIndex);
        int background = colorMode.unpack(backgroundIndex);
        int foregroundPalette = colorMode.isPaletteIndex(foregroundIndex) ? foregroundIndex : -1;
        int backgroundPalette = colorMode.isPaletteIndex(backgroundIndex) ? backgroundIndex : -1; 
        return new CellInfo(buffer[index], foreground, background, foregroundPalette, backgroundPalette);
    }

    @Override
    public ReducedCellInfo getReducedTextCell(int x, int y) {
        int index = (y * width + x) * ELEMENTS_PER_CELL;
        int packedColor = buffer[index + 1];
        return new ReducedCellInfo(buffer[index], packedColor);
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
    public void fill(int x, int y, int width, int height, int codepoint) {
        int packedColors = packColors();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                rawSetTextCell(x + j, y + i, codepoint, packedColors);
            }
        }
    }

    @Override
    public void setBackgroundColor(int color, boolean isPaletteIndex) {
        currentBackground = new ColorData(color, isPaletteIndex);
    }

    @Override
    public ColorData getBackgroundColor() {
        return currentBackground;
    }

    @Override
    public void setForegroundColor(int color, boolean isPaletteIndex) {
        currentForeground = new ColorData(color, isPaletteIndex);
    }

    @Override
    public ColorData getForegroundColor() {
        return currentForeground;
    }

    @Override
    public int getPaletteColor(int index) {
        return colorMode.unpack(index);
    }

    @Override
    public int getDepth() {
        return colorMode.depth();
    }

    @Override
    public int[] resolution() {
        return new int[] { width, buffer.length / (width * ELEMENTS_PER_CELL) };
    }

    @Override
    public int[] viewport() {
        return viewport;
    }

    @Override
    public int[] maxResolution() {
        return maxResolution;
    }

    @Override
    public void setResolution(int width, int height) {
        if (width < 1 || height < 1 || width > maxResolution[0] || height > maxResolution[1]) {
            throw new IllegalArgumentException("unsupported resolution");
        }

        // TODO: Is old data preserved?
        int[] newBuffer = new int[width * height * ELEMENTS_PER_CELL];
        int copyWidth = Math.min(this.width, width);
        int copyHeight = Math.min(buffer.length / (this.width * ELEMENTS_PER_CELL), height);
        for (int i = 0; i < copyHeight; i++) {
            System.arraycopy(buffer, i * this.width * ELEMENTS_PER_CELL, newBuffer, i * width * ELEMENTS_PER_CELL, copyWidth * ELEMENTS_PER_CELL);
        }
        this.width = width;
        buffer = newBuffer;

        setViewport(width, height);
    }

    @Override
    public void setViewport(int width, int height) {
        viewport = new int[] { width, height };
    }

    public void setDepth(int depth) {
        colorMode = new FourBitColorMode();
        currentForeground = new ColorData(0xFFFFFF, false);
        currentBackground = new ColorData(0x000000, false);
        for (int i = 0; i < buffer.length; i += ELEMENTS_PER_CELL) {
            // TODO: setDepth does not clear the buffer
            buffer[i] = 32;
            buffer[i + 1] = packColors();
        }
    }

    private int packColors() {
        return colorMode.pack(currentBackground) << 16 | colorMode.pack(currentForeground);
    }
    
}
