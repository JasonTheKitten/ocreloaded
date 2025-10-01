package li.cil.ocreloaded.core.machine.architecture.luac.api;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import li.cil.ocreloaded.core.machine.Machine;
import li.cil.repack.com.naef.jnlua.LuaState;

public final class APIRegistrationUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(APIRegistrationUtil.class);

    private APIRegistrationUtil() {}

    public static void register(LuaState luaState, Machine machine, String apiName, Map<String, APIFunction> functions) {
        luaState.newTable();
        for (Map.Entry<String, APIFunction> entry : functions.entrySet()) {
            luaState.pushJavaFunction(l -> {
                try {
                    return entry.getValue().invoke(l, machine);
                } catch (Throwable e) {
                    LOGGER.error("Error invoking API function '{}'", entry.getKey());
                    LOGGER.error("Exception was:", e);
                    return 0;
                }
            });
            luaState.setField(-2, entry.getKey());
        }
        luaState.setGlobal(apiName);
    }

    public static interface APIFunction {
        int invoke(LuaState luaState, Machine machine);
    }
    
}
