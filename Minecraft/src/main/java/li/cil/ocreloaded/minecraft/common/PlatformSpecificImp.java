package li.cil.ocreloaded.minecraft.common;

import java.util.Set;

import li.cil.ocreloaded.minecraft.common.network.NetworkInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public interface PlatformSpecificImp {
    
    <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuConstructor<Integer, Inventory, T> constructor, FeatureFlagSet featureFlagSet);

    <T extends AbstractContainerMenu> MenuType<T> createMenuTypeWithData(MenuConstructorWithData<Integer, Inventory, T> constructor);

    <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntityConstructor<T> constructor, Block block);

    <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntityConstructor<T> constructor, Set<Block> blocks);

    void openMenu(Player player, NetworkMenuProvider menuProvider);

    NetworkInterface getNetworkInterface();

    public static interface MenuConstructor<T, U, V extends AbstractContainerMenu> {
        V get(T t, U u);
    }

    public static interface MenuConstructorWithData<T, U, V extends AbstractContainerMenu> {
        V get(T t, U u, FriendlyByteBuf data);
    }

    public static interface BlockEntityConstructor<T extends BlockEntity> {
        T get(BlockPos blockPos, BlockState blockState);
    }

    public static interface NetworkMenuProvider extends MenuProvider {
        void writeData(Player player, FriendlyByteBuf data);
    }

}
