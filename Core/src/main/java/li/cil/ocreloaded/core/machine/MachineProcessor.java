package li.cil.ocreloaded.core.machine;

import li.cil.ocreloaded.core.machine.imp.MachineProcessorImp;

public interface MachineProcessor {
    
    String getArchitecture();

    boolean setArchitecture(String architecture);

    public static MachineProcessor create() {
        return new MachineProcessorImp(MachineRegistry.getDefaultInstance());
    }

}
