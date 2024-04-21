package li.cil.ocreloaded.minecraft.client;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class FabricClientPlatformSpecificImp implements ClientPlatformSpecificImp {

    @Override
    public void registerItemColorHandler(Item item, ItemColor colorHandler) {
        ColorProviderRegistry.ITEM.register(colorHandler, item);
    }

    @Override
    public void registerBlockColorHandler(Block block, BlockColor colorHandler) {
        ColorProviderRegistry.BLOCK.register(colorHandler, block);
    }

}
