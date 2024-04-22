package li.cil.ocreloaded.core.machine.architecture;

import java.io.InputStream;

public interface Architecture {
    
    boolean start(InputStream codStream);
    
    void stop();

}
