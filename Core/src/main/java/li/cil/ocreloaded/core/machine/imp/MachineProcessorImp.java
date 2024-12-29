package li.cil.ocreloaded.core.machine.imp;

import java.util.Optional;

import li.cil.ocreloaded.core.machine.MachineProcessor;
import li.cil.ocreloaded.core.machine.MachineRegistry;
import li.cil.ocreloaded.core.machine.MachineRegistryEntry;

public class MachineProcessorImp implements MachineProcessor {
    
    private final MachineRegistry registry;

    private String architecture = "Lua 5.3";

    public MachineProcessorImp(MachineRegistry registry) {
        this.registry = registry;
    }

    @Override
    public String getArchitecture() {
        return architecture;
    }

    @Override
    public boolean setArchitecture(String architecture) {
        Optional<MachineRegistryEntry> definition = registry.getEntry(architecture);
        if (definition.isEmpty() || !definition.get().isSupported()) {
            return false;
        }

        this.architecture = architecture;
        return true;
    }
    
}
