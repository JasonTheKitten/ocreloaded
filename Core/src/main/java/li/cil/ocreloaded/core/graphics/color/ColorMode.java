package li.cil.ocreloaded.core.graphics.color;

public interface ColorMode {
    
    int pack(int color, boolean isPaletteIndex);

    int unpack(int color);

    boolean isPaletteIndex(int color);

    public static int[] extract(int value) {
        int r = (value >>> 16) & 0xFF;
        int g = (value >>> 8) & 0xFF;
        int b = (value >>> 0) & 0xFF;
        return new int[]{r, g, b};
    }

}
