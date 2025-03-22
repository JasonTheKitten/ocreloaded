package li.cil.ocreloaded.core.machine.architecture.luac.api;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import li.cil.ocreloaded.core.machine.Machine;
import li.cil.ocreloaded.core.machine.MachineProcessor;
import li.cil.ocreloaded.core.machine.architecture.luac.api.APIRegistrationUtil.APIFunction;
import li.cil.repack.com.naef.jnlua.LuaState;

public final class ComputerAPI {

    private ComputerAPI() {}
    
    public static void register(LuaState luaState, Machine machine) {
        APIRegistrationUtil.register(luaState, machine, "computer", Map.ofEntries(
            entry("realTime", ComputerAPI::realTime),
            entry("uptime", ComputerAPI::uptime),
            entry("address", ComputerAPI::address),
            entry("freeMemory", ComputerAPI::freeMemory),
            entry("totalMemory", ComputerAPI::totalMemory),
            entry("pushSignal", ComputerAPI::pushSignal),
            entry("tmpAddress", ComputerAPI::tmpAddress),
            entry("energy", ComputerAPI::energy),
            entry("maxEnergy", ComputerAPI::maxEnergy),
            entry("getArchitectures", ComputerAPI::getArchitectures),
            entry("getArchitecture", ComputerAPI::getArchitecture),
            entry("setArchitecture", ComputerAPI::setArchitecture)
        ));
    }

    private static int realTime(LuaState luaState, Machine machine) {
        luaState.pushNumber(System.currentTimeMillis() / 1000.0);
        return 1;
    }

    private static int uptime(LuaState luaState, Machine machine) {
        luaState.pushNumber(machine.uptime());
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

    private static int energy(LuaState luaState, Machine machine) {
        // TODO: Use proper energy amount
        luaState.pushNumber(Double.POSITIVE_INFINITY);
        return 1;
    }

    private static int maxEnergy(LuaState luaState, Machine machine) {
        // TODO: Use proper energy amount
        luaState.pushNumber(60);
        return 1;
    }

    private static int getArchitectures(LuaState luaState, Machine machine) {
        luaState.newTable();
        List<String> supportedArchitectures = machine.parameters().processor().supportedArchitectures();
        for (int i = 0; i < supportedArchitectures.size(); i++) {
            luaState.pushNumber(i + 1);
            luaState.pushString(supportedArchitectures.get(i));
            luaState.setTable(-3);
        }
        return 1;
    }

    private static int getArchitecture(LuaState luaState, Machine machine) {
        luaState.pushString(machine.parameters().processor().getArchitecture());
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

    private static Entry<String, APIFunction> entry(String name, APIFunction function) {
        return new SimpleEntry<>(name, function);
    }

}
