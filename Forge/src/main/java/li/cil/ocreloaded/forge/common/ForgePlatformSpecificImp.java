package li.cil.ocreloaded.forge.common;

import li.cil.ocreloaded.minecraft.common.PlatformSpecificImp;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.common.extensions.IForgeServerPlayer;
public class ForgePlatformSpecificImp implements PlatformSpecificImp {

    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuConstructor<Integer, Inventory, T> constructor, FeatureFlagSet featureFlagSet) {
        return new MenuType<>(constructor::get, featureFlagSet);
    }

    public <T extends AbstractContainerMenu> MenuType<T> createMenuTypeWithData(MenuConstructorWithData<Integer, Inventory, T> constructor) {
        return IForgeMenuType.create(constructor::get);
    }

    @Override
    public void openMenu(Player player, NetworkMenuProvider menuProvider) {
        ((IForgeServerPlayer) player).openMenu(menuProvider, buf -> menuProvider.writeData(player, buf));
    }

}