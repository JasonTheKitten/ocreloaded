package dev.architectury.registry.registries;

import java.util.function.Consumer;

public interface RegistrySupplier<T> {

    T get();
    
    T value();

    void listen(Consumer<T> listener);
    
}
