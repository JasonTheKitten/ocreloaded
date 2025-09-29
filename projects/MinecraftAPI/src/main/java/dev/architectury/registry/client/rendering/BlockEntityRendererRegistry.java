package dev.architectury.registry.client.rendering;

import java.util.function.Function;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityRendererRegistry {

    public static <T extends BlockEntity> void register(BlockEntityType<T> blockEntityType, Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<T>> factory) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'register'");
    }
    
}
