package li.cil.ocreloaded.fabric.api.registry.client.rendering;

import java.util.function.Function;

import li.cil.ocreloaded.minecraft.api.registry.client.rendering.BlockEntityRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class FabricBlockEntityRendererRegistry implements BlockEntityRendererRegistry {

    @Override
    public <T extends BlockEntity> void registerS(BlockEntityType<T> blockEntityType, Function<Context, BlockEntityRenderer<T>> factory) {
        BlockEntityRenderers.register(blockEntityType, c -> factory.apply(c));
    }
    
}
