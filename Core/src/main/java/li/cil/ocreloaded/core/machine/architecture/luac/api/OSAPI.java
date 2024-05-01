package li.cil.ocreloaded.core.machine.architecture.luac.api;

import java.util.Map;

import li.cil.ocreloaded.core.machine.Machine;
import li.cil.repack.com.naef.jnlua.LuaState;

public final class OSAPI {
    
    private OSAPI() {}
    
    public static void register(LuaState luaState, Machine machine) {
        APIRegistrationUtil.register(luaState, machine, "os", Map.of());
    }

}
