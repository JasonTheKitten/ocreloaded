package li.cil.ocreloaded.minecraft.client.registry;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public record ScreenResource<T extends AbstractContainerMenu, U extends AbstractContainerScreen<T>>(ScreenConstructor<T, U> constructor, MenuType<T> menuType, String name) {

    public static interface ScreenConstructor<T, U> {
        U create(T t, Inventory inventory, Component component);
    }

}
