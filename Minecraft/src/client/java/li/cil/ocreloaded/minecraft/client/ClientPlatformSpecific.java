package li.cil.ocreloaded.minecraft.client;

public final class ClientPlatformSpecific {
  
    private ClientPlatformSpecific() {}
    
    public static ClientPlatformSpecificImp get() {
        throw new UnsupportedOperationException("Not implemented on this platform.");
    }
    
}
