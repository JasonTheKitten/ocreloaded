package li.cil.ocreloaded.minecraft.api.registry.client.rendering;

import li.cil.ocreloaded.minecraft.api.ServiceHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface ColorHandlerRegistry {

    static final ColorHandlerRegistry service = ServiceHelper.loadService(ColorHandlerRegistry.class);

    void registerBlockColorsS(ColorRegisteryBlockEntry colorRegistryEntry, Block[] blocks);

    void registerItemColorsS(ColorRegisteryItemEntry colorRegisteryEntry, Item[] items);

    static void registerBlockColors(ColorRegisteryBlockEntry colorRegistryEntry, Block[] blocks) {
        service.registerBlockColorsS(colorRegistryEntry, blocks);
    }

    static void registerItemColors(ColorRegisteryItemEntry colorRegisteryEntry, Item[] items) {
        service.registerItemColorsS(colorRegisteryEntry, items);
    }

    static interface ColorRegisteryBlockEntry {
        
        int getColor(BlockState blockState, BlockAndTintGetter blockAndTintGetter, BlockPos blockPos, int tintIndex);

    }

    static interface ColorRegisteryItemEntry {
        
        int getColor(ItemStack itemStack, int tintIndex);

    }
    
}
