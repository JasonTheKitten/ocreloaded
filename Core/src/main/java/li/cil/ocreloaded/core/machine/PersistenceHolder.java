package li.cil.ocreloaded.core.machine;

public interface PersistenceHolder {
    
    void storeSubHolder(String key, PersistenceHolder val);
    void storeBool(String key, boolean val);
    void storeInt(String key, int val);
    void storeLong(String key, long val);
    void storeShort(String key, short val);
    void storeByte(String key, byte val);

    PersistenceHolder loadSubHolder(String key);
    boolean loadBool(String key);
    int loadInt(String key);
    long loadLong(String key);
    short loadShort(String key);
    byte loadByte(String key);

    boolean hasKey(String key);

}
