package li.cil.ocreloaded.core.graphics;

import li.cil.ocreloaded.core.graphics.color.ColorMode.ColorData;
import li.cil.ocreloaded.core.graphics.imp.TextModeBufferImp;

public interface TextModeBuffer {

    void reset();

    void setTextCell(int x, int y, int codepoint);

    void rawSetTextCell(int x, int y, int codepoint, int packedColors);

    CellInfo getTextCell(int x, int y);

    ReducedCellInfo getReducedTextCell(int x, int y);

    void copy(int x, int y, int width, int height, int deltaX, int deltaY);

    void fill(int x, int y, int width, int height, int codepoint);

    void setBackgroundColor(int color, boolean isPaletteIndex);

    ColorData getBackgroundColor();

    void setForegroundColor(int color, boolean isPaletteIndex);

    ColorData getForegroundColor();

    int getPaletteColor(int index);

    int getDepth();

    int[] resolution();

    int[] viewport();

    int[] maxResolution();

    void setResolution(int width, int height);

    void setViewport(int width, int height);

    default void writeText(int x, int y, String text, boolean vertical) {
        for (int i = 0; i < text.length(); i++) {
            int mx = vertical ? x : x + i;
            int my = vertical ? y + i : y;
            setTextCell(mx, my, text.codePointAt(i));
        }
    }
    
    public static TextModeBuffer create(int[] maxResolution) {
        return new TextModeBufferImp(maxResolution);
    }

    public static record CellInfo(int codepoint, int foreground, int background, int foregroundIndex, int backgroundIndex) {}

    public static record ReducedCellInfo(int codepoint, int packedColors) {}

}
