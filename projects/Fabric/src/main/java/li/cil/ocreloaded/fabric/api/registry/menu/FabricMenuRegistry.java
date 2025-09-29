package li.cil.ocreloaded.fabric.api.registry.menu;

import li.cil.ocreloaded.minecraft.api.registry.menu.MenuRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType.ExtendedFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class FabricMenuRegistry implements MenuRegistry {

    @Override
    public <T> void openExtendedMenuS(ServerPlayer serverPlayer, T caseBlockEntity, Object object) {
        throw new UnsupportedOperationException("Unimplemented method 'openExtendedMenuS'");
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> ofExtendedS(TypedMenuConstructor<T> menuConstructor) {
        ExtendedFactory<T, FriendlyByteBuf> screenFactory = (a, b, c) -> menuConstructor.createMenu(a, b, c);
        return new ExtendedScreenHandlerType<>(
            screenFactory,
            StreamCodec.of((a, b) -> {}, c -> null));
    }
    
}
