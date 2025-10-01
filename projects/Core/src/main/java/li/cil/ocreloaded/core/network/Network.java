package li.cil.ocreloaded.core.network;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface Network {
    
    void connect(NetworkNode nodeA, NetworkNode nodeB);

    void disconnect(NetworkNode nodeA, NetworkNode nodeB);

    void remove(NetworkNode node);

    void rename(UUID oldName, UUID newName);

    boolean reachable(NetworkNode source, NetworkNode target);

    Set<NetworkNode> reachableNodes(NetworkNode source);

    Optional<NetworkNode> reachableNode(NetworkNode networkNode, UUID nodeId);

    void sendToReachable(NetworkNode networkNode, NetworkMessage networkMessage);

    void sendToNeighbors(NetworkNode source, NetworkMessage message);

    void debug();

}
