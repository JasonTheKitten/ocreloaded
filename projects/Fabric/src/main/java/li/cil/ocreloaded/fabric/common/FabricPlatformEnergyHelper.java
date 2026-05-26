package li.cil.ocreloaded.fabric.common;

import java.util.function.Supplier;

import li.cil.ocreloaded.fabric.common.energy.FabricEnergyStorage;
import li.cil.ocreloaded.minecraft.common.energy.IPlatformEnergyHelper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import team.reborn.energy.api.EnergyStorage;

public class FabricPlatformEnergyHelper implements IPlatformEnergyHelper {

    @Override
    public <T extends BlockEntity> void registerBlockEntityEnergy(
        Supplier<? extends BlockEntityType<T>> type,
        BlockEntityEnergyProvider<T> provider
    ) {
        EnergyStorage.SIDED.registerForBlockEntity(
            (blockEntity, direction) -> provider.get(blockEntity, direction)
                .map(FabricEnergyStorage::new)
                .orElse(null),
            type.get()
        );
    }

}
