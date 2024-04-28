package li.cil.ocreloaded.minecraft.common.block;


import dev.architectury.registry.menu.MenuRegistry;
import io.netty.buffer.Unpooled;
import li.cil.ocreloaded.minecraft.common.entity.CaseBlockEntity;
import li.cil.ocreloaded.minecraft.common.menu.CaseMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
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

    private static final Component MENU_NAME = Component.translatable("gui.ocreloaded.case");
    
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            MenuProvider menuProvider = state.getMenuProvider(level, pos);
            if (menuProvider != null && menuProvider instanceof CaseMenuProvider caseMenuProvider && player instanceof ServerPlayer serverPlayer) {
                MenuRegistry.openExtendedMenu(serverPlayer, menuProvider, caseMenuProvider::writeData);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new CaseMenuProvider(pos);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CaseBlockEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(RUNNING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    private class CaseMenuProvider implements MenuProvider {

        private final BlockPos blockPos;

        public CaseMenuProvider(BlockPos blockPos) {
            this.blockPos = blockPos;
        }
        
        @Override
        public Component getDisplayName() {
            return MENU_NAME;
        }

        @Override
        public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
            FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
            writeData(data);
            return new CaseMenu(windowId, playerInventory, data);
        }

        public void writeData(FriendlyByteBuf data) {
            data.writeBlockPos(this.blockPos);
            data.writeInt(getTier());
        }

    }

}
