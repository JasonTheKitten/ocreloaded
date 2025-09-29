package li.cil.ocreloaded.minecraft.api;

import li.cil.ocreloaded.minecraft.api.service.ModUtilService;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityTypeHelper {

    static final ModUtilService service = ServiceHelper.loadService(ModUtilService.class);

    public static <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntityConstructor<T> constructor, Block... blocks) {
        return service.createBlockEntityType(constructor, blocks);
    };
    
    public static interface BlockEntityConstructor<T extends BlockEntity> {
        T get(BlockPos blockPos, BlockState blockState);
    }

}
