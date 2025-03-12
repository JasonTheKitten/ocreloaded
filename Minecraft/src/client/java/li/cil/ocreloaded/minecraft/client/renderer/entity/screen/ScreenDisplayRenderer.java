package li.cil.ocreloaded.minecraft.client.renderer.entity.screen;

import li.cil.ocreloaded.core.graphics.TextModeBuffer;
import li.cil.ocreloaded.core.graphics.TextModeBuffer.CellInfo;
import li.cil.ocreloaded.minecraft.client.assets.ClientTextures;

public final class ScreenDisplayRenderer {
    
    public static final int CELL_WIDTH = 8; // TODO: Find actual value
    public static final int CELL_HEIGHT = 16;

    private static final int TEXTURE_WIDTH = 2040;
    private static final int TEXTURE_HEIGHT = 4128;

    private static final int NUM_FONT_COLUMNS = TEXTURE_WIDTH / CELL_WIDTH;

    private ScreenDisplayRenderer() {}

    public static void renderDisplay(DrawingContext drawingContext, PositionScale positionScale, TextModeBuffer textModeBuffer) {
        renderCharacters(drawingContext, positionScale, textModeBuffer);
    }

    private static void renderCharacters(DrawingContext drawingContext, PositionScale positionScale, TextModeBuffer textModeBuffer) {
        int[] resolution = textModeBuffer.viewport();
        int width = resolution[0];
        int height = resolution[1];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                CellInfo cellInfo = textModeBuffer.getTextCell(x, y);
                renderCharacter(drawingContext, positionScale, x, y, cellInfo);
            }
        }
        drawingContext.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private static void renderCharacter(DrawingContext drawingContext, PositionScale positionScale, int x, int y, CellInfo cellInfo) {
        int codepoint = cellInfo.codepoint();
        int column = codepoint % NUM_FONT_COLUMNS;
        int row = codepoint / NUM_FONT_COLUMNS;
        int srcX = column * CELL_WIDTH;
        int srcY = row * CELL_HEIGHT;
        int destX = x * CELL_WIDTH;
        int destY = y * CELL_HEIGHT;
        fillCell(drawingContext, positionScale, destX, destY, cellInfo.background());
        setColor(drawingContext, cellInfo.foreground());
        copyTexture(drawingContext, positionScale, destX, destY, srcX, srcY, CELL_WIDTH, CELL_HEIGHT);
        drawingContext.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private static void setColor(DrawingContext drawingContext, int color) {
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        drawingContext.setColor(r, g, b, 1.0f);
    }

    private static void fillCell(DrawingContext drawingContext, PositionScale positionScale, int x, int y, int background) {
        float scale = positionScale.scale();
        float adjustedX = (float) Math.ceil(x * scale + positionScale.x());
        float adjustedY = (float) Math.ceil(y * scale + positionScale.y());
        drawingContext.fill(
            (int) adjustedX, (int) adjustedY, 
            (int) (adjustedX + CELL_WIDTH * scale), (int) (adjustedY + CELL_HEIGHT * scale),
            background | (0xFF << 24));
    }

    private static void copyTexture(DrawingContext drawingContext, PositionScale positionScale, int destX, int destY, int srcX, int srcY, int width, int height) {
        copyTexture(drawingContext, positionScale, destX, destY, width, height, srcX, srcY, width, height);
    }

    private static void copyTexture(DrawingContext drawingContext, PositionScale positionScale, int destX, int destY, int destW, int destH, int srcX, int srcY, int srcW, int srcH) {
        float scale = positionScale.scale();
        int adjustedDestX = (int) Math.ceil(destX * scale + positionScale.x());
        int adjustedDestY = (int) Math.ceil(destY * scale + positionScale.y());
        int adjustedDestW = (int) Math.ceil(destW * scale);
        int adjustedDestH = (int) Math.ceil(destH * scale);
        drawingContext.blit(ClientTextures.FONT,
            adjustedDestX, adjustedDestY, adjustedDestW, adjustedDestH,
            srcX, srcY, srcW, srcH, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public static record PositionScale(int x, int y, float scale) {}

}
