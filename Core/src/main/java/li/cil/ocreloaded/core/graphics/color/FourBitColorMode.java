package li.cil.ocreloaded.core.graphics.color;

public class FourBitColorMode extends MutablePaletteColorMode {

    @Override
    public int unpack(int color) {
        return palette[Math.max(0, Math.min(palette.length - 1, color))];    
    }

    @Override
    public boolean isPaletteIndex(int color) {
        return true;
    }

    @Override
    public int depth() {
        return 4;
    }
    
}
