package li.cil.ocreloaded.forge.client;

import li.cil.ocreloaded.minecraft.client.ClientPlatformSpecificImp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ForgeClientPlatformSpecificImp implements ClientPlatformSpecificImp {

    @Override
    public void registerItemColorHandler(Item item, ItemColor colorHandler) {
        Minecraft.getInstance().getItemColors().register(colorHandler, item);
    }

    @Override
    public void registerBlockColorHandler(Block block, BlockColor colorHandler) {
        Minecraft.getInstance().getBlockColors().register(colorHandler, block);
    }
    
}
