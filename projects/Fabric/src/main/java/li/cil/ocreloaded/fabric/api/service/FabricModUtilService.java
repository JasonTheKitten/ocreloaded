package li.cil.ocreloaded.fabric.api.service;

import li.cil.ocreloaded.fabric.api.registry.registries.FabricDeferredRegister;
import li.cil.ocreloaded.minecraft.api.BlockEntityTypeHelper.BlockEntityConstructor;
import li.cil.ocreloaded.minecraft.api.registry.registries.DeferredRegister;
import li.cil.ocreloaded.minecraft.api.service.ModUtilService;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class FabricModUtilService implements ModUtilService {

    @Override
    public <T> DeferredRegister<T> createDeferredRegister(String modId, ResourceKey<Registry<T>> registryKey) {
        return new FabricDeferredRegister<T>(modId, registryKey);
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntityConstructor<T> constructor, Block[] blocks) {
        return BlockEntityType.Builder.of(constructor::get, blocks).build(null);
    }
    
}
