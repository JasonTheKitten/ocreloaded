package li.cil.ocreloaded.core.machine.architecture.luac.api;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Function;

import li.cil.ocreloaded.core.machine.Machine;
import li.cil.ocreloaded.core.machine.architecture.ArchitectureMachine;
import li.cil.ocreloaded.core.machine.architecture.luac.LuaCComponentCallArguments;
import li.cil.ocreloaded.core.machine.component.Component;
import li.cil.ocreloaded.core.machine.component.ComponentCall;
import li.cil.ocreloaded.core.machine.component.ComponentCallContext;
import li.cil.ocreloaded.core.machine.component.ComponentMethod;
import li.cil.repack.com.naef.jnlua.LuaState;

public final class ComponentAPI {
    
    private ComponentAPI() {}

    public static void register(LuaState luaState, Machine machine) {
        APIRegistrationUtil.register(luaState, machine, "component", Map.of(
            "list", ComponentAPI::list,
            "invoke", ComponentAPI::invoke,
            "methods", ComponentAPI::methods,
            "type", ComponentAPI::type,
            "slot", ComponentAPI::slot
        ));
    }

    private static int list(LuaState luaState, Machine machine) {
        String filter = luaState.isString(1) ? luaState.toString(1) : null;
        boolean exact = luaState.isBoolean(2) && luaState.toBoolean(2);

        Function<String, Boolean> filterFunction = filter == null ?
            s -> true :
            s -> exact ?
                s.equals(filter) :
                s.contains(filter);

        luaState.newTable();
        Map<UUID, Component> components = ((ArchitectureMachine) machine).getComponents();
        for (Entry<UUID, Component> entry : components.entrySet()) {
            UUID id = entry.getKey();
            String type = entry.getValue().getType();
            if (!filterFunction.apply(type)) continue;
            luaState.pushString(id.toString());
            luaState.pushString(type);
            luaState.setTable(-3);
        }

        return 1;
    }

    private static int methods(LuaState luaState, Machine machine) {
        String componentId = luaState.checkString(1);

        Map<UUID, Component> components = ((ArchitectureMachine) machine).getComponents();
        Component component = components.get(UUID.fromString(componentId));
        if (component == null) {
            luaState.pushNil();
            luaState.pushString("no such component");
            return 2;
        }

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

        ComponentCall.ComponentCallResult result = call.call(
            new ComponentCallContext(),
            new LuaCComponentCallArguments(luaState, 3, luaState.getTop() - 2)
        );

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

    private static int type(LuaState luaState, Machine machine) {
        String componentId = luaState.checkString(1);

        Map<UUID, Component> components = ((ArchitectureMachine) machine).getComponents();
        Component component = components.get(UUID.fromString(componentId));
        if (component == null) {
            luaState.pushNil();
            luaState.pushString("no such component");
            return 2;
        }

        luaState.pushString(component.getType());
        return 1;
    }

    private static int slot(LuaState luaState, Machine machine) {
        String componentId = luaState.checkString(1);

        Map<UUID, Component> components = ((ArchitectureMachine) machine).getComponents();
        Component component = components.get(UUID.fromString(componentId));
        if (component == null) {
            luaState.pushNil();
            luaState.pushString("no such component");
            return 2;
        }

        luaState.pushNumber(-1); // TODO: Implement
        return 1;
    }

    private static void pushValue(LuaState luaState, Object o) {
        if (o instanceof String) {
            luaState.pushString((String) o);
        } else if (o instanceof Number) {
            luaState.pushNumber(((Number) o).doubleValue());
        } else if (o instanceof Boolean) {
            luaState.pushBoolean((Boolean) o);
        } else if (o instanceof List oList) {
            luaState.newTable();
            for (int i = 0; i < oList.size(); i++) {
                luaState.pushNumber(i + 1);
                pushValue(luaState, oList.get(i));
                luaState.setTable(-3);
            }
        } else {
            luaState.pushNil();
        }
    }

}
