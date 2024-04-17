package li.cil.ocreloaded.fabric.common;

import li.cil.ocreloaded.minecraft.common.PlatformSpecificImp;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class FabricPlatformSpecificImp implements PlatformSpecificImp {
    
    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuConstructor<Integer, Inventory, T> constructor, FeatureFlagSet featureFlagSet) {
        return new MenuType<T>(constructor::get, featureFlagSet);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuTypeWithData(MenuConstructorWithData<Integer, Inventory, T> constructor) {
        return new ExtendedScreenHandlerType<>(constructor::get);
    }

    @Override
    public void openMenu(Player player, NetworkMenuProvider menuProvider) {
        player.openMenu(new ExtendedScreenHandlerFactory() {

            @Override
            public Component getDisplayName() {
                return menuProvider.getDisplayName();
            }

            @Override
            public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
                return menuProvider.createMenu(windowId, playerInventory, player);
            }

            @Override
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                menuProvider.writeData(player, buf);
            }
            
        });
    }

}
