package li.cil.ocreloaded.minecraft.client;

import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
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
        registerBlockColorHandler("case1", CommonRegistered.CASE_BLOCK_TIER_1.get());
        registerBlockColorHandler("case2", CommonRegistered.CASE_BLOCK_TIER_2.get());
        registerBlockColorHandler("case3", CommonRegistered.CASE_BLOCK_TIER_3.get());
        registerBlockColorHandler("casecreative", CommonRegistered.CASE_BLOCK_CREATIVE.get());

        registerItemColorHandler("case1", CommonRegistered.CASE_BLOCK_ITEM_TIER_1.get());
        registerItemColorHandler("case2", CommonRegistered.CASE_BLOCK_ITEM_TIER_2.get());
        registerItemColorHandler("case3", CommonRegistered.CASE_BLOCK_ITEM_TIER_3.get());
        registerItemColorHandler("casecreative", CommonRegistered.CASE_BLOCK_ITEM_CREATIVE.get());

        registerBlockColorHandler("screen1", CommonRegistered.SCREEN_BLOCK_TIER_1.get());
        registerBlockColorHandler("screen2", CommonRegistered.SCREEN_BLOCK_TIER_2.get());
        registerBlockColorHandler("screen3", CommonRegistered.SCREEN_BLOCK_TIER_3.get());

        registerItemColorHandler("screen1", CommonRegistered.SCREEN_BLOCK_ITEM_TIER_1.get());
        registerItemColorHandler("screen2", CommonRegistered.SCREEN_BLOCK_ITEM_TIER_2.get());
        registerItemColorHandler("screen3", CommonRegistered.SCREEN_BLOCK_ITEM_TIER_3.get());
    }

    private static void registerBlockColorHandler(String string, Block block) {
        ColorHandlerRegistry.registerBlockColors((blockState, blockAndTintGetter, blockPos, tintIndex) -> {
            if (blockState.getBlock() instanceof TieredBlock tieredBlock && tieredBlock.getTier() <= TIER_COLORS.length) {
                return TIER_COLORS[tieredBlock.getTier() - 1];
            }
            
            return Colors.WHITE;
        }, block);
    }

    private static void registerItemColorHandler(String string, Item item) {
        ColorHandlerRegistry.registerItemColors((stack, tintIndex) -> {
            if (stack.getItem() instanceof TieredBlock tieredBlock && tieredBlock.getTier() <= TIER_COLORS.length) {
                return TIER_COLORS[tieredBlock.getTier() - 1];
            }
            if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof TieredBlock tieredBlock && tieredBlock.getTier() <= TIER_COLORS.length) {
                return TIER_COLORS[tieredBlock.getTier() - 1];
            }
            
            return Colors.WHITE;
        }, item);
    }

}
