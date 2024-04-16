package li.cil.ocreloaded.minecraft.client.screen.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ButtonWidget extends AbstractButton {

    private static final int TEXTURE_WIDTH = 36;
    private static final int TEXTURE_HEIGHT = 36;

    private final ResourceLocation texture;

    private final int x;
    private final int y;

    private boolean isPressed;

    public ButtonWidget(int x, int y, int width, int height, ResourceLocation texture) {
        super(x, y, width, height, Component.literal("button"));
        this.texture = texture;
        this.x = x;
        this.y = y;
    }

    @Override
    public void onPress() {
        this.isPressed = !this.isPressed;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'updateWidgetNarration'");
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (!this.isPressed && !this.isHovered) {
            guiGraphics.blit(texture, this.x, this.y, 0, 0, this.width, this.height, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        } else if (!this.isPressed && this.isHovered) {
            guiGraphics.blit(texture, this.x, this.y, 0, this.height, this.width, this.height, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        } else if (this.isPressed && !this.isHovered) {
            guiGraphics.blit(texture, this.x, this.y, this.width, 0, this.width, this.height, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        } else {
            guiGraphics.blit(texture, this.x, this.y, this.width, this.height, this.width, this.height, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        }
    }
    
}
