package li.cil.ocreloaded.minecraft.client.registry;

import java.util.function.Function;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public record BlockEntityRendererEntry<T extends BlockEntity>(BlockEntityType<? extends T> blockEntityType, Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<T>> renderer) {
    
}
