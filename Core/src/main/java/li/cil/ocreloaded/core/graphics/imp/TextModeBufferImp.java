package li.cil.ocreloaded.core.graphics.imp;

import li.cil.ocreloaded.core.graphics.TextModeBuffer;
import li.cil.ocreloaded.core.graphics.color.ColorMode;
import li.cil.ocreloaded.core.graphics.color.ColorMode.ColorData;
import li.cil.ocreloaded.core.graphics.color.EightBitColorMode;
import li.cil.ocreloaded.core.graphics.color.FourBitColorMode;
import li.cil.ocreloaded.core.graphics.color.OneBitColorMode;

public class TextModeBufferImp implements TextModeBuffer {

    private static final int ELEMENTS_PER_CELL = 2;

    private ColorMode colorMode;

    private final int[] maxResolution;
    private final int maxDepth;

    private int[] viewport;
    private int[] buffer;
    private ColorData currentForeground;
    private ColorData currentBackground;
    private int width;

    public TextModeBufferImp(int[] maxResolution, int maxDepth) {
        this.maxResolution = maxResolution;
        this.maxDepth = maxDepth;
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
        if (index < 0 || index >= buffer.length || x < 0 || x >= width) {
            return;
        }
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
            sourceStart += (height - 1) * this.width * ELEMENTS_PER_CELL;
            destStart += (height - 1) * this.width * ELEMENTS_PER_CELL;
            for (int i = height - 1; i >= 0; i--) {
                for (int j = width - 1; j >= 0; j--) {
                    int offset = j * ELEMENTS_PER_CELL;
                    System.arraycopy(buffer, sourceStart + offset, buffer, destStart + offset, ELEMENTS_PER_CELL);
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
    public void setDepth(int depth) {
        ColorMode oldColorMode = colorMode;
        ColorMode newColorMode =
            depth == 1 ? new OneBitColorMode() :
            depth == 4 ? new FourBitColorMode() :
            depth == 8 ? new EightBitColorMode() :
            null;
        if (newColorMode == null) {
            throw new IllegalArgumentException("unsupported depth");
        }
        colorMode = newColorMode;

        currentForeground = new ColorData(0xFFFFFF, false);
        currentBackground = new ColorData(0x000000, false);
        for (int i = 0; i < buffer.length; i += ELEMENTS_PER_CELL) {
            if (oldColorMode == null) {
                buffer[i + 1] = packColors();
            } else {
                int backgroundShifted = buffer[i + 1] >> 16;
                int foregroundShifted = buffer[i + 1] & 0xFFFF;
                buffer[i + 1] = colorMode.pack(new ColorData(oldColorMode.unpack(backgroundShifted), false)) << 16 |
                    colorMode.pack(new ColorData(oldColorMode.unpack(foregroundShifted), false));
            }
        }
    }

    @Override
    public int getDepth() {
        return colorMode.depth();
    }

    @Override
    public int maxDepth() {
        return maxDepth;
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

    private int packColors() {
        return colorMode.pack(currentBackground) << 16 | colorMode.pack(currentForeground);
    }
    
}
