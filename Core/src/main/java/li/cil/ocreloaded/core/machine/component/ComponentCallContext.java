package li.cil.ocreloaded.core.machine.component;

import li.cil.ocreloaded.core.network.NetworkNode;

public interface ComponentCallContext {
    
    NetworkNode networkNode();

    void pause(double duration);
    
}
