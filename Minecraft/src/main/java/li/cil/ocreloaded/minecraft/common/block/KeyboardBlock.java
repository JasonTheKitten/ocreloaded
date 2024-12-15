package li.cil.ocreloaded.minecraft.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class KeyboardBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<AttachFace> ATTACH_FACE = BlockStateProperties.ATTACH_FACE;

    public KeyboardBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(createDefaultState());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(ATTACH_FACE);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return switch (blockState.getValue(ATTACH_FACE)) {
            case CEILING -> switch (blockState.getValue(FACING)) {
                case NORTH, SOUTH -> Block.box(1.0, 0.0, 4.0, 15.0, 1.0, 12.0);
                default -> Block.box(4.0, 0.0, 1.0, 12.0, 1.0, 15.0);
            };
            case WALL -> switch (blockState.getValue(FACING)) {
                case SOUTH -> Block.box(1.0, 4.0, 0.0, 15.0, 12.0, 1.0);
                case NORTH -> Block.box(1.0, 4.0, 15.0, 15.0, 12.0, 16.0);
                case EAST -> Block.box(0.0, 4.0, 1.0, 1.0, 12.0, 15.0);
                default -> Block.box(15.0, 4.0, 1.0, 16.0, 12.0, 15.0);
            };
            case FLOOR -> switch (blockState.getValue(FACING)) {
                case NORTH, SOUTH -> Block.box(1.0, 15.0, 4.0, 15.0, 16.0, 12.0);
                default -> Block.box(4.0, 15.0, 1.0, 12.0, 16.0, 15.0);
            };
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = this.defaultBlockState();

        Direction facingDirection = context.getHorizontalDirection().getOpposite();
        Direction lookingDirection = context.getNearestLookingDirection();
        
        AttachFace attachFace = lookingDirection == Direction.DOWN ? AttachFace.CEILING
            : lookingDirection == Direction.UP ? AttachFace.FLOOR
            : AttachFace.WALL;
        
        return blockState
            .setValue(FACING, facingDirection)
            .setValue(ATTACH_FACE, attachFace);
    }

    private BlockState createDefaultState() {
        BlockState defaultState = this.stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(ATTACH_FACE, AttachFace.WALL);

        return defaultState;
    }
    
}
