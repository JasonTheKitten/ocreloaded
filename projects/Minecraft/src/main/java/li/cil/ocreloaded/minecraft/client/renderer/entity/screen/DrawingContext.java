package li.cil.ocreloaded.minecraft.client.renderer.entity.screen;

import net.minecraft.resources.ResourceLocation;

public interface DrawingContext {

    void setColor(float r, float g, float b, float t);

    void fill(int startX, int startY, int endX, int endY, int color);

    void blit(ResourceLocation textureLocation, int adjustedDestX, int adjustedDestY, int adjustedDestW, int adjustedDestH,
            int srcX, int srcY, int srcW, int srcH, int textureWidth, int textureHeight);
    
}
