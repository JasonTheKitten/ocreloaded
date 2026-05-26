package li.cil.ocreloaded.fabric.common.energy;

import li.cil.ocreloaded.minecraft.common.energy.EnergyAccess;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import team.reborn.energy.api.EnergyStorage;

public class FabricEnergyStorage extends SnapshotParticipant<Double> implements EnergyStorage {

    private final EnergyAccess access;

    public FabricEnergyStorage(EnergyAccess access) {
        this.access = access;
    }

    @Override
    protected Double createSnapshot() {
        return access.buffer().getEnergy();
    }

    @Override
    protected void readSnapshot(Double snapshot) {
        access.buffer().setEnergy(snapshot);
    }

    @Override
    protected void onFinalCommit() {
        access.onChanged().run();
    }

    @Override
    public boolean supportsInsertion() {
        return access.canReceive();
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        if (!supportsInsertion()) return 0;
        double inserted = Math.min(maxAmount, access.buffer().getCapacity() - access.buffer().getEnergy());
        if (inserted <= 0) return 0;
        updateSnapshots(transaction);
        return (long) access.buffer().insert(inserted);
    }

    @Override
    public boolean supportsExtraction() {
        return access.canExtract();
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        if (!supportsExtraction()) return 0;
        double extracted = Math.min(maxAmount, access.buffer().getEnergy());
        if (extracted <= 0) return 0;
        updateSnapshots(transaction);
        return (long) access.buffer().extract(extracted);
    }

    @Override
    public long getAmount() {
        return (long) access.buffer().getEnergy();
    }

    @Override
    public long getCapacity() {
        return (long) access.buffer().getCapacity();
    }

}
