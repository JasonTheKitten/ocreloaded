package li.cil.ocreloaded.minecraft.common.component;

import java.util.Optional;
import java.util.UUID;

import li.cil.ocreloaded.core.machine.PersistenceHolder;
import li.cil.ocreloaded.core.machine.component.Component;
import li.cil.ocreloaded.core.network.Network;
import li.cil.ocreloaded.core.network.NetworkNode;
import li.cil.ocreloaded.core.network.imp.NetworkImp;

public class ComponentNetworkNode implements NetworkNode {

    private final Optional<Component> component;
    private final Visibility visibility;
    private Network network;
    private UUID id = UUID.randomUUID();

    public ComponentNetworkNode(Optional<Component> component, Visibility visibility) {
        this.component = component;
        this.visibility = visibility;
        this.network = new NetworkImp(this);
    }

    @Override
    public Network network() {
        return network;
    }

    @Override
    public UUID id() {
        return id;
    }

    @Override
    public Visibility visibility() {
        return visibility;
    }

    @Override
    public Optional<Component> component() {
        return component;
    }

    @Override
    public void onConnect(NetworkNode otherNode) {}

    @Override
    public void onDisconnect(NetworkNode otherNode) {}

    @Override
    public void onNetworkChange(Network oldNetwork, Network newNetwork) {
        this.network = newNetwork;
    }

    @Override
    public void save(PersistenceHolder persistenceHolder) {
        persistenceHolder.storeLong("INTERNAL_ID1", id.getMostSignificantBits());
        persistenceHolder.storeLong("INTERNAL_ID2", id.getLeastSignificantBits());
    }

    @Override
    public void load(PersistenceHolder persistenceHolder) {
        UUID oldId = id;
        if (!(persistenceHolder.hasKey("INTERNAL_ID1") && persistenceHolder.hasKey("INTERNAL_ID2"))) {
            if (id == null) id = UUID.randomUUID();
        } else {
            id = new UUID(
                persistenceHolder.loadLong("INTERNAL_ID1"),
                persistenceHolder.loadLong("INTERNAL_ID2")
            );
        }
        network.rename(oldId, id);
    }
    
}
