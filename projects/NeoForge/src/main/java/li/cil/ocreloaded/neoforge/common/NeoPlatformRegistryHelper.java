package li.cil.ocreloaded.neoforge.common;

import li.cil.ocreloaded.minecraft.common.registry.IPlatformRegistryHelper;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

public class NeoPlatformRegistryHelper implements IPlatformRegistryHelper {
    @Override
    public CreativeModeTab.Builder constructTabBuilder() {
        return CreativeModeTab.builder();
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntityConstructor<T> constructor, Block... blocks) {
        // Quote: "Build using null; vanilla does some datafixer shenanigans with the parameter that we don't need."
        //noinspection DataFlowIssue
        return BlockEntityType.Builder.of(constructor::get, blocks).build(null);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> registerMenuType(TypedMenuConstructor<T> menuConstructor) {
        return IMenuTypeExtension.create(menuConstructor::createMenu);
    }
}
