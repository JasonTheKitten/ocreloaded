package li.cil.ocreloaded.minecraft.common.entity;

import li.cil.ocreloaded.core.energy.EnergyConstants;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CapacitorBlockEntity extends EnergyStorageBlockEntity {

    public CapacitorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CommonRegistered.CAPACITOR_BLOCK_ENTITY.get(), blockPos, blockState, EnergyConstants.CAPACITOR_ENERGY_CAPACITY);
    }

}
