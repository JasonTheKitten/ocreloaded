package li.cil.ocreloaded.core.machine.architecture.luac.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import li.cil.ocreloaded.core.machine.Machine;
import li.cil.ocreloaded.core.machine.MachineProcessor;
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
            "tmpAddress", ComputerAPI::tmpAddress,
            "setArchitecture", ComputerAPI::setArchitecture,
            "getArchitecture", ComputerAPI::getArchitecture
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
        luaState.pushString(machine.parameters().tmpFsNode().id().toString());
        return 1;
    }

    private static int setArchitecture(LuaState luaState, Machine machine) {
        MachineProcessor processor = machine.parameters().processor();
        String oldArchitecture = processor.getArchitecture();
        String newArchitecture = luaState.checkString(1);
        if (processor.setArchitecture(newArchitecture)) {
            luaState.pushBoolean(!oldArchitecture.equals(newArchitecture));
            return 1;
        } else {
            luaState.pushNil();
            luaState.pushString("unknown architecture");
            return 2;
        }
    }

    private static int getArchitecture(LuaState luaState, Machine machine) {
        luaState.pushString(machine.parameters().processor().getArchitecture());
        return 1;
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
