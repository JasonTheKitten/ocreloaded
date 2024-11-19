package li.cil.ocreloaded.minecraft.common.persistence;

import li.cil.ocreloaded.core.machine.PersistenceHolder;
import net.minecraft.nbt.CompoundTag;

public class NBTPersistenceHolder implements PersistenceHolder {

    private final CompoundTag tag;

    public NBTPersistenceHolder(CompoundTag tag) {
        this.tag = tag;
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
        tag.putLong(key, val);
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
        return tag.getLong(key);
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
    public boolean hasKey(String key) {
        return tag.contains(key);
    }
    
}
