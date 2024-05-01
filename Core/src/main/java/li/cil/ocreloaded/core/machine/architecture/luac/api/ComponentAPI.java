package li.cil.ocreloaded.core.machine.architecture.luac.api;

import java.util.Map;

import li.cil.ocreloaded.core.machine.Machine;
import li.cil.repack.com.naef.jnlua.LuaState;

public final class ComponentAPI {
    
    private ComponentAPI() {}

    public static void register(LuaState luaState, Machine machine) {
        APIRegistrationUtil.register(luaState, machine, "component", Map.of(
            "list", ComponentAPI::list
        ));
    }

    private static int list(LuaState luaState, Machine machine) {
        luaState.newTable();
        return 1;
    }

}
