package li.cil.ocreloaded.minecraft.client.renderer.entity;

import li.cil.ocreloaded.core.graphics.TextModeBuffer;
import li.cil.ocreloaded.core.graphics.TextModeBuffer.CellInfo;
import li.cil.ocreloaded.minecraft.client.assets.ClientTextures;
import net.minecraft.client.gui.GuiGraphics;

public final class ScreenDisplayRenderer {
    
    public static final int CELL_WIDTH = 8; // TODO: Find actual value
    public static final int CELL_HEIGHT = 16;

    private static final int TEXTURE_WIDTH = 2040;
    private static final int TEXTURE_HEIGHT = 4128;

    private static final int NUM_FONT_COLUMNS = TEXTURE_WIDTH / CELL_WIDTH;

    private ScreenDisplayRenderer() {}

    public static void renderDisplay(GuiGraphics guiGraphics, PositionScale positionScale, TextModeBuffer textModeBuffer) {
        renderCharacters(guiGraphics, positionScale, textModeBuffer);
    }

    private static void renderCharacters(GuiGraphics guiGraphics, PositionScale positionScale, TextModeBuffer textModeBuffer) {
        int width = textModeBuffer.getWidth();
        int height = textModeBuffer.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                CellInfo cellInfo = textModeBuffer.getTextCell(x, y);
                renderCharacter(guiGraphics, positionScale, x, y, cellInfo);
            }
        }
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private static void renderCharacter(GuiGraphics guiGraphics, PositionScale positionScale, int x, int y, CellInfo cellInfo) {
        int codepoint = cellInfo.codepoint();
        int column = codepoint % NUM_FONT_COLUMNS;
        int row = codepoint / NUM_FONT_COLUMNS;
        int srcX = column * CELL_WIDTH;
        int srcY = row * CELL_HEIGHT;
        int destX = x * CELL_WIDTH;
        int destY = y * CELL_HEIGHT;
        fillCell(guiGraphics, positionScale, destX, destY, cellInfo.background());
        setColor(guiGraphics, cellInfo.foreground());
        copyTexture(guiGraphics, positionScale, destX, destY, srcX, srcY, CELL_WIDTH, CELL_HEIGHT);
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private static void setColor(GuiGraphics guiGraphics, int color) {
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        guiGraphics.setColor(r, g, b, 1.0f);
    }

    private static void fillCell(GuiGraphics guiGraphics, PositionScale positionScale, int x, int y, int background) {
        int adjustedX = (int) Math.ceil(x * positionScale.scale() + positionScale.x());
        int adjustedY = (int) Math.ceil(y * positionScale.scale() + positionScale.y());
        guiGraphics.fill(adjustedX, adjustedY, adjustedX + CELL_WIDTH, adjustedY + CELL_HEIGHT, background | (0xFF << 24));
    }

    private static void copyTexture(GuiGraphics guiGraphics, PositionScale positionScale, int destX, int destY, int srcX, int srcY, int width, int height) {
        copyTexture(guiGraphics, positionScale, destX, destY, width, height, srcX, srcY, width, height);
    }

    private static void copyTexture(GuiGraphics guiGraphics, PositionScale positionScale, int destX, int destY, int destW, int destH, int srcX, int srcY, int srcW, int srcH) {
        float scale = positionScale.scale();
        int adjustedDestX = (int) Math.ceil(destX * scale + positionScale.x());
        int adjustedDestY = (int) Math.ceil(destY * scale + positionScale.y());
        int adjustedDestW = (int) Math.ceil(destW * scale);
        int adjustedDestH = (int) Math.ceil(destH * scale);
        guiGraphics.blit(ClientTextures.FONT,
            adjustedDestX, adjustedDestY, adjustedDestW, adjustedDestH,
            srcX, srcY, srcW, srcH, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public static record PositionScale(int x, int y, float scale) {}

}
