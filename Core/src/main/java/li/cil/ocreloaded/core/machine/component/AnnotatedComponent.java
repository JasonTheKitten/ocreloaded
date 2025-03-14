package li.cil.ocreloaded.core.machine.component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import li.cil.ocreloaded.core.machine.component.ComponentCall.ComponentCallResult;
import li.cil.ocreloaded.core.network.NetworkNode;

public abstract class AnnotatedComponent implements Component {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotatedComponent.class);
    
    private static final Class<?>[] componentMethodArgs = new Class<?>[] {
        ComponentCallContext.class,
        ComponentCallArguments.class
    };

    private final Map<String, ComponentCall> componentMethods;
    private final NetworkNode networkNode;
    private final String type;

    protected AnnotatedComponent(String type, NetworkNode networkNode) {
        Map<String, ComponentCall> methods = new HashMap<>();
        for (Method m :this.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(ComponentMethod.class)) {
                if (Arrays.equals(m.getParameterTypes(), componentMethodArgs)) {
                    methods.put(m.getName(), createComponentCall(m));
                } else {
                    LOGGER.warn("method \"{}\" on {} does not match method args", m.getName(), this.getClass().getCanonicalName());
                }
            }
        }

        this.componentMethods = Collections.unmodifiableMap(methods);
        this.networkNode = networkNode;
        this.type = type;
    }

    @Override
    public NetworkNode getNetworkNode() {
        return networkNode;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public final Map<String, ComponentCall> componentCalls() {
        return componentMethods;
    }

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
            String message = e.getMessage() != null ? e.getMessage() : "unknown error";
            LOGGER.error("error invoking method \"{}\" on {}", m.getName(), this.getClass().getCanonicalName(), e);
            return ComponentCallResult.failure(message);
        }
    }

}
