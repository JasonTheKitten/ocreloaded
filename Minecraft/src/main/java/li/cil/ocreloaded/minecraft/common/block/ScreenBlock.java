package li.cil.ocreloaded.minecraft.common.block;

import dev.architectury.registry.menu.MenuRegistry;
import li.cil.ocreloaded.minecraft.common.entity.ScreenBlockEntity;
import li.cil.ocreloaded.minecraft.common.menu.provider.ScreenMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

public class ScreenBlock extends Block implements EntityBlock, TieredBlock {

    public static final BooleanProperty[] SIDES = new BooleanProperty[] {
        BooleanProperty.create("up"),
        BooleanProperty.create("down"),
        BooleanProperty.create("left"),
        BooleanProperty.create("right"),
    };

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<AttachFace> ATTACH_FACE = BlockStateProperties.ATTACH_FACE;

    private final int tier;

    public ScreenBlock(Properties properties, int tier) {
        super(properties);

        this.registerDefaultState(createDefaultState());
        this.tier = tier;
    }

    @Override
    public int getTier() {
        return tier;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            ((ScreenBlockEntity) level.getBlockEntity(pos)).networkNode().network().debug();
            MenuProvider menuProvider = state.getMenuProvider(level, pos);
            if (
                menuProvider != null && menuProvider instanceof ScreenMenuProvider screenMenuProvider
                && player instanceof ServerPlayer serverPlayer
                && isKeyboardConnected(level, pos)
             ) {
                MenuRegistry.openExtendedMenu(serverPlayer, menuProvider, screenMenuProvider::writeData);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new ScreenMenuProvider(pos, tier);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ScreenBlockEntity(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(ATTACH_FACE);
        for (BooleanProperty side : SIDES) {
            builder.add(side);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = this.defaultBlockState();
        blockState = determineDefaultPlacement(context, blockState);

        if (!context.isSecondaryUseActive()) {
            blockState = copyNearbyeOrientation(context, blockState);
        }

        if (!context.isSecondaryUseActive()) {
            blockState = copyNearbyeOrientation(context, blockState);
        }
        
        blockState = updateShapeAllDirections(blockState, context.getLevel(), context.getClickedPos());
        
        return blockState;
    }

    @Override
    public BlockState updateShape(
        BlockState myState, Direction placementDirection, BlockState otherState,
        LevelAccessor levelAccessor, BlockPos myPos, BlockPos otherPos
    ) {
        Side side = getSideForDirection(myState.getValue(FACING), myState.getValue(ATTACH_FACE), placementDirection);
        if (side == null) return myState;

        return myState.setValue(side.getSideProperty(), checkDirectionConnected(myState, levelAccessor, myPos, placementDirection));
    }

    private BlockState updateShapeAllDirections(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos) {
        for (Direction direction : Direction.values()) {
            Side side = getSideForDirection(blockState.getValue(FACING), blockState.getValue(ATTACH_FACE), direction);
            if (side == null) continue;

            blockState = blockState.setValue(side.getSideProperty(), checkDirectionConnected(blockState, levelAccessor, blockPos, direction));
        }

        return blockState;
    }

    private BlockState createDefaultState() {
        BlockState defaultState = this.stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(ATTACH_FACE, AttachFace.WALL);
        for (BooleanProperty side : SIDES) {
            defaultState = defaultState.setValue(side, false);
        }

        return defaultState;
    }

    private BlockState determineDefaultPlacement(BlockPlaceContext context, BlockState blockState) {
        Direction facingDirection = context.getHorizontalDirection().getOpposite();
        Direction lookingDirection = context.getNearestLookingDirection();
        
        AttachFace attachFace = lookingDirection == Direction.DOWN ? AttachFace.FLOOR
            : lookingDirection == Direction.UP ? AttachFace.CEILING
            : AttachFace.WALL;
        
        return blockState
            .setValue(FACING, facingDirection)
            .setValue(ATTACH_FACE, attachFace);
    }

    private BlockState copyNearbyeOrientation(BlockPlaceContext context, BlockState blockState) {
        Level level = context.getLevel();

        BlockPos placePos = context.getClickedPos();
        Direction clickDirection = context.getClickedFace();
        BlockPos attachPos = placePos.relative(clickDirection.getOpposite());

        BlockState attachState = level.getBlockState(attachPos);

        if (attachState.getBlock() instanceof ScreenBlock screenBlock) {
            Direction attachDirection = attachState.getValue(FACING);
            AttachFace attachFace = attachState.getValue(ATTACH_FACE);
            boolean isSameAxis = attachFace == AttachFace.WALL
                ? attachDirection.getAxis() == clickDirection.getAxis()
                : true;
            boolean isSameTier = screenBlock.tier == this.tier;
            if (!isSameAxis || !isSameTier) {
                blockState = blockState.setValue(FACING, attachDirection);
                blockState = blockState.setValue(ATTACH_FACE, attachFace);
            }
        }

        return blockState;
    }

    private boolean checkDirectionConnected(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos, Direction direction) {
        BlockPos otherPos = blockPos.relative(direction);
        BlockState otherState = levelAccessor.getBlockState(otherPos);
        
        return
            otherState.getBlock() instanceof ScreenBlock screenBlock
            && screenBlock.tier == this.tier
            && orientationMatches(blockState, otherState);
    }

    public boolean orientationMatches(BlockState blockState, BlockState relativeBlockState) {
        return blockState.getValue(FACING) == relativeBlockState.getValue(FACING)
            && blockState.getValue(ATTACH_FACE) == relativeBlockState.getValue(ATTACH_FACE);
    }

    private boolean isKeyboardConnected(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos otherPos = pos.relative(direction);
            BlockState otherState = level.getBlockState(otherPos);
            if (otherState.getBlock() instanceof KeyboardBlock) {
                // TODO: Which way is the keyboard facing?
                return true;
            }
        }

        return false;
    }

    private Side getSideForDirection(Direction screenDirection, AttachFace attachFace, Direction direction) {
        Direction left = screenDirection.getClockWise();
        Direction right = screenDirection.getCounterClockWise();
        Direction up = attachFace == AttachFace.WALL ?
            Direction.UP :
            screenDirection.getOpposite();
        Direction down = up.getOpposite();
    
        if (direction == left) {
            return Side.LEFT;
        } else if (direction == right) {
            return Side.RIGHT;
        } else if (direction == up) {
            return Side.UP;
        } else if (direction == down) {
            return Side.DOWN;
        }
    
        return null;
    }

    private enum Side {
        UP(SIDES[0]), DOWN(SIDES[1]), LEFT(SIDES[2]), RIGHT(SIDES[3]);

        private final BooleanProperty sideProperty;

        Side(BooleanProperty sideProperty) {
            this.sideProperty = sideProperty;
        }
        
        public BooleanProperty getSideProperty() {
            return sideProperty;
        }
    }

}
