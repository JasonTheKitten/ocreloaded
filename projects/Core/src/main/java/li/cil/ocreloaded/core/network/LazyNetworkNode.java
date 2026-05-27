package li.cil.ocreloaded.core.network;

import java.util.UUID;
import java.util.function.Function;

import li.cil.ocreloaded.core.machine.PersistenceHolder;

public final class LazyNetworkNode {

    private final Function<UUID, NetworkNode> factory;

    private UUID id = UUID.randomUUID();
    private NetworkNode node;

    public LazyNetworkNode(Function<UUID, NetworkNode> factory) {
        this.factory = factory;
    }

    public NetworkNode get() {
        if (node == null) {
            node = factory.apply(id);
        }
        return node;
    }

    public void loadId(PersistenceHolder holder) {
        if (node != null && !NetworkNodePersistence.hasId(holder)) return;

        UUID loadedId = NetworkNodePersistence.loadIdOrRandom(holder);
        if (node != null && !node.id().equals(loadedId)) {
            throw new IllegalStateException("Cannot change network node ID after node creation.");
        }
        id = loadedId;
    }

    public void saveId(PersistenceHolder holder) {
        NetworkNodePersistence.saveId(holder, node != null ? node.id() : id);
    }
}
