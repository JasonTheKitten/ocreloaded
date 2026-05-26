package li.cil.ocreloaded.minecraft.common.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import li.cil.ocreloaded.minecraft.common.entity.PowerConverterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;

public class PowerConverterBlock extends Block implements EntityBlock {

    public PowerConverterBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos blockPos, @Nonnull BlockState blockState) {
        return new PowerConverterBlockEntity(blockPos, blockState);
    }

    @Override
    public void playerDestroy(
        @Nonnull Level level, @Nonnull Player player, @Nonnull BlockPos blockPos,
        @Nonnull BlockState blockState, @Nullable BlockEntity blockEntity, @Nonnull ItemStack itemStack
    ) {
        super.playerDestroy(level, player, blockPos, blockState, blockEntity, itemStack);
        popResource(level, blockPos, new ItemStack(this));
    }

}
