package li.cil.ocreloaded.minecraft.server.machine.lua;


import li.cil.ocreloaded.core.machine.architecture.luac.LuaCStateFactory;
import li.cil.repack.com.naef.jnlua.LuaState;
import li.cil.repack.com.naef.jnlua.NativeSupport;

public class LuaCStateFactoryImp implements LuaCStateFactory {

    private final String libPath;

    public LuaCStateFactoryImp(String libPath) {
        this.libPath = libPath;
    }

    @Override
    public LuaState create() {
        NativeSupport.getInstance().setLoader(() -> System.load(libPath));
        return new LuaState();
    }
    
}
