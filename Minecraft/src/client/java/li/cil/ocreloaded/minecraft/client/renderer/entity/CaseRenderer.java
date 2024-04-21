package li.cil.ocreloaded.minecraft.client.renderer.entity;

import org.joml.Matrix4f;
import org.joml.Quaternionf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import li.cil.ocreloaded.minecraft.client.assets.ClientTextures;
import li.cil.ocreloaded.minecraft.common.block.CaseBlock;
import li.cil.ocreloaded.minecraft.common.entity.CaseBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;

public class CaseRenderer implements BlockEntityRenderer<CaseBlockEntity> {

    private static final int FULL_BRIGHTNESS = 0xF000F0;

    public CaseRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(CaseBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        poseStack.pushPose();

        poseStack.translate(.5, .5, 0.5);
        applyBlockRotation(poseStack, blockEntity.getBlockState());
        poseStack.translate(0, 0, .505);

        if (blockEntity.isPowered()) {
            renderOverlayTexture(poseStack, bufferSource, combinedLight, ClientTextures.CASE_FRONT_RUNNING);
        }

        poseStack.popPose();
    }

    private void applyBlockRotation(PoseStack poseStack, BlockState blockState) {
        Quaternionf rotation = switch (blockState.getValue(CaseBlock.FACING)) {
            case SOUTH -> Axis.YP.rotationDegrees(0);
            case NORTH -> Axis.YP.rotationDegrees(180);
            case EAST -> Axis.YP.rotationDegrees(90);
            case WEST -> Axis.YP.rotationDegrees(270);
            default -> Axis.YP.rotationDegrees(0);
        };

        poseStack.mulPose(rotation);
    }

    private void renderOverlayTexture(PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, ResourceLocation overlayTexture) {
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(overlayTexture);

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.translucent());

        Matrix4f matrix = poseStack.last().pose();
        putVertex(vertexConsumer, matrix, -0.5f, -0.5f, sprite.getU0(), sprite.getV1());
        putVertex(vertexConsumer, matrix, 0.5f, -0.5f, sprite.getU1(), sprite.getV1());
        putVertex(vertexConsumer, matrix, 0.5f, 0.5f, sprite.getU1(), sprite.getV0());
        putVertex(vertexConsumer, matrix, -0.5f, 0.5f, sprite.getU0(), sprite.getV0());
    }

    private void putVertex(VertexConsumer vertexConsumer, Matrix4f matrix, float x, float y, float u, float v) {
        vertexConsumer.vertex(matrix, x, y, 0).color(255, 255, 255, 255).uv(u, v).uv2(FULL_BRIGHTNESS).normal(0, 0, 0).endVertex();
    }
    
}
