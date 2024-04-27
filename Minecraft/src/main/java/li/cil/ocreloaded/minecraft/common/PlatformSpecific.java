package li.cil.ocreloaded.minecraft.common;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.platform.Platform;

public final class PlatformSpecific {

    private PlatformSpecific() {}

    public static PlatformSpecificImp get(){
        try {
            if (Platform.isForgeLike()) {
                return (PlatformSpecificImp) Class.forName("li.cil.ocreloaded.forge.common.ForgePlatformSpecificImp").getConstructor().newInstance();
            } else {
                return (PlatformSpecificImp) Class.forName("li.cil.ocreloaded.fabric.common.FabricPlatformSpecificImp").getConstructor().newInstance();
            }
        } catch (Exception e) {
            throw new UnsupportedOperationException("Not implemented on this platform.");
        }
    }

}
