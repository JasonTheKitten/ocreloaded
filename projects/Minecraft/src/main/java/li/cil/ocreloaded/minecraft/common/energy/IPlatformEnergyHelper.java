package li.cil.ocreloaded.minecraft.common.energy;

import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface IPlatformEnergyHelper {

    IPlatformEnergyHelper INSTANCE = ServiceLoader.load(IPlatformEnergyHelper.class).findFirst().orElseThrow();

    <T extends BlockEntity> void registerBlockEntityEnergy(
        Supplier<? extends BlockEntityType<T>> type,
        BlockEntityEnergyProvider<T> provider
    );

    @FunctionalInterface
    interface BlockEntityEnergyProvider<T extends BlockEntity> {
        Optional<EnergyAccess> get(T blockEntity, @Nullable Direction side);
    }

}
