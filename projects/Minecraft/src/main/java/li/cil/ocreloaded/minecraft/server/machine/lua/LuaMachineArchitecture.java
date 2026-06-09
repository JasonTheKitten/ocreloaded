package li.cil.ocreloaded.minecraft.server.machine.lua;

import java.util.Optional;
import java.util.function.Function;

import li.cil.ocreloaded.core.machine.Machine;
import li.cil.ocreloaded.core.machine.MachineParameters;
import li.cil.ocreloaded.core.machine.architecture.Architecture;
import li.cil.ocreloaded.core.machine.architecture.ArchitectureMachine;
import li.cil.ocreloaded.core.machine.architecture.luac.LuaCArchitecture;
import li.cil.ocreloaded.core.machine.architecture.luac.LuaCStateFactory;
import net.minecraft.server.MinecraftServer;

public class LuaMachineArchitecture {

    private final MinecraftServer minecraftServer;
    private final String architecture;

    public LuaMachineArchitecture(MinecraftServer minecraftServer, String architecture) {
        this.architecture = architecture;
        this.minecraftServer = minecraftServer;
    }

    public Optional<Machine> createMachine(MachineParameters parameters) {
        Optional<LuaCStateFactory> luaCStateFactory = new LuaCFactory(minecraftServer).createFactory(architecture);
        if (luaCStateFactory.isEmpty()) return Optional.empty();

        Function<Machine, Architecture> architectureFactory = machine -> new LuaCArchitecture(luaCStateFactory.get(), machine);
        return Optional.of(new ArchitectureMachine(architectureFactory, parameters));
    }

    public boolean isSupported() {
        return new LuaCFactory(minecraftServer).isAvailable(architecture);
    }
    
}
