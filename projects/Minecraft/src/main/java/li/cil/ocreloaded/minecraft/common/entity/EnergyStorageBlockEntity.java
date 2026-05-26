package li.cil.ocreloaded.minecraft.common.entity;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import li.cil.ocreloaded.core.energy.EnergyBuffer;
import li.cil.ocreloaded.core.energy.SimpleEnergyBuffer;
import li.cil.ocreloaded.core.network.NetworkNode;
import li.cil.ocreloaded.core.network.NetworkNode.Visibility;
import li.cil.ocreloaded.minecraft.common.SettingsConstants;
import li.cil.ocreloaded.minecraft.common.component.ComponentNetworkNode;
import li.cil.ocreloaded.minecraft.common.component.ComponentNetworkUtil;
import li.cil.ocreloaded.minecraft.common.energy.EnergyAccess;
import li.cil.ocreloaded.minecraft.common.persistence.NBTPersistenceHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class EnergyStorageBlockEntity extends BlockEntity implements TickableEntity, ComponentTileEntity {

    private static final String TAG_ENERGY = "ocreloaded:energy";

    private final SimpleEnergyBuffer energyBuffer;
    private final NetworkNode networkNode;
    private boolean initialized;

    protected EnergyStorageBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState, double capacity) {
        super(type, blockPos, blockState);
        this.energyBuffer = new SimpleEnergyBuffer(capacity);
        this.networkNode = new ComponentNetworkNode(Optional.empty(), Optional.of(energyBuffer), Visibility.NONE);
    }

    @Override
    public NetworkNode networkNode() {
        return networkNode;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        networkNode.remove();
    }

    @Override
    public void loadAdditional(@Nonnull CompoundTag compoundTag, @Nonnull HolderLookup.Provider registries) {
        super.loadAdditional(compoundTag, registries);
        networkNode.load(new NBTPersistenceHolder(compoundTag, SettingsConstants.namespace));
        energyBuffer.setEnergy(compoundTag.getDouble(TAG_ENERGY));
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compoundTag, @Nonnull HolderLookup.Provider registries) {
        super.saveAdditional(compoundTag, registries);
        networkNode.save(new NBTPersistenceHolder(compoundTag, SettingsConstants.namespace));
        compoundTag.putDouble(TAG_ENERGY, energyBuffer.getEnergy());
    }

    @Override
    public void setLevel(@Nonnull Level level) {
        super.setLevel(level);
        if (!level.isClientSide()) {
            level.addBlockEntityTicker(new BlockEntityTicker(this));
        }
    }

    @Override
    @SuppressWarnings("null")
    public void tick() {
        if (level == null || level.isClientSide) return;
        if (!initialized) {
            ComponentNetworkUtil.connectToNeighbors(level, worldPosition);
            initialized = true;
        }
    }

    public Optional<EnergyAccess> receiveOnlyEnergyAccess(@Nullable Direction side) {
        return Optional.of(EnergyAccess.receiveOnly(energyBuffer, this::setChanged));
    }

    public EnergyBuffer energyBuffer() {
        return energyBuffer;
    }

}
