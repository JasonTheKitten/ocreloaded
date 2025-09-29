package li.cil.ocreloaded.fabric.api.registry.registries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import dev.architectury.registry.registries.RegistrySupplier;
import li.cil.ocreloaded.minecraft.api.registry.registries.DeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class FabricDeferredRegister<T> implements DeferredRegister<T> {

    private final String modId;
    private final ResourceKey<Registry<T>> registryKey;

    private final List<FabricRegistrySupplier<?>> registrySuppliers = new ArrayList<>();

    private boolean registrationCompleted = false;

    public FabricDeferredRegister(String modId, ResourceKey<Registry<T>> registryKey) {
        this.modId = modId;
        this.registryKey = registryKey;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U extends T> RegistrySupplier<U> register(String path, Supplier<U> valueSupplier) {
        if (registrationCompleted) {
            throw new IllegalStateException("Registry already initialized");
        }

        System.out.println(registryKey.location());
        FabricRegistrySupplier<U> registrySupplier = new FabricRegistrySupplier<U>(
            () -> {
                System.out.println(BuiltInRegistries.REGISTRY.get(registryKey.location()));
                System.out.println(ResourceLocation.fromNamespaceAndPath(modId, path));
                U v = valueSupplier.get();
                System.out.println(v);
                return Registry.register((Registry<T>) BuiltInRegistries.REGISTRY.get(registryKey.location()), ResourceLocation.fromNamespaceAndPath(modId, path), v);
        });

        registrySuppliers.add(registrySupplier);

        return registrySupplier;
    }

    @Override
    public void register() {
        if (registrationCompleted) {
            throw new IllegalStateException("Registry already initialized");
        }

        for (FabricRegistrySupplier<?> supplier: registrySuppliers) {
            supplier.resolve();
        }
    }
    
}
