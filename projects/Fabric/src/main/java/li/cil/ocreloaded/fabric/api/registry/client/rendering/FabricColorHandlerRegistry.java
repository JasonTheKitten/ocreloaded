package li.cil.ocreloaded.fabric.api.registry.client.rendering;

import li.cil.ocreloaded.minecraft.api.registry.client.rendering.ColorHandlerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class FabricColorHandlerRegistry implements ColorHandlerRegistry {

    @Override
    public void registerBlockColorsS(ColorRegisteryBlockEntry colorRegistryEntry, Block[] blocks) {
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> colorRegistryEntry.getColor(state, view, pos, tintIndex), blocks);
    }

    @Override
    public void registerItemColorsS(ColorRegisteryItemEntry colorRegisteryEntry, Item[] items) {
        ColorProviderRegistry.ITEM.register((itemStack, tintIndex) -> colorRegisteryEntry.getColor(itemStack, tintIndex), items);
    }
    
}
