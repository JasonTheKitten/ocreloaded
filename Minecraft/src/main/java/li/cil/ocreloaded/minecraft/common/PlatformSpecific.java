package li.cil.ocreloaded.minecraft.common;

public final class PlatformSpecific {

    private PlatformSpecific() {}
    
    public static PlatformSpecificImp get() {
        throw new UnsupportedOperationException("Not implemented on this platform.");
    }

}
