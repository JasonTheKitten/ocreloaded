package li.cil.ocreloaded.core.graphics;

import li.cil.ocreloaded.core.graphics.imp.TextModeBufferImp;

public interface TextModeBuffer {

    void setTextCell(int x, int y, int codepoint);

    CellInfo getTextCell(int x, int y);

    void copy(int x, int y, int width, int height, int deltaX, int deltaY);

    int getWidth();

    int getHeight();

    default void writeText(int x, int y, String text, boolean vertical) {
        for (int i = 0; i < text.length(); i++) {
            int mx = vertical ? x : x + i;
            int my = vertical ? y + i : y;
            setTextCell(mx, my, text.codePointAt(i));
        }
    }
    
    public static TextModeBuffer create() {
        return new TextModeBufferImp(50, 16);
    }

    public static record CellInfo(int codepoint, int foreground, int background, int foregroundIndex, int backgroundIndex) {}

}
