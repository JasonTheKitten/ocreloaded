package li.cil.ocreloaded.core.graphics.color;

public interface ColorMode {
    
    int pack(ColorData colorData);

    int unpack(int color);

    boolean isPaletteIndex(int color);

    int depth();

    public static record ColorData(int color, boolean isPaletteIndex) {}

    public static int[] extract(int value) {
        int r = (value >>> 16) & 0xFF;
        int g = (value >>> 8) & 0xFF;
        int b = (value >>> 0) & 0xFF;
        return new int[]{r, g, b};
    }

}
