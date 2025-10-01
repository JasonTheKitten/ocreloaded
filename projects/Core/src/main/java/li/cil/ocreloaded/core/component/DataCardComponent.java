package li.cil.ocreloaded.core.component;

import li.cil.ocreloaded.core.machine.component.AnnotatedComponent;
import li.cil.ocreloaded.core.network.NetworkNode;

public class DataCardComponent extends AnnotatedComponent {
    
    public DataCardComponent(NetworkNode networkNode) {
        super("data", networkNode);
    }

}
