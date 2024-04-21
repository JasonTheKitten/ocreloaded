package li.cil.ocreloaded.fabric.client;


import li.cil.ocreloaded.fabric.client.fabric.FabricClientNetworkInterface;
import li.cil.ocreloaded.fabric.common.network.FabricNetworkInterface;
import li.cil.ocreloaded.minecraft.client.CommonClientHooks;
import li.cil.ocreloaded.minecraft.client.registry.BlockEntityRendererEntry;
import li.cil.ocreloaded.minecraft.client.registry.ClientRegistered;
import li.cil.ocreloaded.minecraft.client.registry.ScreenResource;
import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import li.cil.ocreloaded.minecraft.common.PlatformSpecific;
import li.cil.ocreloaded.minecraft.common.network.NetworkHandler;
import li.cil.ocreloaded.minecraft.common.network.NetworkInterface;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.MenuScreens.ScreenConstructor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;

public class OCReloadedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        NetworkInterface networkInterface = new FabricClientNetworkInterface();
        ((FabricNetworkInterface) PlatformSpecific.get().getNetworkInterface()).setClientInterface(networkInterface);

        registerNetworkHandlers(networkInterface);
        registerScreenResources();
        registerEntityRenderers();

        CommonClientHooks.setup();
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

    private void registerNetworkHandlers(NetworkInterface networkInterface) {
        for (NetworkHandler<?> handler : OCReloadedCommon.NETWORK_HANDLERS) {
            networkInterface.registerNetworkHandler(handler);
        }
    }

    private void registerEntityRenderers() {
        for (BlockEntityRendererEntry<?> entry : ClientRegistered.ALL_BLOCK_ENTITY_RENDERERS) {
            registerEntityRenderer(entry);
        }
    }

    private <T extends BlockEntity> void registerEntityRenderer(BlockEntityRendererEntry<T> entry) {
        BlockEntityRenderers.register(entry.blockEntityType(), entry.renderer()::apply);
    }

}
