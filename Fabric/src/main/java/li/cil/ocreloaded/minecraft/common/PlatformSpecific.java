package li.cil.ocreloaded.minecraft.common;

import li.cil.ocreloaded.fabric.common.FabricPlatformSpecificImp;

public final class PlatformSpecific {

    private static final PlatformSpecificImp INSTANCE = new FabricPlatformSpecificImp();

    private PlatformSpecific() {}
    
    public static PlatformSpecificImp get() {
        return INSTANCE;
    }

}
