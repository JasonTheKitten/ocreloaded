package li.cil.ocreloaded.core.machine;

import java.util.List;

import li.cil.ocreloaded.core.machine.imp.MachineProcessorImp;

public interface MachineProcessor {

    List<String> supportedArchitectures();
    
    String getArchitecture();

    boolean setArchitecture(String architecture);

    public static MachineProcessor create() {
        return new MachineProcessorImp(MachineRegistry.getDefaultInstance());
    }

}
