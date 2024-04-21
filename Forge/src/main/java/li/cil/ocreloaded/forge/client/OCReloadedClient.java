package li.cil.ocreloaded.forge.client;

import li.cil.ocreloaded.minecraft.client.CommonClientHooks;
import li.cil.ocreloaded.minecraft.client.registry.BlockEntityRendererEntry;
import li.cil.ocreloaded.minecraft.client.registry.ClientRegistered;
import li.cil.ocreloaded.minecraft.client.registry.ScreenResource;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.MenuScreens.ScreenConstructor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class OCReloadedClient {

    public OCReloadedClient() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        registerScreenResources(event);
        CommonClientHooks.setup();
    }

    @SubscribeEvent
    public void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        for (BlockEntityRendererEntry<?> blockEntityRendererEntry : ClientRegistered.ALL_BLOCK_ENTITY_RENDERERS) {
            registerBlockEntityRenderer(event, blockEntityRendererEntry);
        }
    }

    private void registerScreenResources(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            for (ScreenResource<?, ?> screenResource: ClientRegistered.ALL_SCREENS) {
                registerScreenResource(screenResource);
            }
        });
    }

    private <T extends AbstractContainerMenu, U extends AbstractContainerScreen<T>> void registerScreenResource(ScreenResource<T, U> screenResource) {
        ScreenConstructor<T, U> constructor = (menu, inventory, title) -> screenResource.constructor().create(menu, inventory, title);
        MenuScreens.register(screenResource.menuType(), constructor);
    }

    private <T extends BlockEntity> void registerBlockEntityRenderer(EntityRenderersEvent.RegisterRenderers event, BlockEntityRendererEntry<T> blockEntityRendererEntry) {
        event.registerBlockEntityRenderer(blockEntityRendererEntry.blockEntityType(), blockEntityRendererEntry.renderer()::apply);
    }

}
