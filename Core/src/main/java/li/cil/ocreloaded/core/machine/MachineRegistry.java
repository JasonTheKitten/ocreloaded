package li.cil.ocreloaded.core.machine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MachineRegistry {

    private static final MachineRegistry INSTANCE = new MachineRegistry();
    
    private final Map<String, MachineRegistryEntry> entries = new HashMap<>();

    public void register(String architectureId, MachineRegistryEntry entry) {
        entries.put(architectureId, entry);
    }

    public Optional<MachineRegistryEntry> getEntry(String architectureId) {
        return Optional.ofNullable(entries.get(architectureId));
    }

    public List<String> getSupportedEntries() {
        return entries.keySet().stream()
            .filter(key -> entries.get(key).isSupported())
            .collect(Collectors.toList());
    }

    public static MachineRegistry getDefaultInstance() {
        return INSTANCE;
    }

}
