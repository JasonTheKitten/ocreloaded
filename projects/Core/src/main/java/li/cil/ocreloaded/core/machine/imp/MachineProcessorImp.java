package li.cil.ocreloaded.core.machine.imp;

import java.util.List;

import li.cil.ocreloaded.core.machine.MachineBuilder;
import li.cil.ocreloaded.core.machine.MachineProcessor;

public class MachineProcessorImp implements MachineProcessor {
    
    private final MachineBuilder builder;

    private String architecture = "Lua 5.3";

    public MachineProcessorImp(MachineBuilder builder) {
        this.builder = builder;
    }

    @Override
    public List<String> supportedArchitectures() {
        return builder.supportedArchitectures();
    }

    @Override
    public String getArchitecture() {
        return architecture;
    }

    @Override
    public boolean setArchitecture(String architecture) {
        if (!builder.isSupportedArchitecture(architecture)) {
            return false;
        }

        this.architecture = architecture;
        return true;
    }
    
}
