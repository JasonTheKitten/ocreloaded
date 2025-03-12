package li.cil.ocreloaded.core.machine;

public interface Persistable {

    /**
     * Load component data from a persistent data store.
     * @param holder The persistence holder to store persistent data.
     */
    default void load(PersistenceHolder holder) {};

    /**
     * Store component data into a persistent data store.
     * @param holder The persistence holder to store persistent data.
     */
    default void save(PersistenceHolder holder) {};

}
