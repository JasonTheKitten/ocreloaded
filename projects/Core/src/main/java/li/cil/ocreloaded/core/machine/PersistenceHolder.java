package li.cil.ocreloaded.core.machine;

import java.util.UUID;

public interface PersistenceHolder {
    
    void storeSubHolder(String key, PersistenceHolder val);
    void storeBool(String key, boolean val);
    void storeInt(String key, int val);
    void storeLong(String key, long val);
    void storeShort(String key, short val);
    void storeByte(String key, byte val);
    void storeString(String key, String val);
    void storeUUID(String key, UUID val);

    PersistenceHolder loadSubHolder(String key);
    boolean loadBool(String key);
    int loadInt(String key);
    long loadLong(String key);
    short loadShort(String key);
    byte loadByte(String key);
    String loadString(String key);
    UUID loadUUID(String key);

    boolean hasKey(String key);
}
