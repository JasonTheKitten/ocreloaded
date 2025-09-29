package li.cil.ocreloaded.fabric.api.registry.menu;

import java.util.function.Consumer;

import io.netty.buffer.ByteBufUtil;
import li.cil.ocreloaded.minecraft.api.registry.menu.MenuRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType.ExtendedFactory;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.MenuScreens.ScreenConstructor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class FabricMenuRegistry implements MenuRegistry {

    @Override
    public <T> void openExtendedMenuS(ServerPlayer serverPlayer, MenuProvider menuProvider, Consumer<FriendlyByteBuf> byteBufConsumer) {
        serverPlayer.openMenu(new ExtendedScreenHandlerFactory<byte[]>() {

            @Override
            public Component getDisplayName() {
                return menuProvider.getDisplayName();
            }

            @Override
            public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
                return menuProvider.createMenu(containerId, playerInventory, player);
            }

            @Override
            public byte[] getScreenOpeningData(ServerPlayer player) {
                FriendlyByteBuf byteBuf = PacketByteBufs.create();
                byteBufConsumer.accept(byteBuf);
                byte[] bytes = ByteBufUtil.getBytes(byteBuf);
                byteBuf.release();
                return bytes;
            }
            
        });
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> ofExtendedS(TypedMenuConstructor<T> menuConstructor) {
        ExtendedFactory<T, FriendlyByteBuf> screenFactory = (a, b, c) -> menuConstructor.createMenu(a, b, c);
        return new ExtendedScreenHandlerType<>(
            screenFactory,
            StreamCodec.of((a, b) -> {}, c -> null));
    }

    @Override
    public <T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> void registerScreenFactoryS(
        MenuType<T> menu, TypedScreenConstructor<T, U> screenConstructor
    ) {
        MenuScreens.register(menu, (ScreenConstructor<T, U>) (menu2, inventory, title) -> screenConstructor.create(menu2, inventory, title));
    }
    
}
