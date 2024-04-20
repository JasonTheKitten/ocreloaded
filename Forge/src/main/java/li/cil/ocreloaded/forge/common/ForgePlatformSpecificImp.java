package li.cil.ocreloaded.forge.common;

import li.cil.ocreloaded.forge.common.network.ForgeNetworkInterface;
import li.cil.ocreloaded.minecraft.common.PlatformSpecificImp;
import li.cil.ocreloaded.minecraft.common.network.NetworkInterface;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.common.extensions.IForgeServerPlayer;
public class ForgePlatformSpecificImp implements PlatformSpecificImp {

    private final NetworkInterface networkInterface = new ForgeNetworkInterface();

    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuConstructor<Integer, Inventory, T> constructor, FeatureFlagSet featureFlagSet) {
        return new MenuType<>(constructor::get, featureFlagSet);
    }

    public <T extends AbstractContainerMenu> MenuType<T> createMenuTypeWithData(MenuConstructorWithData<Integer, Inventory, T> constructor) {
        return IForgeMenuType.create(constructor::get);
    }

    @SuppressWarnings("null")
    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntityConstructor<T> constructor, Block block) {
        return BlockEntityType.Builder.of(constructor::get, block).build(null);
    }

    @Override
    public void openMenu(Player player, NetworkMenuProvider menuProvider) {
        ((IForgeServerPlayer) player).openMenu(menuProvider, buf -> menuProvider.writeData(player, buf));
    }

    @Override
    public NetworkInterface getNetworkInterface() {
        return networkInterface;
    }

}