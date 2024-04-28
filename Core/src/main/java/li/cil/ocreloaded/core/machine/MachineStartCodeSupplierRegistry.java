package li.cil.ocreloaded.core.machine;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class MachineStartCodeSupplierRegistry {

    private static final MachineStartCodeSupplierRegistry INSTANCE = new MachineStartCodeSupplierRegistry();

    private final Map<String, Supplier<Optional<InputStream>>> startCodeSuppliers = new HashMap<>();
    
    public void register(String id, Supplier<Optional<InputStream>> supplier) {
        startCodeSuppliers.put(id, supplier);
    }

    public Optional<Supplier<Optional<InputStream>>> getSupplier(String id) {
        return Optional.ofNullable(startCodeSuppliers.get(id));
    }

    public static MachineStartCodeSupplierRegistry getDefaultInstance() {
        return INSTANCE;
    }

}
