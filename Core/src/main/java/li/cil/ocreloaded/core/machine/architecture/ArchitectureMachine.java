package li.cil.ocreloaded.core.machine.architecture;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import li.cil.ocreloaded.core.machine.Component;
import li.cil.ocreloaded.core.machine.Machine;
import li.cil.ocreloaded.core.machine.MachineParameters;

public class ArchitectureMachine implements Machine {

    private final Architecture architecture;
    private final MachineParameters parameters;

    public ArchitectureMachine(Function<Machine, Architecture> architectureFactory, MachineParameters parameters) {
        this.architecture = architectureFactory.apply(this);
        this.parameters = parameters;
    }

    @Override
    public boolean start() {
        Optional<InputStream> codeStream = parameters.codeStreamSupplier().get();
        if (codeStream.isEmpty()) return false;

        return architecture.start(codeStream.get());
    }

    @Override
    public void stop() {
        architecture.stop();
    }

    public List<Component> getComponents() {
        return parameters.componentScanner().get();
    }
    
}
