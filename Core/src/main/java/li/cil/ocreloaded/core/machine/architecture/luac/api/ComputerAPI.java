package li.cil.ocreloaded.core.machine.architecture.luac.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import li.cil.ocreloaded.core.machine.Machine;
import li.cil.repack.com.naef.jnlua.LuaState;

public final class ComputerAPI {

    private ComputerAPI() {}
    
    public static void register(LuaState luaState, Machine machine) {
        APIRegistrationUtil.register(luaState, machine, "computer", Map.of(
            "realTime", ComputerAPI::realTime,
            "uptime", ComputerAPI::uptime,
            "address", ComputerAPI::address,
            "freeMemory", ComputerAPI::freeMemory,
            "totalMemory", ComputerAPI::totalMemory,
            "pushSignal", ComputerAPI::pushSignal,
            "tmpAddress", ComputerAPI::tmpAddress
        ));
    }

    private static int realTime(LuaState luaState, Machine machine) {
        luaState.pushNumber(System.currentTimeMillis() / 1000.0);
        return 1;
    }

    private static int uptime(LuaState luaState, Machine machine) {
        luaState.pushNumber(machine.uptime() / 1000);
        return 1;
    }

    private static int address(LuaState luaState, Machine machine) {
        luaState.pushString(machine.parameters().networkNode().id().toString());
        return 1;
    }

    private static int freeMemory(LuaState luaState, Machine machine) {
        // TODO: Consider kernel memory usage
        luaState.pushNumber(Runtime.getRuntime().freeMemory());
        return 1;
    }

    private static int totalMemory(LuaState luaState, Machine machine) {
        luaState.pushNumber(Runtime.getRuntime().totalMemory());
        return 1;
    }

    private static int pushSignal(LuaState luaState, Machine machine) {
        List<Object> signalInfo = new ArrayList<>();

        String signalName = luaState.checkString(1);
        for (int i = 2; i <= luaState.getTop(); i++) {
            signalInfo.add(toSimpleJavaObject(luaState, i));
        }

        luaState.pushBoolean(machine.signal(signalName, signalInfo.toArray()));

        return 1;
    }

    private static int tmpAddress(LuaState luaState, Machine machine) {
        // TODO: Implement tmpAddress
        luaState.pushNil();
        return 0;
    }

    private static Object toSimpleJavaObject(LuaState luaState, int index) {
        switch (luaState.type(index)) {
            case BOOLEAN:
                return luaState.toBoolean(index);
            case NUMBER:
                return luaState.toNumber(index);
            case STRING:
                return luaState.toString(index);
            case NIL:
            default:
                return null;
        }
    }

}
