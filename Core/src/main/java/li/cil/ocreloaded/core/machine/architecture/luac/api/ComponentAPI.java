package li.cil.ocreloaded.core.machine.architecture.luac.api;

import java.util.Map;
import java.util.UUID;

import li.cil.ocreloaded.core.machine.Machine;
import li.cil.ocreloaded.core.machine.architecture.ArchitectureMachine;
import li.cil.ocreloaded.core.machine.architecture.component.Component;
import li.cil.ocreloaded.core.machine.architecture.component.ComponentCall;
import li.cil.ocreloaded.core.machine.architecture.component.ComponentMethod;
import li.cil.repack.com.naef.jnlua.LuaState;

public final class ComponentAPI {
    
    private ComponentAPI() {}

    public static void register(LuaState luaState, Machine machine) {
        APIRegistrationUtil.register(luaState, machine, "component", Map.of(
            "list", ComponentAPI::list,
            "invoke", ComponentAPI::invoke,
            "methods", ComponentAPI::methods
        ));
    }

    private static int list(LuaState luaState, Machine machine) {
        luaState.newTable();

        Map<UUID, Component> components = ((ArchitectureMachine) machine).getComponents();
        for (Component component : components.values()) {
            luaState.pushString(component.getId().toString());
            luaState.pushString(component.getType());
            luaState.setTable(-3);
        }

        return 1;
    }

    private static int methods(LuaState luaState, Machine machine) {
        String componentId = luaState.checkString(1);

        Map<UUID, Component> components = ((ArchitectureMachine) machine).getComponents();
        Component component = components.get(UUID.fromString(componentId));
        Map<String, ComponentCall> componentCalls = component.componentCalls();

        luaState.newTable();
        for (String methodName : componentCalls.keySet()) {
            ComponentMethod methodCall = componentCalls.get(methodName).getAnnotation();
            luaState.pushString(methodName);
            luaState.newTable();
            luaState.pushBoolean(methodCall.direct());
            luaState.setField(-2, "direct");
            luaState.pushBoolean(methodCall.getter());
            luaState.setField(-2, "getter");
            luaState.pushBoolean(methodCall.setter());
            luaState.setField(-2, "setter");
            luaState.rawSet(-3);
        }

        return 1;
    }

    private static int invoke(LuaState luaState, Machine machine) {
        String componentId = luaState.checkString(1);
        String methodName = luaState.checkString(2);

        Map<UUID, Component> components = ((ArchitectureMachine) machine).getComponents();
        Component component = components.get(UUID.fromString(componentId));
        if (component == null) {
            luaState.pushNil();
            luaState.pushString("no such component");
            return 2;
        }

        ComponentCall call = component.componentCalls().get(methodName);
        if (call == null) {
            luaState.pushNil();
            luaState.pushString("no such method");
            return 2;
        }

        ComponentCall.ComponentCallResult result = call.call();
        if (result.error() != null) {
            luaState.pushNil();
            luaState.pushString(result.error());
            return 2;
        }

        luaState.pushBoolean(true);
        for (Object o : result.result()) {
            pushValue(luaState, o);
        }

        return result.result().length + 1;
    }

    private static void pushValue(LuaState luaState, Object o) {
        if (o instanceof String) {
            luaState.pushString((String) o);
        } else if (o instanceof Number) {
            luaState.pushNumber(((Number) o).doubleValue());
        } else if (o instanceof Boolean) {
            luaState.pushBoolean((Boolean) o);
        } else {
            luaState.pushNil();
        }
    }

}
