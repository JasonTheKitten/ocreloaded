package li.cil.ocreloaded.minecraft.server.machine.lua;

import java.util.Optional;
import java.util.function.Function;

import li.cil.ocreloaded.core.machine.Machine;
import li.cil.ocreloaded.core.machine.MachineParameters;
import li.cil.ocreloaded.core.machine.MachineRegistryEntry;
import li.cil.ocreloaded.core.machine.architecture.Architecture;
import li.cil.ocreloaded.core.machine.architecture.ArchitectureMachine;
import li.cil.ocreloaded.core.machine.architecture.luac.LuaCArchitecture;
import li.cil.ocreloaded.core.machine.architecture.luac.LuaCStateFactory;
import net.minecraft.server.MinecraftServer;

public class LuaMachineRegistryEntry implements MachineRegistryEntry {

    private final MinecraftServer minecraftServer;
    private final String architecture;

    public LuaMachineRegistryEntry(MinecraftServer minecraftServer, String architecture) {
        this.architecture = architecture;
        this.minecraftServer = minecraftServer;
    }

    @Override
    public Optional<Machine> createMachine(MachineParameters parameters) {
        Optional<LuaCStateFactory> luaCStateFactory = new LuaCChooser(minecraftServer).createFactory(architecture);
        if (luaCStateFactory.isEmpty()) return Optional.empty();

        Function<Machine, Architecture> architectureFactory = machine -> new LuaCArchitecture(luaCStateFactory.get(), machine);
        return Optional.of(new ArchitectureMachine(architectureFactory, parameters));
    }

    @Override
    public boolean isSupported() {
        return new LuaCChooser(minecraftServer).isAvailable(architecture);
    }
    
}
