package li.cil.ocreloaded.core.network.imp;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import li.cil.ocreloaded.core.machine.PersistenceHolder;
import li.cil.ocreloaded.core.machine.component.Component;
import li.cil.ocreloaded.core.network.Network;
import li.cil.ocreloaded.core.network.NetworkNode;
import li.cil.ocreloaded.core.network.NetworkNodePersistence;

import javax.annotation.Nullable;

public class ComponentNetworkNode implements NetworkNode {

    private @Nullable Component component;
    private final Visibility visibility;
    private final UUID id;
    private Network network;

    public ComponentNetworkNode(UUID id, Function<NetworkNode, Component> componentFactory, Visibility visibility) {
        this.id = id;
        this.component = null;
        this.visibility = visibility;
        this.network = new NetworkImp(this);
        this.component = componentFactory.apply(this);
    }

    public ComponentNetworkNode(UUID id, @Nullable Component component, Visibility visibility) {
        this.id = id;
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
        return Optional.ofNullable(component);
    }

    @Override
    public void onConnect(NetworkNode otherNode) {
        if (component != null) {
            component.onConnect(otherNode);
        }
    }

    @Override
    public void onDisconnect(NetworkNode otherNode) {
        if (component != null) {
            component.onDisconnect(otherNode);
        }
    }

    @Override
    public void onNetworkChange(Network oldNetwork, Network newNetwork) {
        this.network = newNetwork;
    }

    @Override
    public void save(PersistenceHolder persistenceHolder) {
        NetworkNodePersistence.saveId(persistenceHolder, id);
    }
}
