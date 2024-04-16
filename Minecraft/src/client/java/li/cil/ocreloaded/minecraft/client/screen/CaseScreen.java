package li.cil.ocreloaded.minecraft.client.screen;

import li.cil.ocreloaded.minecraft.client.assets.ClientTextures;
import li.cil.ocreloaded.minecraft.client.screen.widget.ButtonWidget;
import li.cil.ocreloaded.minecraft.common.menu.CaseMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class CaseScreen extends AbstractContainerScreen<CaseMenu> {

    private ButtonWidget powerButton;

    public CaseScreen(CaseMenu caseMenu, Inventory inventory, Component component) {
        super(caseMenu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        this.powerButton = new ButtonWidget(this.leftPos + 70, this.topPos + 33, 18, 18, ClientTextures.POWER_BUTTON);
        this.addRenderableWidget(this.powerButton);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(ClientTextures.BACKGROUND, x, y, 0, 0, this.imageWidth, this.imageHeight);
        guiGraphics.blit(ClientTextures.COMPUTER, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

}
