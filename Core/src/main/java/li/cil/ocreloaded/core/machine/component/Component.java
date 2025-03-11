package li.cil.ocreloaded.core.machine.component;

import java.util.Map;

import li.cil.ocreloaded.core.machine.PersistenceHolder;
import li.cil.ocreloaded.core.network.NetworkMessage;
import li.cil.ocreloaded.core.network.NetworkNode;

/**
 * Base interface for components that can be used with a machine.
 */
public interface Component {

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
     * Load component data from a persistent data store.
     * @param holder The persistence holder to store persistent data.
     */
    void loadFromState(PersistenceHolder holder);

    /**
     * Store component data into a persistent data store.
     * @param holder The persistence holder to store persistent data.
     */
    void storeIntoState(PersistenceHolder holder);

    default void dispose() {}

}