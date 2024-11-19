package li.cil.ocreloaded.core.machine.architecture.component;

import java.util.Map;
import java.util.UUID;

import li.cil.ocreloaded.core.machine.PersistenceHolder;

/**
 * Base interface for components that can be used with a machine.
 */
public interface Component {
    
    /**
     * Get the unique identifier of this component.
     * @return the unique identifier of this component.
     */
    UUID getId();

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
     * Load component data from a persistent data store.
     * @param holder The persistence holder to store persistent data.
     */
    void loadFromState(PersistenceHolder holder);

    /**
     * Store component data into a persistent data store.
     * @param holder The persistence holder to store persistent data.
     */
    void storeIntoState(PersistenceHolder holder);

}