package li.cil.ocreloaded.core.machine;

import java.util.Optional;

public interface MachineRegistryEntry {
    
    Optional<Machine> createMachine(MachineParameters parameters);

    boolean isSupported();

}
