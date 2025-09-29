package li.cil.ocreloaded.minecraft.api.service;

import li.cil.ocreloaded.minecraft.api.BlockEntityTypeHelper.BlockEntityConstructor;
import li.cil.ocreloaded.minecraft.api.registry.registries.DeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface ModUtilService {
    
    <T> DeferredRegister<T> createDeferredRegister(String modId, ResourceKey<Registry<T>> registryKey);

    <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntityConstructor<T> constructor, Block[] blocks);

}
