package li.cil.ocreloaded.minecraft.common.registry;

import java.util.ServiceLoader;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public interface IPlatformRegistryHelper {

    IPlatformRegistryHelper INSTANCE = ServiceLoader.load(IPlatformRegistryHelper.class).findFirst().orElseThrow();

    CreativeModeTab.Builder constructTabBuilder();

    interface BlockEntityConstructor<T extends BlockEntity> {
        T get(BlockPos blockPos, BlockState blockState);
    }

    <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntityConstructor<T> constructor, Block... blocks);

    <T extends AbstractContainerMenu> MenuType<T> registerMenuType(TypedMenuConstructor<T> menuConstructor);

    @FunctionalInterface
    interface TypedMenuConstructor<T extends AbstractContainerMenu> {
        T createMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf);
    }
    
}
