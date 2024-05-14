package li.cil.ocreloaded.core.machine;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.*;

public abstract class Component {
    private static final Class<?>[] ComponentMethodArgs = new Class<?>[]{};
    private final Map<String,Method> component_methods;
    private UUID id;

    Component(UUID id) {
        Map<String, Method> methods = new HashMap<>();
        for (Method m :this.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(ComponentMethod.class)) {
                if (Arrays.equals(m.getParameterTypes(), ComponentMethodArgs)) {
                    methods.put(m.getName(),m);
                }
            }
        }
        component_methods = Collections.unmodifiableMap(methods);
        this.id = id;
    }

    public final Map<String,Method> componentMethods() {
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
}

/**
 * A annotation to be put on methods of a {@link Component}
 * every method annotated with this is exposed to the underlying machine
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface ComponentMethod {
    /**
     * @return The documentation string for this method
     */
    String value();
}