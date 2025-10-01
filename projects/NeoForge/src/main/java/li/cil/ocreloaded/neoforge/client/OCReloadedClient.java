package li.cil.ocreloaded.neoforge.client;

import li.cil.ocreloaded.minecraft.client.ColorHandlers;
import li.cil.ocreloaded.minecraft.client.CommonClientHooks;
import li.cil.ocreloaded.minecraft.client.registry.ClientRegistered;
import li.cil.ocreloaded.minecraft.client.renderer.entity.CaseRenderer;
import li.cil.ocreloaded.minecraft.client.renderer.entity.screen.ScreenRenderer;
import li.cil.ocreloaded.minecraft.client.screen.CaseScreen;
import li.cil.ocreloaded.minecraft.client.screen.ScreenScreen;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import li.cil.ocreloaded.neoforge.common.OCReloaded;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod(value = OCReloaded.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = OCReloaded.MOD_ID, value = Dist.CLIENT)
public class OCReloadedClient {
    public OCReloadedClient() {

    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ClientRegistered.setup();
        CommonClientHooks.setup();
    }

    @SubscribeEvent
    public static void registerItems(RegisterColorHandlersEvent.Item event) {
        ColorHandlers.registerItemColorHandlers((item, itemColor) -> event.register(itemColor, item));
    }

    @SubscribeEvent
    public static void registerItems(RegisterColorHandlersEvent.Block event) {
        ColorHandlers.registerBlockColorHandlers((block, blockColor) -> event.register(blockColor, block));
    }

    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CommonRegistered.CASE_BLOCK_ENTITY.get(), CaseRenderer::new);
        event.registerBlockEntityRenderer(CommonRegistered.SCREEN_BLOCK_ENTITY.get(), ScreenRenderer::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(CommonRegistered.CASE_MENU_TYPE.get(), CaseScreen::new);
        event.register(CommonRegistered.SCREEN_MENU_TYPE.get(), ScreenScreen::new);
    }
}
