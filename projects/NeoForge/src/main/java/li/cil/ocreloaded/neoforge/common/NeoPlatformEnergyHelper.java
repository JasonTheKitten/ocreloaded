package li.cil.ocreloaded.neoforge.common;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import li.cil.ocreloaded.minecraft.common.energy.IPlatformEnergyHelper;
import li.cil.ocreloaded.neoforge.common.energy.NeoEnergyStorage;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class NeoPlatformEnergyHelper implements IPlatformEnergyHelper {

    private final List<BlockEntityEnergyRegistration<?>> blockEntityEnergyRegistrations = new ArrayList<>();

    @Override
    public <T extends BlockEntity> void registerBlockEntityEnergy(
        Supplier<? extends BlockEntityType<T>> type,
        BlockEntityEnergyProvider<T> provider
    ) {
        blockEntityEnergyRegistrations.add(new BlockEntityEnergyRegistration<>(type, provider));
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (BlockEntityEnergyRegistration<?> registration : blockEntityEnergyRegistrations) {
            registerBlockEntityCapability(event, registration);
        }
    }

    private <T extends BlockEntity> void registerBlockEntityCapability(
        RegisterCapabilitiesEvent event,
        BlockEntityEnergyRegistration<T> registration
    ) {
        event.registerBlockEntity(
            Capabilities.EnergyStorage.BLOCK,
            registration.type().get(),
            (blockEntity, side) -> registration.provider().get(blockEntity, side)
                .map(NeoEnergyStorage::new)
                .orElse(null)
        );
    }

    private record BlockEntityEnergyRegistration<T extends BlockEntity>(
        Supplier<? extends BlockEntityType<T>> type,
        BlockEntityEnergyProvider<T> provider
    ) {}

}
