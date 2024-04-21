package li.cil.ocreloaded.fabric.common;

import java.util.Set;

import li.cil.ocreloaded.fabric.common.network.FabricNetworkInterface;
import li.cil.ocreloaded.minecraft.common.PlatformSpecificImp;
import li.cil.ocreloaded.minecraft.common.network.NetworkInterface;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class FabricPlatformSpecificImp implements PlatformSpecificImp {

    private final NetworkInterface networkInterface = new FabricNetworkInterface();
    
    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuConstructor<Integer, Inventory, T> constructor, FeatureFlagSet featureFlagSet) {
        return new MenuType<T>(constructor::get, featureFlagSet);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuTypeWithData(MenuConstructorWithData<Integer, Inventory, T> constructor) {
        return new ExtendedScreenHandlerType<>(constructor::get);
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntityConstructor<T> constructor, Block block) {
        return BlockEntityType.Builder.of(constructor::get, block).build(null);
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntityConstructor<T> constructor, Set<Block> blocks) {
        return BlockEntityType.Builder.of(constructor::get, blocks.toArray(new Block[0])).build(null);
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

    @Override
    public NetworkInterface getNetworkInterface() {
        return networkInterface;
    }

}
