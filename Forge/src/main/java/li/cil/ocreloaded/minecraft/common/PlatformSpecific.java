package li.cil.ocreloaded.minecraft.common;

import li.cil.ocreloaded.forge.common.ForgePlatformSpecificImp;

public class PlatformSpecific {
    
    private static final PlatformSpecificImp INSTANCE = new ForgePlatformSpecificImp();

    private PlatformSpecific() {}
    
    public static PlatformSpecificImp get() {
        return INSTANCE;
    }

}
