package li.cil.ocreloaded.minecraft.common;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class PlatformSpecific {
    
    public static <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuConstructor<Integer, Inventory, T> constructor, FeatureFlagSet featureFlagSet) {
        throw new UnsupportedOperationException("Please implement me!");
    }

    public static interface MenuConstructor<T, U, V extends AbstractContainerMenu> {
        V get(T t, U u);
    }

}
