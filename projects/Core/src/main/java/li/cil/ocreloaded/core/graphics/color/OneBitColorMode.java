package li.cil.ocreloaded.core.graphics.color;

public class OneBitColorMode implements ColorMode {
    
    @Override
    public int pack(ColorData colorData) {
        return colorData.color() > 0 ? 1 : 0;
    }

    @Override
    public int unpack(int color) {
        return color > 0 ? 0xFFFFFF : 0;
    }

    @Override
    public boolean isPaletteIndex(int color) {
        return false;
    }

    @Override
    public int depth() {
        return 1;
    }
    
}
