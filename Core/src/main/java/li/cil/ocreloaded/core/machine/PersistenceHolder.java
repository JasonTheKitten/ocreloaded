package li.cil.ocreloaded.core.machine;

import java.util.List;

public abstract class PersistenceHolder {
    abstract void  storeSubHolder(String key, PersistenceHolder val);
    abstract void       storeBool(String key, boolean val);
    abstract void        storeInt(String key, int val);
    abstract void       storeLong(String key, long val);
    abstract void      storeShort(String key, short val);
    abstract void       storeByte(String key, byte val);

    abstract PersistenceHolder loadSubHolder(String key);
    abstract boolean                loadBool(String key);
    abstract int                     loadInt(String key);
    abstract long                   loadLong(String key);
    abstract short                 loadShort(String key);
    abstract byte                   loadByte(String key);
}
