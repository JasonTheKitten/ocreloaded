package li.cil.ocreloaded.core.network;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import li.cil.ocreloaded.core.machine.PersistenceHolder;
import li.cil.ocreloaded.core.machine.component.Component;

public interface NetworkNode {

    Network network();

    UUID id();

    Visibility visibility();

    Optional<Component> component();

    void onConnect(NetworkNode otherNode);

    void onDisconnect(NetworkNode otherNode);

    void onNetworkChange(Network oldNetwork, Network newNetwork);

    void save(PersistenceHolder persistenceHolder);

    void load(PersistenceHolder persistenceHolder);

    default void connect(NetworkNode otherNode) {
        network().connect(this, otherNode);
    }

    default void disconnect(NetworkNode otherNode) {
        network().disconnect(this, otherNode);
    }

    default boolean canReach(NetworkNode otherNode) {
        return network().reachable(this, otherNode);
    }

    default Set<NetworkNode> reachableNodes() {
        return network().reachableNodes(this);
    }

    public static enum Visibility {
        NONE, NEIGHBORS, NETWORK
    }

}
