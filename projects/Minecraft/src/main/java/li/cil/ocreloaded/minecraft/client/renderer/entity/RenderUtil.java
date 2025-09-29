package li.cil.ocreloaded.minecraft.client.renderer.entity;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;

public final class RenderUtil {

    private RenderUtil() {}

    public static void renderOverlayTexture(
        PoseStack poseStack, MultiBufferSource bufferSource, ResourceLocation overlayTexture
    ) {
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(overlayTexture);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.translucent());

        Matrix4f matrix = poseStack.last().pose();
        for (int i = 0; i < 4; i++) {
            putVertexWithAO(vertexConsumer, matrix, getVertexPosX(i), getVertexPosY(i), sprite, 1, 0xF000F0);
        }
    }

    public static void renderSideTexture(
        PoseStack poseStack, MultiBufferSource bufferSource, ResourceLocation overlayTexture, 
        BlockAndTintGetter blockView, BlockPos blockPos, Direction face
    ) {
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(overlayTexture);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.translucent());

        AmbientOcclusionCalculator aoCalculator = new AmbientOcclusionCalculator();
        aoCalculator.calculate(blockView, blockView.getBlockState(blockPos), blockPos, face);

        Matrix4f matrix = poseStack.last().pose();
        for (int i = 0; i < 4; i++) {
            putVertexWithAO(vertexConsumer, matrix, getVertexPosX(i), getVertexPosY(i), sprite, aoCalculator.brightness[i], aoCalculator.lightmap[i]);
        }
    }

    private static void putVertexWithAO(VertexConsumer vertexConsumer, Matrix4f matrix, float x, float y, TextureAtlasSprite sprite, float brightness, int lightmap) {
        vertexConsumer
            .addVertex(matrix, x, y, 0)
            .setColor((int) (brightness * 255), (int) (brightness * 255), (int) (brightness * 255), 255)
            .setUv(sprite.getU(x > 0 ? 1 : 0), sprite.getV(y > 0 ? 0 : 1))
            .setLight(lightmap)
            .setNormal(0, 0, 0);
    }

    private static float getVertexPosX(int vertexIndex) {
        return (vertexIndex == 1 || vertexIndex == 2) ? 0.5f : -0.5f;
    }

    private static float getVertexPosY(int vertexIndex) {
        return (vertexIndex == 2 || vertexIndex == 3) ? 0.5f : -0.5f;
    }
    
}
