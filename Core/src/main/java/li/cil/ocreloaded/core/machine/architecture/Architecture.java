package li.cil.ocreloaded.core.machine.architecture;

import java.io.InputStream;

import li.cil.ocreloaded.core.machine.MachineResult;

public interface Architecture {
    
    MachineResult start(InputStream codeStream);

    MachineResult resume();
    
    void stop();

}
