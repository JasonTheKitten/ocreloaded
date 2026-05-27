package li.cil.ocreloaded.core.network;

import java.util.UUID;
import java.util.function.Function;

import javax.annotation.Nullable;

import li.cil.ocreloaded.core.machine.component.Component;
import li.cil.ocreloaded.core.network.imp.ComponentNetworkNode;

public final class NetworkNodes {
    private NetworkNodes() {}

    public static NetworkNode component(UUID id, Function<NetworkNode, Component> componentFactory, NetworkNode.Visibility visibility) {
        return new ComponentNetworkNode(id, componentFactory, visibility);
    }

    public static NetworkNode component(UUID id, @Nullable Component component, NetworkNode.Visibility visibility) {
        return new ComponentNetworkNode(id, component, visibility);
    }

    public static LazyNetworkNode lazy(Function<UUID, NetworkNode> factory) {
        return new LazyNetworkNode(factory);
    }
}
