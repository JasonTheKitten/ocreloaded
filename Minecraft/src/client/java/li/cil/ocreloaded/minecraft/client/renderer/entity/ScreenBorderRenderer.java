package li.cil.ocreloaded.minecraft.client.renderer.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import li.cil.ocreloaded.minecraft.common.block.ScreenBlock;
import li.cil.ocreloaded.minecraft.common.entity.ScreenBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;

public class ScreenBorderRenderer {

    private static Logger logger = LoggerFactory.getLogger(ScreenRenderer.class);
    
    public void render(
        ScreenBlockEntity blockEntity, float partialTick, PoseStack poseStack,
        MultiBufferSource bufferSource, int combinedLight, int combinedOverlay
    ) {
        for (Side side : Side.values()) {
            renderSide(poseStack, blockEntity, bufferSource, side, RenderUtil.FULL_BRIGHTNESS);
        }
    }

    private void renderSide(PoseStack poseStack, ScreenBlockEntity blockEntity, MultiBufferSource bufferSource, Side side, int brightness) {
        poseStack.pushPose();
        setPoseSide(poseStack, blockEntity, side);

        String textureName = determineTextureName(blockEntity, side);
        ResourceLocation textureLocation = new ResourceLocation(OCReloadedCommon.MOD_ID, textureName);
        RenderUtil.renderOverlayTexture(poseStack, bufferSource, textureLocation, brightness);

        poseStack.popPose();
    }

    private void setPoseSide(PoseStack poseStack, BlockEntity blockEntity, Side side) {
        orientForFace(poseStack, blockEntity, side);
        poseStack.translate(0, 0, -.5);
        poseStack.last().pose().normal().rotate(Axis.YP.rotationDegrees(180));
    }

    private String determineTextureName(ScreenBlockEntity blockEntity, Side side) {
        BlockState blockState = blockEntity.getBlockState();
        Direction screenDirection = blockState.getValue(ScreenBlock.FACING);
        AttachFace attachFace = blockState.getValue(ScreenBlock.ATTACH_FACE);
        Direction direction = getDirectionForSide(screenDirection, attachFace, side);

        boolean up = sideProperty(blockState, screenDirection, attachFace, Side.TOP);
        boolean down = sideProperty(blockState, screenDirection, attachFace, Side.BOTTOM);
        String upDown = switch(side) {
            case FRONT, BACK, LEFT, RIGHT -> selectPart(up, down, "single", "bottom", "top", "middle");
            case TOP, BOTTOM -> "single";
        };

        boolean left = sideProperty(blockState, screenDirection, attachFace, Side.LEFT);
        boolean right = sideProperty(blockState, screenDirection, attachFace, Side.RIGHT);
        String leftRight = switch(side) {
            case FRONT, TOP, BOTTOM -> selectPart(left, right, "single", "right", "left", "middle");
            case BACK -> selectPart(left, right, "single", "left", "right", "middle");
            case LEFT, RIGHT -> "single";
        };

        StringBuilder textureName = new StringBuilder("block/screen/");
        textureName.append(side == Side.FRONT ? "front" : "back");

        textureName.append("_");
        textureName.append(upDown);
        textureName.append("_");
        textureName.append(leftRight);

        if (direction != Direction.UP && direction != Direction.DOWN && !down) {
            textureName.append("_side");
        }

        return textureName.toString();
    }

    private String selectPart(boolean a, boolean b, String nn, String an, String nb, String ab) {
        if (a && b) return ab;
        else if (a) return an;
        else if (b) return nb;
        else return nn;
    }

    private boolean sideProperty(BlockState blockState, Direction screenDirection, AttachFace attachFace, Side side) {
        Direction direction = getDirectionForSide(screenDirection, attachFace, side);
        return blockState.getValue(ScreenBlock.DIRECTIONS[direction.get3DDataValue()]);
    }

    private void orientForFace(PoseStack poseStack, BlockEntity blockEntity, Side side) {
        Direction screenDirection = blockEntity.getBlockState().getValue(ScreenBlock.FACING);
        AttachFace attachFace = blockEntity.getBlockState().getValue(ScreenBlock.ATTACH_FACE);

        // Block origin
        poseStack.translate(.5, .5, .5);

        

        // Next, rotate to front
        switch (screenDirection) {
            case NORTH -> poseStack.mulPose(Axis.YP.rotationDegrees(0));
            case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(270));
            case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180));
            case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(90));
            default -> poseStack.mulPose(Axis.YP.rotationDegrees(0));
        }

        switch (attachFace) {
            case FLOOR -> poseStack.mulPose(Axis.XP.rotationDegrees(270));
            case CEILING -> poseStack.mulPose(Axis.XP.rotationDegrees(90));
            default -> poseStack.mulPose(Axis.XP.rotationDegrees(0));
        }

        // Rotate to the side
        switch (side) {
            case FRONT -> poseStack.mulPose(Axis.YP.rotationDegrees(0));
            case BACK -> poseStack.mulPose(Axis.YP.rotationDegrees(180));
            case LEFT -> poseStack.mulPose(Axis.YP.rotationDegrees(270));
            case RIGHT -> poseStack.mulPose(Axis.YP.rotationDegrees(90));
            case TOP -> poseStack.mulPose(Axis.XP.rotationDegrees(90));
            case BOTTOM -> poseStack.mulPose(Axis.XP.rotationDegrees(270));
        }
    }

    private Direction getDirectionForSide(Direction screenDirection, AttachFace attachFace, Side side) {
        switch (attachFace) {
            case WALL:
                return switch (side) {
                    case FRONT -> screenDirection;
                    case BACK -> screenDirection.getOpposite();
                    case LEFT -> screenDirection.getClockWise();
                    case RIGHT -> screenDirection.getCounterClockWise();
                    case TOP -> Direction.UP;
                    case BOTTOM -> Direction.DOWN;
                };
            case FLOOR:
                return switch (side) {
                    case FRONT -> Direction.UP;
                    case BACK -> Direction.DOWN;
                    case LEFT -> screenDirection.getClockWise();
                    case RIGHT -> screenDirection.getCounterClockWise();
                    case TOP -> screenDirection;
                    case BOTTOM -> screenDirection.getOpposite();
                };
            case CEILING:
                return switch (side) {
                    case FRONT -> Direction.DOWN;
                    case BACK -> Direction.UP;
                    case LEFT -> screenDirection.getClockWise();
                    case RIGHT -> screenDirection.getCounterClockWise();
                    case TOP -> screenDirection.getOpposite();
                    case BOTTOM -> screenDirection;
                };
            default:
                logger.warn("Unknown attach face: {}", attachFace);
                return Direction.NORTH;
        }
    }

    private static enum Side {
        FRONT, BACK, LEFT, RIGHT, TOP, BOTTOM;
    }

}
