package li.cil.ocreloaded.neoforge.common.energy;

import li.cil.ocreloaded.minecraft.common.energy.EnergyAccess;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class NeoEnergyStorage implements IEnergyStorage {

    private final EnergyAccess access;

    public NeoEnergyStorage(EnergyAccess access) {
        this.access = access;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return (int) access.insert(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return (int) access.extract(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return (int) access.buffer().getEnergy();
    }

    @Override
    public int getMaxEnergyStored() {
        return (int) access.buffer().getCapacity();
    }

    @Override
    public boolean canExtract() {
        return access.canExtract();
    }

    @Override
    public boolean canReceive() {
        return access.canReceive();
    }

}
