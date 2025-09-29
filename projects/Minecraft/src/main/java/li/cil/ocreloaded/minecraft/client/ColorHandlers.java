package li.cil.ocreloaded.minecraft.client;

import li.cil.ocreloaded.minecraft.api.registry.client.rendering.ColorHandlerRegistry;
import li.cil.ocreloaded.minecraft.common.block.TieredBlock;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import li.cil.ocreloaded.minecraft.common.util.Colors;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class ColorHandlers {
    
    private static final int[] TIER_COLORS = new int[] {
        Colors.LIGHT_GRAY,
        Colors.YELLOW,
        Colors.CYAN,
        Colors.MAGENTA
    };

    private ColorHandlers() {}

    public static void setup() {
        registerBlockColorHandlers(
            CommonRegistered.CASE_BLOCK_TIER_1.get(),
            CommonRegistered.CASE_BLOCK_TIER_2.get(),
            CommonRegistered.CASE_BLOCK_TIER_3.get(),
            CommonRegistered.CASE_BLOCK_CREATIVE.get(),
            CommonRegistered.SCREEN_BLOCK_TIER_1.get(),
            CommonRegistered.SCREEN_BLOCK_TIER_2.get(),
            CommonRegistered.SCREEN_BLOCK_TIER_3.get()
        );

        registerItemColorHandlers(
            CommonRegistered.CASE_BLOCK_ITEM_TIER_1.get(),
            CommonRegistered.CASE_BLOCK_ITEM_TIER_2.get(),
            CommonRegistered.CASE_BLOCK_ITEM_TIER_3.get(),
            CommonRegistered.CASE_BLOCK_ITEM_CREATIVE.get(),
            CommonRegistered.SCREEN_BLOCK_ITEM_TIER_1.get(),
            CommonRegistered.SCREEN_BLOCK_ITEM_TIER_2.get(),
            CommonRegistered.SCREEN_BLOCK_ITEM_TIER_3.get()
        );
    }

    private static void registerBlockColorHandlers(Block... blocks) {
        ColorHandlerRegistry.registerBlockColors((blockState, blockAndTintGetter, blockPos, tintIndex) -> {
            if (blockState.getBlock() instanceof TieredBlock tieredBlock && tieredBlock.getTier() <= TIER_COLORS.length) {
                return TIER_COLORS[tieredBlock.getTier() - 1];
            }
            
            return Colors.WHITE;
        }, blocks);
    }

    private static void registerItemColorHandlers(Item... items) {
        ColorHandlerRegistry.registerItemColors((stack, tintIndex) -> {
            if (stack.getItem() instanceof TieredBlock tieredBlock && tieredBlock.getTier() <= TIER_COLORS.length) {
                return TIER_COLORS[tieredBlock.getTier() - 1];
            }
            if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof TieredBlock tieredBlock && tieredBlock.getTier() <= TIER_COLORS.length) {
                return TIER_COLORS[tieredBlock.getTier() - 1];
            }
            
            return Colors.WHITE;
        }, items);
    }

}
