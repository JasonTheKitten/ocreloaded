package li.cil.ocreloaded.minecraft.common.persistence;

import li.cil.ocreloaded.core.machine.PersistenceHolder;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class NBTPersistenceHolder implements PersistenceHolder {

    private final CompoundTag tag;
    private final String prefix;

    public NBTPersistenceHolder(CompoundTag tag, String prefix) {
        this.tag = tag;
        this.prefix = prefix;
    }

    @Override
    public void storeSubHolder(String key, PersistenceHolder val) {
        // tag.getAllKeys().stream().filter(s -> s.startsWith(((NBTPersistenceHolder)val).prefix()));
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'storeSubHolder'");
    }

    @Override
    public void storeBool(String key, boolean val) {
        tag.putBoolean(prefix + key, val);
    }

    @Override
    public void storeInt(String key, int val) {
        tag.putInt(prefix + key, val);
    }

    @Override
    public void storeLong(String key, long val) {
        tag.putLong(prefix + key, val);
    }

    @Override
    public void storeShort(String key, short val) {
        tag.putShort(prefix + key, val);
    }

    @Override
    public void storeByte(String key, byte val) {
        tag.putByte(prefix + key, val);
    }

    @Override
    public void storeString(String key, String val) {
        tag.putString(prefix + key, val);
    }

    @Override
    public void storeUUID(String key, UUID val) {
        tag.putUUID(prefix + key, val);
    }

    @Override
    public PersistenceHolder loadSubHolder(String key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadSubHolder'");
    }

    @Override
    public boolean loadBool(String key) {
        return tag.getBoolean(prefix + key);
    }

    @Override
    public int loadInt(String key) {
        return tag.getInt(prefix + key);
    }

    @Override
    public long loadLong(String key) {
        return tag.getLong(prefix + key);
    }

    @Override
    public short loadShort(String key) {
        return tag.getShort(prefix + key);
    }

    @Override
    public byte loadByte(String key) {
        return tag.getByte(prefix + key);
    }

    @Override
    public String loadString(String key) {
        return tag.getString(prefix + key);
    }

    @Override
    public UUID loadUUID(String key) {
        return tag.getUUID(prefix + key);
    }

    @Override
    public boolean hasKey(String key) {
        return tag.contains(prefix + key);
    }

    String prefix() {
        return prefix;
    }
}
