package li.cil.ocreloaded.minecraft.common.entity;

import li.cil.ocreloaded.core.energy.EnergyConstants;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PowerConverterBlockEntity extends EnergyStorageBlockEntity {

    public PowerConverterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CommonRegistered.POWER_CONVERTER_BLOCK_ENTITY.get(), blockPos, blockState, EnergyConstants.POWER_CONVERTER_ENERGY_CAPACITY);
    }

}
