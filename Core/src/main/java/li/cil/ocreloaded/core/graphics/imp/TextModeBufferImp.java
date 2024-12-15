package li.cil.ocreloaded.core.graphics.imp;

import li.cil.ocreloaded.core.graphics.TextModeBuffer;

public class TextModeBufferImp implements TextModeBuffer {

    private static final int ELEMENTS_PER_CELL = 3;

    int[] buffer;
    int currentForeground = 15, currentBackground = 0;
    int width;

    public TextModeBufferImp(int width, int height) {
        this.width = width;
        buffer = new int[width * height * ELEMENTS_PER_CELL];
    }

    @Override
    public void setTextCell(int x, int y, int codepoint) {
        int index = (y * width + x) * ELEMENTS_PER_CELL;
        buffer[index] = codepoint;
        buffer[index + 1] = currentForeground;
        buffer[index + 2] = currentBackground;
    }

    @Override
    public int getTextCell(int x, int y) {
        int index = (y * width + x) * ELEMENTS_PER_CELL;
        return buffer[index];
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
    
}
