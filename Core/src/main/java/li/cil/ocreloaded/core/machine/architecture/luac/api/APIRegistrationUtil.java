package li.cil.ocreloaded.core.machine.architecture.luac.api;

import java.util.Map;

import li.cil.ocreloaded.core.machine.Machine;
import li.cil.repack.com.naef.jnlua.LuaState;

public final class APIRegistrationUtil {

    private APIRegistrationUtil() {}

    public static void register(LuaState luaState, Machine machine, String apiName, Map<String, APIFunction> functions) {
        luaState.newTable();
        for (Map.Entry<String, APIFunction> entry : functions.entrySet()) {
            luaState.pushJavaFunction(l -> {
                return entry.getValue().invoke(l, machine);
            });
            luaState.setField(-2, entry.getKey());
        }
        luaState.setGlobal(apiName);
    }

    public static interface APIFunction {
        int invoke(LuaState luaState, Machine machine);
    }
    
}
