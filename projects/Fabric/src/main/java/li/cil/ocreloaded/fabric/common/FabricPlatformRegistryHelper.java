package li.cil.ocreloaded.fabric.common;

import io.netty.buffer.Unpooled;
import li.cil.ocreloaded.minecraft.common.registry.IPlatformRegistryHelper;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class FabricPlatformRegistryHelper implements IPlatformRegistryHelper {
    @Override
    public CreativeModeTab.Builder constructTabBuilder() {
        return FabricItemGroup.builder();
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntityConstructor<T> constructor, Block... blocks) {
        return BlockEntityType.Builder.of(constructor::get, blocks).build();
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> registerMenuType(TypedMenuConstructor<T> menuConstructor) {
        ExtendedScreenHandlerType.ExtendedFactory<T, FriendlyByteBuf> screenFactory = menuConstructor::createMenu;

        return new ExtendedScreenHandlerType<>(screenFactory, StreamCodec.of((dest, src) -> {
            dest.writeBytes(src);
        }, s -> {
            FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
            byteBuf.writeBytes(s);
            return byteBuf;
        }));
    }
}
