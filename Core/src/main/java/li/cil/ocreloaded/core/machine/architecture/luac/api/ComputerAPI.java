package li.cil.ocreloaded.core.machine.architecture.luac.api;

import java.util.Map;

import li.cil.ocreloaded.core.machine.Machine;
import li.cil.repack.com.naef.jnlua.LuaState;

public final class ComputerAPI {

    private ComputerAPI() {}
    
    public static void register(LuaState luaState, Machine machine) {
        APIRegistrationUtil.register(luaState, machine, "computer", Map.of(
            "realTime", ComputerAPI::realTime
        ));
    }

    private static int realTime(LuaState luaState, Machine machine) {
        luaState.pushNumber(System.currentTimeMillis() / 1000.0);
        return 1;
    }

}
