package li.cil.ocreloaded.minecraft.api.registry.registries;

import java.util.function.Supplier;

import dev.architectury.registry.registries.RegistrySupplier;
import li.cil.ocreloaded.minecraft.api.ServiceHelper;
import li.cil.ocreloaded.minecraft.api.service.ModUtilService;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface DeferredRegister<T> {

    static final ModUtilService service = ServiceHelper.loadService(ModUtilService.class);

    public static <T> DeferredRegister<T> create(String modId, ResourceKey<Registry<T>> registryKey) {
        return service.createDeferredRegister(modId, registryKey);
    };

    <U extends T> RegistrySupplier<U> register(String path, Supplier<U> valueSupplier);

    void register();
    
}
