package li.cil.ocreloaded.fabric.client;

import li.cil.ocreloaded.minecraft.client.ColorHandlers;
import li.cil.ocreloaded.minecraft.client.CommonClientHooks;
import li.cil.ocreloaded.minecraft.client.registry.ClientRegistered;
import li.cil.ocreloaded.minecraft.client.renderer.entity.CaseRenderer;
import li.cil.ocreloaded.minecraft.client.renderer.entity.screen.ScreenRenderer;
import li.cil.ocreloaded.minecraft.client.screen.CaseScreen;
import li.cil.ocreloaded.minecraft.client.screen.ScreenScreen;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class OCReloadedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientRegistered.setup();
        CommonClientHooks.setup();
        registerClientStuff();
    }

    public void registerClientStuff() {
        registerColorHandlers();
        registerBERenderers();
        registerMenuScreens();
    }

    public void registerColorHandlers() {
        ColorHandlers.registerBlockColorHandlers((block, blockColor) -> ColorProviderRegistry.BLOCK.register(blockColor, block));
        ColorHandlers.registerItemColorHandlers((item, itemColor) -> ColorProviderRegistry.ITEM.register(itemColor, item));
    }

    public void registerBERenderers() {
        BlockEntityRenderers.register(CommonRegistered.CASE_BLOCK_ENTITY.get(), CaseRenderer::new);
        BlockEntityRenderers.register(CommonRegistered.SCREEN_BLOCK_ENTITY.get(), ScreenRenderer::new);
    }

    public void registerMenuScreens() {
        MenuScreens.register(CommonRegistered.CASE_MENU_TYPE.get(), CaseScreen::new);
        MenuScreens.register(CommonRegistered.SCREEN_MENU_TYPE.get(), ScreenScreen::new);
    }
}