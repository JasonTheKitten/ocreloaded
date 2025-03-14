package li.cil.ocreloaded.minecraft.common.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class ClientBridge {

    private static final ClientBridge INSTANCE = new ClientBridge();

    private final Map<Class<?>, Object> clientRegistry = new HashMap<>();
    
    private ClientBridge() {}

    public <T> void registerClient(Class<T> clazz, T instance) {
        clientRegistry.put(clazz, instance);
    }

    public <T> Optional<T> getClient(Class<T> clazz) {
        return Optional.ofNullable(clazz.cast(clientRegistry.get(clazz)));
    }

    public static ClientBridge getInstance() {
        return INSTANCE;
    }

}
