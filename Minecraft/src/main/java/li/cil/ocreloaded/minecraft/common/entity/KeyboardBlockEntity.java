package li.cil.ocreloaded.minecraft.common.entity;

import li.cil.ocreloaded.core.component.KeyboardComponent;
import li.cil.ocreloaded.core.network.NetworkNode;
import li.cil.ocreloaded.core.network.NetworkNode.Visibility;
import li.cil.ocreloaded.minecraft.common.SettingsConstants;
import li.cil.ocreloaded.minecraft.common.component.ComponentNetworkNode;
import li.cil.ocreloaded.minecraft.common.component.ComponentNetworkUtil;
import li.cil.ocreloaded.minecraft.common.persistence.NBTPersistenceHolder;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class KeyboardBlockEntity extends BlockEntityWithTick implements ComponentTileEntity {

    private final NetworkNode networkNode = new ComponentNetworkNode(KeyboardComponent::new, Visibility.NETWORK);

    private boolean initialized = false;

    public KeyboardBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CommonRegistered.KEYBOARD_BLOCK_ENTITY.get(), blockPos, blockState);
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
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        networkNode.load(new NBTPersistenceHolder(compoundTag, SettingsConstants.namespace));
    }

    @Override
    public void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        networkNode.save(new NBTPersistenceHolder(compoundTag, SettingsConstants.namespace));
    }

    // TODO: Find a simpler way to do this than needing both setLevel and a ticker
    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if (level == null) return;

        if (!level.isClientSide()) {
            level.addBlockEntityTicker(new BlockEntityTicker(this));
        }
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide) return;
        if (!initialized) {
            ComponentNetworkUtil.connectToNeighbors(level, worldPosition);
            initialized = true;
        }
    }

}
