package li.cil.ocreloaded.core.machine.component;

import java.util.Map;

import li.cil.ocreloaded.core.machine.Persistable;
import li.cil.ocreloaded.core.machine.PersistenceHolder;
import li.cil.ocreloaded.core.network.NetworkMessage;
import li.cil.ocreloaded.core.network.NetworkNode;

/**
 * Base interface for components that can be used with a machine.
 */
public interface Component extends Persistable {

    /**
     * Get the network node associated with this component.
     * @return the network node associated with this component.
     */
    NetworkNode getNetworkNode();

    /**
     * Get the type of this component.
     * @return the type of this component.
     */
    String getType();

    /**
     * Get the invokable calls that this component provides.
     * @return the invokable calls that this component provides.
     */
    Map<String, ComponentCall> componentCalls();

    /**
     * Handle messages sent to this component.
     * @param message The message to handle.
     * @param sender The sender of the message.
     */
    default void onMessage(NetworkMessage message, NetworkNode sender) {};

    /**
     * Handle when a component is connected to a network.
     * @param node The network node holding the component.
     */
    default void onConnect(NetworkNode node) {}

    /**
     * Handle when a component is disconnected from a network.
     * @param node The network node holding the component.
     */
    default void onDisconnect(NetworkNode node) {}

    /**
     * Load component data from a persistent data store.
     * @param holder The persistence holder to store persistent data.
     */
    @Override
    default void load(PersistenceHolder holder) {
        getNetworkNode().load(holder);
    };

    /**
     * Store component data into a persistent data store.
     * @param holder The persistence holder to store persistent data.
     */
    @Override
    default void save(PersistenceHolder holder) {
        getNetworkNode().save(holder);
    };

    default void dispose() {}

}