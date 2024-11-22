package li.cil.ocreloaded.minecraft.client.renderer.entity;

import org.joml.Quaternionf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import li.cil.ocreloaded.minecraft.client.assets.ClientTextures;
import li.cil.ocreloaded.minecraft.common.block.CaseBlock;
import li.cil.ocreloaded.minecraft.common.entity.CaseBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class CaseRenderer implements BlockEntityRenderer<CaseBlockEntity> {

    public CaseRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(CaseBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        poseStack.pushPose();

        poseStack.translate(.5, .5, 0.5);
        Direction direction = blockEntity.getBlockState().getValue(CaseBlock.FACING);
        applyBlockRotation(poseStack, direction);
        poseStack.translate(0, 0, .505);

        if (blockEntity.isPowered()) {
            RenderUtil.renderOverlayTexture(poseStack, bufferSource, ClientTextures.CASE_FRONT_RUNNING);
        }

        poseStack.popPose();
    }

    public static void applyBlockRotation(PoseStack poseStack, Direction direction) {
        Quaternionf rotation = switch (direction) {
            case SOUTH -> Axis.YP.rotationDegrees(0);
            case NORTH -> Axis.YP.rotationDegrees(180);
            case EAST -> Axis.YP.rotationDegrees(90);
            case WEST -> Axis.YP.rotationDegrees(270);
            default -> Axis.YP.rotationDegrees(0);
        };

        poseStack.mulPose(rotation);
    }
    
}
