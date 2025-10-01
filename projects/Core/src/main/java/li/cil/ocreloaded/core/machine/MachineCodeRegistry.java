package li.cil.ocreloaded.core.machine;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class MachineCodeRegistry {

    private static final MachineCodeRegistry INSTANCE = new MachineCodeRegistry();

    private final Map<String, Supplier<Optional<InputStream>>> startCodeSuppliers = new HashMap<>();
    private final Map<String, Supplier<Optional<InputStream>>> biosSuppliers = new HashMap<>();
    
    public void registerMachineCode(String id, Supplier<Optional<InputStream>> supplier) {
        startCodeSuppliers.put(id, supplier);
    }

    public Optional<Supplier<Optional<InputStream>>> getMachineCodeSupplier(String id) {
        return Optional.ofNullable(startCodeSuppliers.get(id));
    }

    public void registerBiosCode(String id, Supplier<Optional<InputStream>> supplier) {
        biosSuppliers.put(id, supplier);
    }

    public Optional<Supplier<Optional<InputStream>>> getBiosCodeSupplier(String id) {
        return Optional.ofNullable(biosSuppliers.get(id));
    }

    public static MachineCodeRegistry getDefaultInstance() {
        return INSTANCE;
    }

}
