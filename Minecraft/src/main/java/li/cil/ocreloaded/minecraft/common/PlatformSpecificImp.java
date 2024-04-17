package li.cil.ocreloaded.minecraft.common;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public interface PlatformSpecificImp {
    
    <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuConstructor<Integer, Inventory, T> constructor, FeatureFlagSet featureFlagSet);

    <T extends AbstractContainerMenu> MenuType<T> createMenuTypeWithData(MenuConstructorWithData<Integer, Inventory, T> constructor);

    void openMenu(Player player, NetworkMenuProvider menuProvider);

    public static interface MenuConstructor<T, U, V extends AbstractContainerMenu> {
        V get(T t, U u);
    }

    public static interface MenuConstructorWithData<T, U, V extends AbstractContainerMenu> {
        V get(T t, U u, FriendlyByteBuf data);
    }

    public static interface NetworkMenuProvider extends MenuProvider {
        
        void writeData(Player player, FriendlyByteBuf data);

    }

}
