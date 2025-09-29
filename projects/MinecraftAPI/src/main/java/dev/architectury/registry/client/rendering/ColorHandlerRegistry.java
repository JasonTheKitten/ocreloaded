package dev.architectury.registry.client.rendering;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ColorHandlerRegistry {

    public static void registerBlockColors(ColorRegisteryBlockEntry colorRegistryEntry, Block[] blocks) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registerBlockColors'");
    }



    public static void registerItemColors(ColorRegisteryItemEntry colorRegisteryEntry, Item[] items) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registerItemColors'");
    }

    public static interface ColorRegisteryBlockEntry {
        
        int getColor(BlockState blockState, BlockAndTintGetter blockAndTintGetter, BlockPos blockPos, int tintIndex);

    }

    public static interface ColorRegisteryItemEntry {
        
        int getColor(ItemStack itemStack, int tintIndex);

    }
    
}
