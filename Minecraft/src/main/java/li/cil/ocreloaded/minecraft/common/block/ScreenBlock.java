package li.cil.ocreloaded.minecraft.common.block;

import li.cil.ocreloaded.minecraft.common.entity.ScreenBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class ScreenBlock extends Block implements EntityBlock, TieredBlock {

    public static final BooleanProperty[] DIRECTIONS = new BooleanProperty[] {
        BlockStateProperties.DOWN, BlockStateProperties.UP, BlockStateProperties.NORTH,
        BlockStateProperties.SOUTH, BlockStateProperties.WEST, BlockStateProperties.EAST
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
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ScreenBlockEntity(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(ATTACH_FACE);
        for (BooleanProperty direction : DIRECTIONS) {
            builder.add(direction);
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
        return myState.setValue(
            getDirectionProperty(placementDirection),
            checkDirectionConnected(myState, levelAccessor, myPos, placementDirection));
    }

    private BlockState updateShapeAllDirections(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos) {
        for (Direction direction : Direction.values()) {
            blockState = blockState.setValue(
                getDirectionProperty(direction),
                checkDirectionConnected(blockState, levelAccessor, blockPos, direction));
        }

        return blockState;
    }

    private BlockState createDefaultState() {
        BlockState defaultState = this.stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(ATTACH_FACE, AttachFace.WALL);
        for (BooleanProperty direction : DIRECTIONS) {
            defaultState = defaultState.setValue(direction, false);
        }

        return defaultState;
    }

    private BlockState determineDefaultPlacement(BlockPlaceContext context, BlockState blockState) {
        Direction facingDirection = context.getHorizontalDirection().getOpposite();
        Direction lookingDirection = context.getNearestLookingDirection();
        
        AttachFace attachFace = lookingDirection == Direction.DOWN ? AttachFace.CEILING
            : lookingDirection == Direction.UP ? AttachFace.FLOOR
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

    public Property<Boolean> getDirectionProperty(Direction direction) {
        return DIRECTIONS[direction.get3DDataValue()];
    }

    public boolean orientationMatches(BlockState blockState, BlockState relativeBlockState) {
        return blockState.getValue(FACING) == relativeBlockState.getValue(FACING)
            && blockState.getValue(ATTACH_FACE) == relativeBlockState.getValue(ATTACH_FACE);
    }

}
