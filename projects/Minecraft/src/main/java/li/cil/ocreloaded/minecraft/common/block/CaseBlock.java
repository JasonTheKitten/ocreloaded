package li.cil.ocreloaded.minecraft.common.block;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import li.cil.ocreloaded.minecraft.common.entity.CaseBlockEntity;
import li.cil.ocreloaded.minecraft.common.util.IPlatformMenuHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

public class CaseBlock extends Block implements EntityBlock, TieredBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty RUNNING = BooleanProperty.create("running");
    
    private final int tier;

    public CaseBlock(Properties properties, int tier) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
        this.registerDefaultState(this.stateDefinition.any().setValue(RUNNING, false));

        this.tier = tier;
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    @Override
    public InteractionResult useWithoutItem(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CaseBlockEntity caseBlockEntity && player instanceof ServerPlayer serverPlayer) {
            IPlatformMenuHelper.INSTANCE.openExtendedMenu(serverPlayer, caseBlockEntity, caseBlockEntity::writeData);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(@Nonnull BlockState blockState, @Nonnull Level level, @Nonnull BlockPos blockPos, @Nonnull BlockState blockState2, boolean bl) {
        Containers.dropContentsOnDestroy(blockState, blockState2, level, blockPos);
        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }
    
    @Override
    public void playerDestroy(
        @Nonnull Level level, @Nonnull Player player, @Nonnull BlockPos blockPos,
        @Nonnull BlockState blockState, @Nullable BlockEntity blockEntity, @Nonnull ItemStack itemStack
    ) {
        super.playerDestroy(level, player, blockPos, blockState, blockEntity, itemStack);
        popResource(level, blockPos, new ItemStack(this));
        // TODO: How to instead use loot table and respect tier?
    }

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos blockPos, @Nonnull BlockState blockState) {
        return new CaseBlockEntity(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(RUNNING);
    }

    @Override
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
}
