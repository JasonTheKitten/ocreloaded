package li.cil.ocreloaded.core.machine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public abstract class Component {
    private static final Class<?>[] ComponentMethodArgs = new Class<?>[]{};
    protected final Map<String,MachineMethod> component_methods;
    private UUID id;

    protected Component(UUID id) {
        this.id = id;
        Map<String, MachineMethod> methods = new HashMap<>();
        Method call_method;
        try {
            call_method = MachineMethod.class.getMethod("call");
        } catch (NoSuchMethodException e) {
            System.out.println("somehow the MachineMethod lacks the call method....");
            component_methods = new HashMap<>();
            return;
        }
        for (Method m :this.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(ComponentMethod.class)) {
                ComponentMethod method = m.getAnnotation(ComponentMethod.class);
                if (areArgsMatch(m,call_method)) {
                    Component com = this;
                    methods.put(m.getName(), new MachineMethod() {
                        @Override
                        public void call() throws InvocationTargetException, IllegalAccessException {
                            m.invoke(com);
                        }

                        @Override
                        public String doc() {
                            return method.value();
                        }

                        @Override
                        public boolean mainthread() {
                            return method.mainthread();
                        }
                    });
                } else {
                    System.err.printf("WARN method \"%s\" on %s does not match method args", m.getName(),this.getClass().getCanonicalName());
                }
            }
        }
        component_methods = methods;
    }

    public UUID getId() {
        return id;
    }

    public void test() {

    }

    public final Map<String,MachineMethod> componentMethods() {
        return component_methods;
    }

//    @ComponentMethod("Prints hello world to console")
//    void exampleMethod() {
//        System.out.println("hello world");
//    }

    public void loadFromState(PersistenceHolder holder) {
        holder.storeLong("INTERNAL_ID1",id.getMostSignificantBits());
        holder.storeLong("INTERNAL_ID2",id.getLeastSignificantBits());
    };
    public void storeIntoState(PersistenceHolder holder) {
        id = new UUID(
                holder.loadLong("INTERNAL_ID1"),
                holder.loadLong("INTERNAL_ID2")
        );
    };

    private static boolean areArgsMatch(Method method1, Method method2) {
        // Get parameter types of both methods
        Class<?>[] params1 = method1.getParameterTypes();
        Class<?>[] params2 = method2.getParameterTypes();

        // Check if the lengths of parameter lists match
        if (params1.length != params2.length) {
            return false;
        }

        // Check if parameter types match
        for (int i = 0; i < params1.length; i++) {
            if (params1[i] != params2[i]) {
                return false;
            }
        }

        return true;
    }
}


