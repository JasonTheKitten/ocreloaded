package li.cil.ocreloaded.minecraft.api.registry.menu;

import javax.annotation.Nullable;

import li.cil.ocreloaded.minecraft.api.ServiceHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public interface MenuRegistry {

    static final MenuRegistry service = ServiceHelper.loadService(MenuRegistry.class);

    <T> void openExtendedMenuS(ServerPlayer serverPlayer, T caseBlockEntity, Object object);

    <T extends AbstractContainerMenu> MenuType<T> ofExtendedS(TypedMenuConstructor<T> menuConstructor);

    public static <T> void openExtendedMenu(ServerPlayer serverPlayer, T caseBlockEntity, Object object) {
        service.openExtendedMenuS(serverPlayer, caseBlockEntity, object);
    }

    public static <T extends AbstractContainerMenu> MenuType<T> ofExtended(TypedMenuConstructor<T> menuConstructor) {
        return service.ofExtendedS(menuConstructor);
    }

    @FunctionalInterface
    public interface TypedMenuConstructor<T extends AbstractContainerMenu> {

        @Nullable
        T createMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf);

    }
    
}
