package li.cil.ocreloaded.core.machine;

//TODO: hook this in and set this up.
public interface Persistable {
    void load(PersistenceHolder holder);
    void save(PersistenceHolder holder);
}
