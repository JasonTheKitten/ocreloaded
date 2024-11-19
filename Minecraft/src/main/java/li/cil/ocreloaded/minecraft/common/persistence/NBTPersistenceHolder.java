package li.cil.ocreloaded.minecraft.common.persistence;

import li.cil.ocreloaded.core.machine.PersistenceHolder;
import net.minecraft.nbt.CompoundTag;

public class NBTPersistenceHolder implements PersistenceHolder {

    private final CompoundTag tag;
    private final String prefix;

    public NBTPersistenceHolder(CompoundTag tag, String prefix) {
        this.tag = tag;
        this.prefix = prefix;
    }

    @Override
    public void storeSubHolder(String key, PersistenceHolder val) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'storeSubHolder'");
    }

    @Override
    public void storeBool(String key, boolean val) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'storeBool'");
    }

    @Override
    public void storeInt(String key, int val) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'storeInt'");
    }

    @Override
    public void storeLong(String key, long val) {
        tag.putLong(prefix + key, val);
    }

    @Override
    public void storeShort(String key, short val) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'storeShort'");
    }

    @Override
    public void storeByte(String key, byte val) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'storeByte'");
    }

    @Override
    public void storeString(String key, String val) {
        tag.putString(prefix + key, val);
    }

    @Override
    public PersistenceHolder loadSubHolder(String key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadSubHolder'");
    }

    @Override
    public boolean loadBool(String key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadBool'");
    }

    @Override
    public int loadInt(String key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadInt'");
    }

    @Override
    public long loadLong(String key) {
        return tag.getLong(prefix + key);
    }

    @Override
    public short loadShort(String key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadShort'");
    }

    @Override
    public byte loadByte(String key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadByte'");
    }

    @Override
    public String loadString(String key) {
        return tag.getString(prefix + key);
    }

    @Override
    public boolean hasKey(String key) {
        return tag.contains(prefix + key);
    }
    
}
