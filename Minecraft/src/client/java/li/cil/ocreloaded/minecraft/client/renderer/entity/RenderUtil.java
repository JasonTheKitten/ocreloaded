package li.cil.ocreloaded.minecraft.client.renderer.entity;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public final class RenderUtil {

    public static final int FULL_BRIGHTNESS = 0xF000F0;
    
    private RenderUtil() {}

    public static void renderOverlayTexture(PoseStack poseStack, MultiBufferSource bufferSource, ResourceLocation overlayTexture, int brightness) {
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(overlayTexture);

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.translucent());

        Matrix4f matrix = poseStack.last().pose();
        putVertex(vertexConsumer, matrix, -0.5f, -0.5f, sprite.getU0(), sprite.getV1(), brightness);
        putVertex(vertexConsumer, matrix, 0.5f, -0.5f, sprite.getU1(), sprite.getV1(), brightness);
        putVertex(vertexConsumer, matrix, 0.5f, 0.5f, sprite.getU1(), sprite.getV0(), brightness);
        putVertex(vertexConsumer, matrix, -0.5f, 0.5f, sprite.getU0(), sprite.getV0(), brightness);
    }

    private static void putVertex(VertexConsumer vertexConsumer, Matrix4f matrix, float x, float y, float u, float v, int brightness) {
        vertexConsumer
            .vertex(matrix, x, y, 0)
            .color(255, 255, 255, 255)
            .uv(u, v)
            .uv2(brightness)
            .normal(0, 0, 0)
            .endVertex();
    }
    
}
