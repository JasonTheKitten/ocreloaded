package li.cil.ocreloaded.minecraft.client.screen.widget;

import javax.annotation.Nonnull;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.DataSlot;

public class ButtonWidget extends AbstractButton {

    private static final int TEXTURE_WIDTH = 36;
    private static final int TEXTURE_HEIGHT = 36;

    private final ResourceLocation texture;

    private final int x;
    private final int y;

    private final DataSlot backingStore;
    private final Runnable onPress;

    public ButtonWidget(int x, int y, int width, int height, ResourceLocation texture, DataSlot backingStore, Runnable onPress) {
        super(x, y, width, height, Component.literal("button"));
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.backingStore = backingStore;
        this.onPress = onPress;
    }

    @Override
    public void onPress() {
        this.backingStore.set(1 - this.backingStore.get());
        this.onPress.run();
    }

    @Override
    protected void updateWidgetNarration(@Nonnull NarrationElementOutput narrationElementOutput) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'updateWidgetNarration'");
    }

    @Override
    public void renderWidget(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (!this.isPressed() && !this.isHovered) {
            guiGraphics.blit(texture, this.x, this.y, 0, 0, this.width, this.height, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        } else if (!this.isPressed() && this.isHovered) {
            guiGraphics.blit(texture, this.x, this.y, 0, this.height, this.width, this.height, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        } else if (this.isPressed() && !this.isHovered) {
            guiGraphics.blit(texture, this.x, this.y, this.width, 0, this.width, this.height, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        } else {
            guiGraphics.blit(texture, this.x, this.y, this.width, this.height, this.width, this.height, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        }
    }

    private boolean isPressed() {
        return this.backingStore.get() == 1;
    }
    
}
