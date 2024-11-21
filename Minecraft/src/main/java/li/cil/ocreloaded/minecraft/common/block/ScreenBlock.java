package li.cil.ocreloaded.minecraft.common.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;

public class ScreenBlock extends Block implements TieredBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<AttachFace> ATTACH_FACE = BlockStateProperties.ATTACH_FACE;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;

    private final int tier;

    public ScreenBlock(Properties properties, int tier) {
        super(properties);
        var defaultState = this.stateDefinition.any();
        defaultState = defaultState.setValue(FACING, Direction.NORTH);
        defaultState = defaultState.setValue(ATTACH_FACE, AttachFace.WALL);
        defaultState = defaultState.setValue(UP, false);
        defaultState = defaultState.setValue(DOWN, false);
        defaultState = defaultState.setValue(EAST, false);
        defaultState = defaultState.setValue(WEST, false);
        defaultState = defaultState.setValue(NORTH, false);
        defaultState = defaultState.setValue(SOUTH, false);
        this.registerDefaultState(defaultState);
        this.tier = tier;
    }

    @Override
    public int getTier() {
        return tier;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {

        var blockState = this.defaultBlockState();
        var facing = context.getHorizontalDirection().getOpposite();
        var attachFace = context.getClickedFace() == Direction.DOWN ? AttachFace.FLOOR
            : context.getClickedFace() == Direction.UP ? AttachFace.CEILING
            : AttachFace.WALL;
        
        blockState = blockState.setValue(FACING, facing);
        blockState = blockState.setValue(ATTACH_FACE, attachFace);

        var blockPos = context.getClickedPos();
        var level = context.getLevel();
        var axis = attachFace == AttachFace.WALL ? facing.getAxis() : Direction.Axis.Y;

        // If shift is not pressed, and the block is placed on another screen block, and we aren't placed inline with it's axis, copy the orientation of the block it is placed on.
        var clickedFace = context.getClickedFace();
        if (!context.isSecondaryUseActive()) {
            var blockPos2 = blockPos.relative(clickedFace.getOpposite());
            var blockState2 = level.getBlockState(blockPos2);
            if (blockState2.getBlock() instanceof ScreenBlock) {
                var axis2 = blockState2.getValue(ATTACH_FACE) == AttachFace.WALL ? blockState2.getValue(FACING).getAxis() : Direction.Axis.Y;
                if (axis2 != clickedFace.getAxis()) {
                    blockState = blockState.setValue(FACING, blockState2.getValue(FACING));
                    blockState = blockState.setValue(ATTACH_FACE, blockState2.getValue(ATTACH_FACE));
                    return blockState;
                }
            }
        }
        

        // For each direction in the plane of our placement, check if the orientation of the block in that direction matches.
        // for Floor or Cieling, we only need to check the horizontal directions.
        // for Wall, we need to check the coplanar horizontal and vertical directions.
        for (var direction : Direction.values()) {
            // Skip the directions matcing our monitor axis.
            if (direction.getAxis() == axis) {
                continue;
            }
            blockState = blockState.setValue(getDirectionProperty(direction), checkDirection(blockState, level, blockPos, direction));
        }
        
        return blockState;
    }

    public boolean orientationMatches(BlockState blockState, BlockState relativeBlockState) {
        // Check if the block in the given direction matches the facing and attach face of this block.
        return blockState.getValue(FACING) == relativeBlockState.getValue(FACING)
            && blockState.getValue(ATTACH_FACE) == relativeBlockState.getValue(ATTACH_FACE);
    }

    public Property<Boolean> getDirectionProperty(Direction direction) {
        return switch (direction) {
            case UP -> UP;
            case DOWN -> DOWN;
            case EAST -> EAST;
            case WEST -> WEST;
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            default -> null;
        };
    }

    private boolean checkDirection(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos, Direction direction) {
        // Must not be the same axis as the block.
        var axis = blockState.getValue(ATTACH_FACE) == AttachFace.WALL ? blockState.getValue(FACING).getAxis() : Direction.Axis.Y;
        if (direction.getAxis() == axis) {
            return false;
        }

        var blockPos2 = blockPos.relative(direction);
        var blockState2 = levelAccessor.getBlockState(blockPos2);
        // Must be a screen block.
        if (!(blockState2.getBlock() instanceof ScreenBlock)) {
            return false;
        }

        // Must have the same facing and attach face.
        if (!orientationMatches(blockState, blockState2)) {
            return false;
        }

        return true;
    }

    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        // check and update the connection state of the block in the given direction.
        return blockState.setValue(getDirectionProperty(direction), checkDirection(blockState, levelAccessor, blockPos, direction));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(ATTACH_FACE);
        builder.add(UP);
        builder.add(DOWN);
        builder.add(EAST);
        builder.add(WEST);
        builder.add(NORTH);
        builder.add(SOUTH);
    }
}
