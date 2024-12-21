package li.cil.ocreloaded.core.network;

import java.util.Set;
import java.util.UUID;

public interface Network {
    
    void connect(NetworkNode nodeA, NetworkNode nodeB);

    void disconnect(NetworkNode nodeA, NetworkNode nodeB);

    void rename(UUID oldName, UUID newName);

    boolean reachable(NetworkNode source, NetworkNode target);

    Set<NetworkNode> reachableNodes(NetworkNode source);

}
