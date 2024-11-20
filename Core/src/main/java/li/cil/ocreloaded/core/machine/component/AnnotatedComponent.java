package li.cil.ocreloaded.core.machine.component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import li.cil.ocreloaded.core.machine.PersistenceHolder;
import li.cil.ocreloaded.core.machine.component.ComponentCall.ComponentCallResult;

public abstract class AnnotatedComponent implements Component {
    
    private static final Class<?>[] componentMethodArgs = new Class<?>[] {
        ComponentCallContext.class,
        ComponentCallArguments.class
    };

    private final Map<String, ComponentCall> componentMethods;
    private final String type;

    private UUID id;

    protected AnnotatedComponent(String type) {
        Map<String, ComponentCall> methods = new HashMap<>();
        for (Method m :this.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(ComponentMethod.class)) {
                if (Arrays.equals(m.getParameterTypes(), componentMethodArgs)) {
                    methods.put(m.getName(), createComponentCall(m));
                } else {
                    System.err.printf("WARN method \"%s\" on %s does not match method args", m.getName(),this.getClass().getCanonicalName());
                }
            }
        }

        this.componentMethods = Collections.unmodifiableMap(methods);
        this.type = type;
    }

    protected AnnotatedComponent(String type, UUID id) {
        this(type);
        this.id = id;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public final Map<String, ComponentCall> componentCalls() {
        return componentMethods;
    }

    @Override
    public void loadFromState(PersistenceHolder holder) {
        if (!(holder.hasKey("INTERNAL_ID1") && holder.hasKey("INTERNAL_ID2"))) {
            if (id == null) id = UUID.randomUUID();
        } else {
            id = new UUID(
                    holder.loadLong("INTERNAL_ID1"),
                    holder.loadLong("INTERNAL_ID2")
            );
        }
    };

    @Override
    public void storeIntoState(PersistenceHolder holder) {
        holder.storeLong("INTERNAL_ID1",id.getMostSignificantBits());
        holder.storeLong("INTERNAL_ID2",id.getLeastSignificantBits());
    };

    private ComponentCall createComponentCall(Method m) {
        return new ComponentCall() {

            @Override
            public ComponentCallResult call(ComponentCallContext context, ComponentCallArguments arguments) {
                return invoke(m, context, arguments);
            }

            @Override
            public ComponentMethod getAnnotation() {
                return m.getAnnotation(ComponentMethod.class);
            }
        
        };
    }

    private ComponentCallResult invoke(Method m, Object... args) {
        try {
            return (ComponentCallResult) m.invoke(this, args);
        } catch (Exception e) {
            return ComponentCallResult.failure(e.getMessage());
        }
    }

}
