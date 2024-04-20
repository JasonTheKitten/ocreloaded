package li.cil.ocreloaded.minecraft.client.screen;

import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;

import li.cil.ocreloaded.minecraft.client.assets.ClientTextures;
import li.cil.ocreloaded.minecraft.client.screen.widget.ButtonWidget;
import li.cil.ocreloaded.minecraft.common.assets.SharedTextures;
import li.cil.ocreloaded.minecraft.common.menu.CaseMenu;
import li.cil.ocreloaded.minecraft.common.menu.ComponentSlot;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class CaseScreen extends AbstractContainerScreen<CaseMenu> {

    private ButtonWidget powerButton;

    public CaseScreen(CaseMenu caseMenu, Inventory inventory, Component component) {
        super(caseMenu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        this.powerButton = new ButtonWidget(this.leftPos + 70, this.topPos + 33, 18, 18, ClientTextures.POWER_BUTTON, this.menu.getPower(), this.menu::sendServerPowerState);
        this.addRenderableWidget(this.powerButton);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(ClientTextures.BACKGROUND, x, y, 0, 0, this.imageWidth, this.imageHeight);
        guiGraphics.blit(ClientTextures.COMPUTER, x, y, 0, 0, this.imageWidth, this.imageHeight);

        drawSlotBackgrounds(guiGraphics, x, y);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        if (this.powerButton.isHovered() && this.menu.getPower().get() == 0) {
            guiGraphics.renderComponentTooltip(this.font, List.of(Component.translatable("gui.ocreloaded.case.power_on")), mouseX, mouseY);
        } else if (this.powerButton.isHovered() && this.menu.getPower().get() == 1) {
            guiGraphics.renderComponentTooltip(this.font, List.of(Component.translatable("gui.ocreloaded.case.power_off")), mouseX, mouseY);
        }
    }

    private void drawSlotBackgrounds(GuiGraphics guiGraphics, int x, int y) {
        for (Slot slot: this.menu.slots) {
            guiGraphics.blit(ClientTextures.SLOT, x + slot.x - 1, y + slot.y - 1, 0, 0, 18, 18, 18, 18);
            if (slot instanceof ComponentSlot && !slot.hasItem()) {
                ComponentSlot componentSlot = (ComponentSlot) slot;
                int slotX = x + slot.x, slotY = y + slot.y;
                startBackgroundDraw();
                guiGraphics.blit(componentSlot.getTexture(), slotX, slotY, 0, 0, 16, 16, 16, 16);
                drawSlotTier(guiGraphics, slotX, slotY, componentSlot.getTier());
                endBackgroundDraw();
            }
        }
    }

    private void drawSlotTier(GuiGraphics guiGraphics, int slotX, int slotY, int tier) {
        ResourceLocation texture = switch (tier) {
            case 1 -> SharedTextures.TIER1_ICON;
            case 2 -> SharedTextures.TIER2_ICON;
            case 3 -> SharedTextures.TIER3_ICON;
            default -> null;
        };

        if (texture == null) {
            return;
        }

        guiGraphics.blit(texture, slotX + 1, slotY + 1, 0, 0, 16, 16, 16, 16);
    }

    private void startBackgroundDraw() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.SRC_ALPHA, DestFactor.DST_ALPHA);
    }

    private void endBackgroundDraw() {
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

}
