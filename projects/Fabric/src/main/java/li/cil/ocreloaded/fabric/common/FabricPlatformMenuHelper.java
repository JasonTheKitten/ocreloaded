package li.cil.ocreloaded.fabric.common;

import java.util.function.Consumer;

import li.cil.ocreloaded.minecraft.common.util.IPlatformMenuHelper;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class FabricPlatformMenuHelper implements IPlatformMenuHelper {
    @Override
    public void openExtendedMenu(ServerPlayer serverPlayer, MenuProvider menuProvider, Consumer<FriendlyByteBuf> byteBufConsumer) {
        serverPlayer.openMenu(new ExtendedScreenHandlerFactory<FriendlyByteBuf>() {

            @Override
            public Component getDisplayName() {
                return menuProvider.getDisplayName();
            }

            @Override
            @SuppressWarnings("null") // TODO: Figure this out later
            public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
                return menuProvider.createMenu(containerId, playerInventory, player);
            }

            @Override
            public FriendlyByteBuf getScreenOpeningData(ServerPlayer player) {
                FriendlyByteBuf byteBuf = PacketByteBufs.create();
                byteBufConsumer.accept(byteBuf);
                return byteBuf;
            }

        });
    }
    
}
