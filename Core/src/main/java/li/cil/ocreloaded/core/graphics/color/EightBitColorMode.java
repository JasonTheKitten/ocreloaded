package li.cil.ocreloaded.core.graphics.color;

public class EightBitColorMode extends MutablePaletteColorMode {
    
    private static final int REDS = 6;
    private static final int GREENS = 8;
    private static final int BLUES = 5;

    private int[] staticPalette = new int[240];

    {
        for (int i = 0; i < staticPalette.length; i++) {
            int idxB = i % BLUES;
            int idxG = (i / BLUES) % GREENS;
            int idxR = (i / (BLUES * GREENS)) % REDS;
            int r = (int) (idxR * 255 / (REDS - 1.0) + 0.5);
            int g = (int) (idxG * 255 / (GREENS - 1.0) + 0.5);
            int b = (int) (idxB * 255 / (BLUES - 1.0) + 0.5);
            staticPalette[i] = (r << 16) | (g << 8) | b;
        }

        for (int i = 0; i < palette.length; i++) {
            int shade = 255 * (i + 1) / (palette.length + 1);
            palette[i] = (shade << 16) | (shade << 8) | shade;
        }
    }

    @Override
    public int pack(ColorData colorData) {
        int paletteIndex = super.pack(colorData);
        if (colorData.isPaletteIndex()) {
            return paletteIndex;
        } else {
            int r = (colorData.color() >> 16) & 0xFF;
            int g = (colorData.color() >> 8) & 0xFF;
            int b = colorData.color() & 0xFF;
            int idxR = (int) (r * (REDS - 1.0) / 255 + 0.5);
            int idxG = (int) (g * (GREENS - 1.0) / 255 + 0.5);
            int idxB = (int) (b * (BLUES - 1.0) / 255 + 0.5);
            int deflated = palette.length + idxR * GREENS * BLUES + idxG * BLUES + idxB;
            if (delta(unpack(deflated & 0xFF), colorData.color()) < delta(unpack(paletteIndex & 0xFF), colorData.color())) {
                return deflated;
            } else {
                return paletteIndex;
            }
        }
    }

    @Override
    public int unpack(int color) {
        if (isPaletteIndex(color)) {
            return palette[color];
        } else {
            return staticPalette[(color - palette.length) % staticPalette.length];
        }
    }

    @Override
    public boolean isPaletteIndex(int color) {
        return  0 <= color && color < palette.length;
    }

    @Override
    public int depth() {
        return 8;
    }

}
