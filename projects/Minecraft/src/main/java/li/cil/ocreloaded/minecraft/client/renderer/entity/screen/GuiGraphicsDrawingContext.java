package li.cil.ocreloaded.minecraft.client.renderer.entity.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiGraphicsDrawingContext implements DrawingContext {

    private final GuiGraphics graphics;

    public GuiGraphicsDrawingContext(GuiGraphics graphics) {
        this.graphics = graphics;
    }

    @Override
    public void setColor(float r, float g, float b, float t) {
        this.graphics.setColor(r, g, b, t);
    }

    @Override
    public void fill(int startX, int startY, int endX, int endY, int color) {
        this.graphics.fill(startX, startY, endX, endY, color);
    }

    @Override
    public void blit(
        ResourceLocation textureLocation, int adjustedDestX, int adjustedDestY, int adjustedDestW, int adjustedDestH,
        int srcX, int srcY, int srcW, int srcH, int textureWidth, int textureHeight
    ) {
        ResourceLocation adjustedTextureLocation = ResourceLocation.fromNamespaceAndPath(textureLocation.getNamespace(), "textures/" + textureLocation.getPath() + ".png");
        this.graphics.blit(adjustedTextureLocation, adjustedDestX, adjustedDestY, adjustedDestW, adjustedDestH, srcX, srcY, srcW, srcH, textureWidth, textureHeight);
    }
    
}
