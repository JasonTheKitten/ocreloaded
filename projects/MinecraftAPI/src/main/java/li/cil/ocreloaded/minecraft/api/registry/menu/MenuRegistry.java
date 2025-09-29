package li.cil.ocreloaded.minecraft.api.registry.menu;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import li.cil.ocreloaded.minecraft.api.ServiceHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public interface MenuRegistry {

    static final MenuRegistry service = ServiceHelper.loadService(MenuRegistry.class);

    <T> void openExtendedMenuS(ServerPlayer serverPlayer, MenuProvider screenMenuProvider, Consumer<FriendlyByteBuf> byteBufConsumer);

    <T extends AbstractContainerMenu> MenuType<T> ofExtendedS(TypedMenuConstructor<T> menuConstructor);

    <T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> void registerScreenFactoryS(MenuType<T> menu, TypedScreenConstructor<T, U> screenConstructor);

    static <T> void openExtendedMenu(ServerPlayer serverPlayer, MenuProvider screenMenuProvider, Consumer<FriendlyByteBuf> byteBufConsumer) {
        service.openExtendedMenuS(serverPlayer, screenMenuProvider, byteBufConsumer);
    }

    static <T extends AbstractContainerMenu> MenuType<T> ofExtended(TypedMenuConstructor<T> menuConstructor) {
        return service.ofExtendedS(menuConstructor);
    }

    static <T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> void registerScreenFactory(MenuType<T> menu, TypedScreenConstructor<T, U> screenConstructor) {
        service.registerScreenFactoryS(menu, screenConstructor);
    }

    @FunctionalInterface
    interface TypedMenuConstructor<T extends AbstractContainerMenu> {

        @Nullable
        T createMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf);

    }

    @FunctionalInterface
    interface TypedScreenConstructor<T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> {

        U create(T menu, Inventory inventory, Component title);

    }
    
}
