package li.cil.ocreloaded.minecraft.client;

import li.cil.ocreloaded.forge.client.ForgeClientPlatformSpecificImp;

public final class ClientPlatformSpecific {
 
    private ClientPlatformSpecific() {}

    public static ClientPlatformSpecificImp get() {
        return new ForgeClientPlatformSpecificImp();
    }

}
