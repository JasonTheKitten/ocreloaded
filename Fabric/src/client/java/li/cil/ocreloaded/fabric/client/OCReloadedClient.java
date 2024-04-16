package li.cil.ocreloaded.fabric.client;

import li.cil.ocreloaded.minecraft.client.registry.ClientRegistered;
import li.cil.ocreloaded.minecraft.client.registry.ScreenResource;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.MenuScreens.ScreenConstructor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class OCReloadedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerScreenResources();
    }
    
    private void registerScreenResources() {
        for (ScreenResource<?, ?> screenResource: ClientRegistered.ALL_SCREENS) {
            registerScreenResource(screenResource);
        }
    }

    private <T extends AbstractContainerMenu, U extends AbstractContainerScreen<T>> void registerScreenResource(ScreenResource<T, U> screenResource) {
        ScreenConstructor<T, U> constructor = (menu, inventory, title) -> screenResource.constructor().create(menu, inventory, title);
        MenuScreens.register(screenResource.menuType(), constructor);
    }

}
