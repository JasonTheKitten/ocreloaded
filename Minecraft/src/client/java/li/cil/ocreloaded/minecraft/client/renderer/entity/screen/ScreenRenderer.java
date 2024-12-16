package li.cil.ocreloaded.minecraft.client.renderer.entity.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import li.cil.ocreloaded.core.graphics.TextModeBuffer;
import li.cil.ocreloaded.minecraft.client.renderer.entity.screen.ScreenDisplayRenderer.PositionScale;
import li.cil.ocreloaded.minecraft.common.block.ScreenBlock;
import li.cil.ocreloaded.minecraft.common.entity.ScreenBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.AttachFace;

public class ScreenRenderer implements BlockEntityRenderer<ScreenBlockEntity> {

    private static final float SCREEN_MARGIN = 2f / 16f;

    private ScreenBorderRenderer borderRenderer = new ScreenBorderRenderer();

    public ScreenRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(
        ScreenBlockEntity blockEntity, float partialTick, PoseStack poseStack,
        MultiBufferSource bufferSource, int combinedLight, int combinedOverlay
    ) {
        borderRenderer.render(blockEntity, partialTick, poseStack, bufferSource, combinedLight, combinedOverlay);
        renderScreen(blockEntity, partialTick, poseStack, bufferSource, combinedLight, combinedOverlay);
    }

    private void renderScreen(
        ScreenBlockEntity blockEntity, float partialTick, PoseStack poseStack,
        MultiBufferSource bufferSource, int combinedLight, int combinedOverlay
    ) {
        float totalWidth = blockEntity.getScreenBuffer().getWidth() * ScreenDisplayRenderer.CELL_WIDTH;
        float totalHeight = blockEntity.getScreenBuffer().getHeight() * ScreenDisplayRenderer.CELL_HEIGHT;
        float postMarginWidth = 1 - SCREEN_MARGIN * 2;
        float scaleValue = Math.min(postMarginWidth / totalWidth, postMarginWidth / totalHeight);
        float translateX = (1 - totalWidth * scaleValue) / 2;
        float translateY = (1 - totalHeight * scaleValue) / 2;
        
        poseStack.pushPose();
        orientForFace(poseStack, blockEntity);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
        poseStack.translate(-1 + translateX, -1 + translateY, -.002);
        poseStack.scale(scaleValue, scaleValue, 1);

        TextModeBuffer textModeBuffer = blockEntity.getScreenBuffer();
        DrawingContext drawingContext = new MultiBufferSourceDrawingContext(bufferSource, poseStack.last().pose());
        PositionScale positionScale = new PositionScale(0, 0, 1.0f);
        
        // TODO: Improve FPS - GuiGraphics flushes too often, so maybe make alternate interface
        ScreenDisplayRenderer.renderDisplay(drawingContext, positionScale, textModeBuffer);

        poseStack.popPose();
    }

    private void orientForFace(PoseStack poseStack, BlockEntity blockEntity) {
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

        // Rotate to side
        if (attachFace == AttachFace.WALL) {
            poseStack.mulPose(Axis.YP.rotationDegrees(0));
        } else {
            poseStack.mulPose(Axis.XP.rotationDegrees(attachFace == AttachFace.FLOOR ? 270 : 90));
        }

        // Untranslate
        poseStack.translate(-.5, -.5, -.5);
    }
    
}
