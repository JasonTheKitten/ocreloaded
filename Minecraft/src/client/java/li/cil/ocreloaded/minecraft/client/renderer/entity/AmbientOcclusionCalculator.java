package li.cil.ocreloaded.minecraft.client.renderer.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;

public class AmbientOcclusionCalculator {
    final float[] brightness = new float[4];
    final int[] lightmap = new int[4];

    public void calculate(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Direction face) {
        BlockPos[] cornerOffsets = getCornerOffsets(face);

        for (int i = 0; i < 4; i++) {
            BlockPos cornerPos = pos.offset(cornerOffsets[i]);

            BlockState neighborState = blockView.getBlockState(cornerPos);
            float cornerBrightness = getShadeBrightness(blockView, neighborState, cornerPos);
            int cornerLightmap = getLightColor(blockView, neighborState, cornerPos);

            brightness[i] = cornerBrightness;
            lightmap[i] = cornerLightmap;
        }
    }

    private BlockPos[] getCornerOffsets(Direction face) {
        return switch (face) {
            case UP -> new BlockPos[]{ new BlockPos(-1, 0, -1), new BlockPos(1, 0, -1), new BlockPos(1, 0, 1), new BlockPos(-1, 0, 1) };
            case DOWN -> new BlockPos[]{ new BlockPos(-1, 0, -1), new BlockPos(1, 0, -1), new BlockPos(1, 0, 1), new BlockPos(-1, 0, 1) };
            case NORTH -> new BlockPos[]{ new BlockPos(-1, -1, 0), new BlockPos(1, -1, 0), new BlockPos(1, 1, 0), new BlockPos(-1, 1, 0) };
            case SOUTH -> new BlockPos[]{ new BlockPos(-1, -1, 0), new BlockPos(1, -1, 0), new BlockPos(1, 1, 0), new BlockPos(-1, 1, 0) };
            case EAST -> new BlockPos[]{ new BlockPos(0, -1, -1), new BlockPos(0, -1, 1), new BlockPos(0, 1, 1), new BlockPos(0, 1, -1) };
            case WEST -> new BlockPos[]{ new BlockPos(0, -1, -1), new BlockPos(0, -1, 1), new BlockPos(0, 1, 1), new BlockPos(0, 1, -1) };
            default -> new BlockPos[]{};
        };
    }

    private float getShadeBrightness(BlockAndTintGetter blockView, BlockState state, BlockPos pos) {
        if (state.isSolidRender(blockView, pos)) {
            return 0.2f;
        }
    
        int lightBlocking = state.getLightBlock(blockView, pos);
        if (lightBlocking > 0) {
            return 1.0f - (lightBlocking / 15.0f);
        }
    
        return 1.0f;
    }

    private int getLightColor(BlockAndTintGetter blockView, BlockState state, BlockPos pos) {
        return blockView.getBrightness(LightLayer.BLOCK, pos) << 20 | blockView.getBrightness(LightLayer.SKY, pos) << 4;
    }

}
