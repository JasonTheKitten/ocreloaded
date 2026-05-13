package li.cil.ocreloaded.minecraft.server.machine.lua;

import li.cil.repack.com.naef.jnlua.NativeSupport.Loader;

public interface NativeSupportLoader extends Loader {
    
    void loadLib(String libPath);

}
