package li.cil.ocreloaded.core.machine.architecture.luac.api;

import java.util.Map;

import li.cil.ocreloaded.core.machine.Machine;
import li.cil.repack.com.naef.jnlua.LuaState;

public final class SystemAPI {

    private SystemAPI() {}

    public static void register(LuaState luaState, Machine machine) {
        APIRegistrationUtil.register(luaState, machine, "system", Map.of(
            "timeout", SystemAPI::timeout,
            "allowBytecode", SystemAPI::allowBytecode
        ));
    }

    private static int timeout(LuaState luaState, Machine machine) {
        luaState.pushNumber(5); // TODO: Implement config option
        return 1;
    }

    private static int allowBytecode(LuaState luaState, Machine machine) {
        luaState.pushBoolean(true); // TODO: Implement config option
        return 1;
    }
    
}
