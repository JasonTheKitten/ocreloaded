package li.cil.ocreloaded.minecraft.client;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface ClientPlatformSpecificImp {

    void registerItemColorHandler(Item item, ItemColor colorHandler);

    void registerBlockColorHandler(Block block, BlockColor colorHandler);
    
}
