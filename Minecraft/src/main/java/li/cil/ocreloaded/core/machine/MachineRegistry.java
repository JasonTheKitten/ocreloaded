package li.cil.ocreloaded.core.machine;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MachineRegistry {

    private static final MachineRegistry INSTANCE = new MachineRegistry();
    
    private final Map<String, MachineRegistryEntry> entries = new HashMap<>();

    public void register(String architectureId, MachineRegistryEntry entry) {
        entries.put(architectureId, entry);
    }

    public Optional<MachineRegistryEntry> getEntry(String architectureId) {
        return Optional.ofNullable(entries.get(architectureId));
    }

    public static MachineRegistry getDefaultInstance() {
        return INSTANCE;
    }

}
