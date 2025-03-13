package li.cil.ocreloaded.core.graphics.color;

public abstract class MutablePaletteColorMode implements ColorMode {

    protected int[] palette = new int[] {
        0xFFFFFF, 0xFFCC33, 0xCC66CC, 0x6699FF,
        0xFFFF33, 0x33CC33, 0xFF6699, 0x333333,
        0xCCCCCC, 0x336699, 0x9933CC, 0x333399,
        0x663300, 0x336600, 0xFF3333, 0x000000
    };

    @Override
    public int pack(ColorData colorData) {
        if (colorData.isPaletteIndex()) {
            return Math.max(Math.min(colorData.color(), palette.length - 1), 0);
        } else {
            int closestColor = 0;
            for (int i = 1; i < palette.length; i++) {
                if (delta(colorData.color(), palette[i]) < delta(colorData.color(), palette[closestColor])) {
                    closestColor = i;
                }
            }

            return closestColor;
        }
    }
    
    protected double delta(int colorA, int colorB) {
        int[] rgbA = ColorMode.extract(colorA);
        int[] rgbB = ColorMode.extract(colorB);
        int diffRed = rgbA[0] - rgbB[0];
        int diffGreen = rgbA[1] - rgbB[1];
        int diffBlue = rgbA[2] - rgbB[2];
        return 0.2126 * diffRed * diffRed + 0.7152 * diffGreen * diffGreen + 0.0722 * diffBlue * diffBlue;
    }

}
