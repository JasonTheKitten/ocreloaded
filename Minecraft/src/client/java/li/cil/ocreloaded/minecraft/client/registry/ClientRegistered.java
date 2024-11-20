package li.cil.ocreloaded.minecraft.client.registry;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import li.cil.ocreloaded.minecraft.client.renderer.entity.CaseRenderer;
import li.cil.ocreloaded.minecraft.client.renderer.entity.ScreenRenderer;
import li.cil.ocreloaded.minecraft.client.screen.CaseScreen;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;

public class ClientRegistered {

    public static void setup() {
        CommonRegistered.CASE_MENU_TYPE.listen(menu -> MenuRegistry.registerScreenFactory(menu, CaseScreen::new));
        CommonRegistered.CASE_BLOCK_ENTITY.listen(blockEntityType -> BlockEntityRendererRegistry.register(blockEntityType, CaseRenderer::new));
        CommonRegistered.SCREEN_BLOCK_ENTITY.listen(blockEntityType -> BlockEntityRendererRegistry.register(blockEntityType, ScreenRenderer::new));
    }

}
