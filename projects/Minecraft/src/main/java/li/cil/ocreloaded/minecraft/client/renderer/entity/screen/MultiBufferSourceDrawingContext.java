package li.cil.ocreloaded.minecraft.client.renderer.entity.screen;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.VertexConsumer;

import li.cil.ocreloaded.minecraft.client.assets.ClientTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class MultiBufferSourceDrawingContext implements DrawingContext {

    private final MultiBufferSource bufferSource;
    private final Matrix4f pose;

    private final float[] color = new float[] { 1, 1, 1, 1 };

    public MultiBufferSourceDrawingContext(MultiBufferSource bufferSource, Matrix4f pose) {
        this.bufferSource = bufferSource;
        this.pose = pose;
    }

    @Override
    public void setColor(float r, float g, float b, float t) {
        this.color[0] = r;
        this.color[1] = g;
        this.color[2] = b;
        this.color[3] = t;
    }

    @Override
    public void fill(int startX, int startY, int endX, int endY, int color) {
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.translucent());
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(ClientTextures.FONT_NONE);

        putFillVertexWithAO(vertexConsumer, pose, startX, endY, 0, 1, sprite, color);
        putFillVertexWithAO(vertexConsumer, pose, endX, endY, 1, 1, sprite, color);
        putFillVertexWithAO(vertexConsumer, pose, endX, startY, 1, 0, sprite, color);
        putFillVertexWithAO(vertexConsumer, pose, startX, startY, 0, 0, sprite, color);
    }

    @Override
    public void blit(
        ResourceLocation textureLocation, int adjustedDestX, int adjustedDestY, int adjustedDestW, int adjustedDestH,
        int srcX, int srcY, int srcW, int srcH, int textureWidth, int textureHeight
    ) {
        float u0 = (float) srcX / textureWidth;
        float u1 = (float) (srcX + srcW) / textureWidth;
        float v0 = (float) srcY / textureHeight;
        float v1 = (float) (srcY + srcH) / textureHeight;

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.translucent());
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(textureLocation);

        putVertexWithAO(vertexConsumer, pose, adjustedDestX, adjustedDestY + adjustedDestH, u0, v1, sprite);
        putVertexWithAO(vertexConsumer, pose, adjustedDestX + adjustedDestW, adjustedDestY + adjustedDestH, u1, v1, sprite);
        putVertexWithAO(vertexConsumer, pose, adjustedDestX + adjustedDestW, adjustedDestY, u1, v0, sprite);
        putVertexWithAO(vertexConsumer, pose, adjustedDestX, adjustedDestY, u0, v0, sprite);
    }

    private void putVertexWithAO(VertexConsumer vertexConsumer, Matrix4f matrix, float x, float y, float u, float v, TextureAtlasSprite sprite) {
        vertexConsumer
            .addVertex(matrix, x, y, 0)
            .setColor((int) (color[0] * 255), (int) (color[1] * 255), (int) (color[2] * 255), (int) (color[3] * 255))
            .setUv(sprite.getU(u), sprite.getV(v))
            .setLight(0xF000F0)
            .setNormal(0, 0, 0);
    }

    private void putFillVertexWithAO(VertexConsumer vertexConsumer, Matrix4f matrix, float x, float y, float u, float v, TextureAtlasSprite sprite, int myColor) {
        vertexConsumer
            .addVertex(matrix, x, y, .001f)
            .setColor((myColor >> 16) & 0xFF, (myColor >> 8) & 0xFF, myColor & 0xFF, (myColor >> 24) & 0xFF)
            .setUv(sprite.getU(u), sprite.getV(v))
            .setLight(0xF000F0)
            .setNormal(0, 0, 0);
    }

}
