package li.cil.ocreloaded.core.machine.component;

import java.util.Map;

import li.cil.ocreloaded.core.machine.PersistenceHolder;

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