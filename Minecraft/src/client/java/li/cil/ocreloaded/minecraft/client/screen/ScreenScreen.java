package li.cil.ocreloaded.minecraft.client.screen;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.Key;

import li.cil.ocreloaded.core.graphics.TextModeBuffer;
import li.cil.ocreloaded.minecraft.client.assets.ClientTextures;
import li.cil.ocreloaded.minecraft.client.renderer.entity.screen.GuiGraphicsDrawingContext;
import li.cil.ocreloaded.minecraft.client.renderer.entity.screen.ScreenDisplayRenderer;
import li.cil.ocreloaded.minecraft.client.renderer.entity.screen.ScreenDisplayRenderer.PositionScale;
import li.cil.ocreloaded.minecraft.common.entity.ScreenBlockEntity;
import li.cil.ocreloaded.minecraft.common.menu.ScreenMenu;
import li.cil.ocreloaded.minecraft.common.util.KeyMappings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenScreen extends AbstractContainerScreen<ScreenMenu> {

    // TODO: When we support other devices, we should split out some of this to something more reusable

    private static final int TEXTURE_WIDTH = 16;
    private static final int TEXTURE_HEIGHT = 16;
    private static final int MARGIN_OUTER = 7;

    private int lastKeyCode;
    
    public ScreenScreen(ScreenMenu screenMenu, Inventory inventory, Component component) {
        super(screenMenu, inventory, component);

        // TODO: This is here because char events are not fired when control is pressed. Find a better way to handle this.
        GLFW.glfwSetInputMode(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_LOCK_KEY_MODS, GLFW.GLFW_TRUE);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        ScreenBlockEntity blockEntity = this.menu.getBlockEntity();
        TextModeBuffer textModeBuffer = blockEntity.getScreenBuffer();
        int[] resolution = textModeBuffer.viewport();
        int innerWidth = resolution[0] * ScreenDisplayRenderer.CELL_WIDTH;
        int innerHeight = resolution[1] * ScreenDisplayRenderer.CELL_HEIGHT;
        int leftStart = -innerWidth / 2 - MARGIN_OUTER;
        int topStart = -innerHeight / 2 - MARGIN_OUTER;
        int rightStart = innerWidth / 2;
        int bottomStart = innerHeight / 2;

        float scale = calculateScale(innerWidth, innerHeight);
        
        borderBlit(guiGraphics, scale, leftStart, topStart, 0, 0, MARGIN_OUTER, MARGIN_OUTER);
        borderBlit(guiGraphics, scale, rightStart, topStart, TEXTURE_WIDTH - MARGIN_OUTER, 0, MARGIN_OUTER, MARGIN_OUTER);
        borderBlit(guiGraphics, scale, leftStart, bottomStart, 0, TEXTURE_HEIGHT - MARGIN_OUTER, MARGIN_OUTER, MARGIN_OUTER);
        borderBlit(guiGraphics, scale, rightStart, bottomStart, TEXTURE_WIDTH - MARGIN_OUTER, TEXTURE_HEIGHT - MARGIN_OUTER, MARGIN_OUTER, MARGIN_OUTER);

        stretchBorderBlit(guiGraphics, scale, leftStart + MARGIN_OUTER, topStart, innerWidth, MARGIN_OUTER, MARGIN_OUTER, 0, TEXTURE_WIDTH - MARGIN_OUTER * 2, MARGIN_OUTER);
        stretchBorderBlit(guiGraphics, scale, leftStart + MARGIN_OUTER, bottomStart, innerWidth, MARGIN_OUTER, MARGIN_OUTER, TEXTURE_HEIGHT - MARGIN_OUTER, TEXTURE_WIDTH - MARGIN_OUTER * 2, MARGIN_OUTER);
        stretchBorderBlit(guiGraphics, scale, leftStart, topStart + MARGIN_OUTER, MARGIN_OUTER, innerHeight, 0, MARGIN_OUTER, MARGIN_OUTER, TEXTURE_HEIGHT - MARGIN_OUTER * 2);
        stretchBorderBlit(guiGraphics, scale, rightStart, topStart + MARGIN_OUTER, MARGIN_OUTER, innerHeight, TEXTURE_WIDTH - MARGIN_OUTER, MARGIN_OUTER, MARGIN_OUTER, TEXTURE_HEIGHT - MARGIN_OUTER * 2);

        stretchBorderBlit(guiGraphics, scale, leftStart + MARGIN_OUTER, topStart + MARGIN_OUTER, innerWidth, innerHeight, MARGIN_OUTER, MARGIN_OUTER, TEXTURE_WIDTH - MARGIN_OUTER * 2, TEXTURE_HEIGHT - MARGIN_OUTER * 2);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTicks);

        ScreenBlockEntity blockEntity = this.menu.getBlockEntity();
        TextModeBuffer textModeBuffer = blockEntity.getScreenBuffer();
        PositionScale positionScale = computePositionScale(textModeBuffer);
        ScreenDisplayRenderer.renderDisplay(new GuiGraphicsDrawingContext(guiGraphics), positionScale, textModeBuffer);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == InputConstants.KEY_ESCAPE) {
            this.onClose();
            return true;
        }

        Key key = InputConstants.getKey(keyCode, scanCode);
        if (key != null && key.getType() == InputConstants.Type.KEYSYM) {
            String keyName = GLFW.glfwGetKeyName(keyCode, scanCode);
            int translatedValue = KeyMappings.translateKeyCode(key.getValue());

            if ((modifiers & InputConstants.MOD_CONTROL) != 0 && key.getValue() == InputConstants.KEY_INSERT) {
                assert minecraft != null;
                menu.onKeyboardClipboard(minecraft.keyboardHandler.getClipboard());
                return true;
            }

            if (((keyName != null && keyName.length() == 1) || key.getValue() == InputConstants.KEY_SPACE)
                && (modifiers & InputConstants.MOD_CONTROL) == 0) {
                // TODO: Better way to handle characters
                lastKeyCode = translatedValue;
            } else if (key.getValue() == InputConstants.KEY_BACKSPACE) {
                menu.onKeyPressed(8, translatedValue);
            } else if (key.getValue() == InputConstants.KEY_TAB) {
                menu.onKeyPressed(9, translatedValue);
            } else if (key.getValue() == InputConstants.KEY_RETURN) {
                menu.onKeyPressed(10, translatedValue);
            } else {
                // TODO: Properly determine charCode when CTRL is pressed
                menu.onKeyPressed(0, translatedValue);
            }
            return true;
        }

        return true;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        Key key = InputConstants.getKey(keyCode, scanCode);
        if (key != null && key.getType() == InputConstants.Type.KEYSYM) {
            int translatedValue = KeyMappings.translateKeyCode(key.getValue());
            menu.onKeyReleased(translatedValue);
            return true;
        }

        return true;
    }

    @Override
    public boolean charTyped(char charTyped, int modifiers) {
        if (lastKeyCode != 0) {
            menu.onKeyPressed(charTyped, lastKeyCode);
        }
        lastKeyCode = 0;
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        double[] coords = computeMouseEventCoords(mouseX, mouseY);
        menu.onMousePressed(button, coords[0], coords[1]);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        double[] coords = computeMouseEventCoords(mouseX, mouseY);
        menu.onMouseReleased(button, coords[0], coords[1]);
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        double[] coords = computeMouseEventCoords(mouseX, mouseY);
        menu.onMouseDragged(button, coords[0], coords[1]);
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        double[] coords = computeMouseEventCoords(mouseX, mouseY);
        menu.onMouseScrolled((int) deltaY, coords[0], coords[1]);
        return true;
    }

    private float calculateScale(TextModeBuffer textModeBuffer) {
        int[] resolution = textModeBuffer.viewport();
        int innerWidth = resolution[0] * ScreenDisplayRenderer.CELL_WIDTH;
        int innerHeight = resolution[1] * ScreenDisplayRenderer.CELL_HEIGHT;
        return calculateScale(innerWidth, innerHeight);
    }

    private float calculateScale(int innerWidth, int innerHeight) {
        int maxWidth = this.width - MARGIN_OUTER * 2;
        int maxHeight = this.height - MARGIN_OUTER * 2;
        float scaleX = (float) maxWidth / innerWidth;
        float scaleY = (float) maxHeight / innerHeight;
        return Math.min(1, Math.min(scaleX, scaleY));
    }

    private PositionScale computePositionScale(TextModeBuffer textModeBuffer) {
        float scale = calculateScale(textModeBuffer);
        int[] resolution = textModeBuffer.viewport();
        return new ScreenDisplayRenderer.PositionScale(
            this.width / 2 - (int) (resolution[0] * ScreenDisplayRenderer.CELL_WIDTH / 2 * scale),
            this.height / 2 - (int) (resolution[1] * ScreenDisplayRenderer.CELL_HEIGHT / 2 * scale),
            scale);
    }

    private double[] computeMouseEventCoords(double mouseX, double mouseY) {
        PositionScale positionScale = computePositionScale(this.menu.getBlockEntity().getScreenBuffer());
        double posX = (mouseX - positionScale.x()) / positionScale.scale() / ScreenDisplayRenderer.CELL_WIDTH;
        double posY = (mouseY - positionScale.y()) / positionScale.scale() / ScreenDisplayRenderer.CELL_HEIGHT;
        return new double[] { posX, posY };
    }

    private void borderBlit(GuiGraphics guiGraphics, float scale, int destX, int destY, int srcX, int srcY, int width, int height) {
        stretchBorderBlit(guiGraphics, scale, destX, destY, width, height, srcX, srcY, width, height);
    }

    private void stretchBorderBlit(GuiGraphics guiGraphics, float scale, int destX, int destY, int destW, int destH, int srcX, int srcY, int srcW, int srcH) {
        int adjustedDestX = (int) Math.ceil(destX * scale + this.width / 2f);
        int adjustedDestY = (int) Math.ceil(destY * scale + this.height / 2f);
        int adjustedDestW = (int) Math.ceil(destW * scale);
        int adjustedDestH = (int) Math.ceil(destH * scale);
        guiGraphics.blit(ClientTextures.BORDERS,
            adjustedDestX, adjustedDestY, adjustedDestW, adjustedDestH,
            srcX, srcY, srcW, srcH, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

}
