package li.cil.ocreloaded.minecraft.common.energy;

import li.cil.ocreloaded.core.energy.EnergyBuffer;

public record EnergyAccess(EnergyBuffer buffer, boolean canReceive, boolean canExtract, Runnable onChanged) {

    public EnergyAccess {
        if (onChanged == null) {
            onChanged = () -> {};
        }
    }

    public static EnergyAccess receiveOnly(EnergyBuffer buffer, Runnable onChanged) {
        return new EnergyAccess(buffer, true, false, onChanged);
    }

    public static EnergyAccess storage(EnergyBuffer buffer, Runnable onChanged) {
        return new EnergyAccess(buffer, true, true, onChanged);
    }

    public double insert(double amount, boolean simulate) {
        if (!canReceive) return 0;
        double inserted = simulate ? Math.min(Math.max(0, amount), buffer.getCapacity() - buffer.getEnergy()) : buffer.insert(amount);
        if (!simulate && inserted > 0) {
            onChanged.run();
        }
        return inserted;
    }

    public double extract(double amount, boolean simulate) {
        if (!canExtract) return 0;
        double extracted = simulate ? Math.min(Math.max(0, amount), buffer.getEnergy()) : buffer.extract(amount);
        if (!simulate && extracted > 0) {
            onChanged.run();
        }
        return extracted;
    }

}
