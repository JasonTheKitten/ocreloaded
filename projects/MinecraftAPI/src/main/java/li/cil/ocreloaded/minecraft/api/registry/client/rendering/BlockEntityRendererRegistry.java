package li.cil.ocreloaded.minecraft.api.registry.client.rendering;

import java.util.function.Function;

import li.cil.ocreloaded.minecraft.api.ServiceHelper;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface BlockEntityRendererRegistry {

    static final BlockEntityRendererRegistry service = ServiceHelper.loadService(BlockEntityRendererRegistry.class);

    <T extends BlockEntity> void registerS(BlockEntityType<T> blockEntityType, Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<T>> factory);

    static <T extends BlockEntity> void register(BlockEntityType<T> blockEntityType, Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<T>> factory) {
        service.registerS(blockEntityType, factory);
    }
    
}
