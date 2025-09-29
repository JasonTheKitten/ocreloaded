package li.cil.ocreloaded.fabric.api.registry.registries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import dev.architectury.registry.registries.RegistrySupplier;

public class FabricRegistrySupplier<T> implements RegistrySupplier<T> {
    
    private final Supplier<T> supplier;
    private final List<Consumer<T>> listeners = new ArrayList<>();

    private T resolved = null;

    public FabricRegistrySupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        if (resolved == null) {
            resolve();
        }

        return value();
    }

    @Override
    public T value() {
        if (resolved == null) {
            throw new RuntimeException("Not yet resolved!");
        }

        return resolved;
    }

    @Override
    public void listen(Consumer<T> listener) {
        if (resolved != null) {
            listener.accept(resolved);
        } else {
            listeners.add(listener);
        }
    }

    public void resolve() {
        if (resolved != null) {
            return;
        }

        this.resolved = supplier.get();
        if (resolved == null) {
            throw new RuntimeException("Cannot resolve to null!");
        }

        for (Consumer<T> listener: listeners) {
            listener.accept(resolved);
        }
        listeners.clear();
    }

}
