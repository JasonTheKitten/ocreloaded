package li.cil.ocreloaded.minecraft.client;

import li.cil.ocreloaded.minecraft.common.block.TieredBlock;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import li.cil.ocreloaded.minecraft.common.util.Colors;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;

public final class ColorHandlers {
    private static final int[] TIER_COLORS = new int[]{
            Colors.LIGHT_GRAY,
            Colors.YELLOW,
            Colors.CYAN,
            Colors.MAGENTA
    };

    private static final Item[] ITEMS = new Item[]{
            CommonRegistered.CASE_BLOCK_ITEM_TIER_1.get(),
            CommonRegistered.CASE_BLOCK_ITEM_TIER_2.get(),
            CommonRegistered.CASE_BLOCK_ITEM_TIER_3.get(),
            CommonRegistered.CASE_BLOCK_ITEM_CREATIVE.get(),
            CommonRegistered.SCREEN_BLOCK_ITEM_TIER_1.get(),
            CommonRegistered.SCREEN_BLOCK_ITEM_TIER_2.get(),
            CommonRegistered.SCREEN_BLOCK_ITEM_TIER_3.get()
    };

    private static final Block[] BLOCKS = new Block[]{
            CommonRegistered.CASE_BLOCK_TIER_1.get(),
            CommonRegistered.CASE_BLOCK_TIER_2.get(),
            CommonRegistered.CASE_BLOCK_TIER_3.get(),
            CommonRegistered.CASE_BLOCK_CREATIVE.get(),
            CommonRegistered.SCREEN_BLOCK_TIER_1.get(),
            CommonRegistered.SCREEN_BLOCK_TIER_2.get(),
            CommonRegistered.SCREEN_BLOCK_TIER_3.get()
    };

    private ColorHandlers() {
    }

    public static void registerBlockColorHandlers(BiConsumer<Block, BlockColor> registerFunc) {
        for (Block block : BLOCKS) {
            registerFunc.accept(block, (blockState, blockAndTintGetter, blockPos, i) -> {
                if (blockState.getBlock() instanceof TieredBlock tieredBlock && tieredBlock.getTier() <= TIER_COLORS.length) {
                    return TIER_COLORS[tieredBlock.getTier() - 1];
                }

                return Colors.WHITE;
            });
        }
    }

    public static void registerItemColorHandlers(BiConsumer<Item, ItemColor> registerFunc) {
        for (Item item : ITEMS) {
            registerFunc.accept(item, (itemStack, tintIndex) -> {
                if (itemStack.getItem() instanceof TieredBlock tieredBlock && tieredBlock.getTier() <= TIER_COLORS.length) {
                    return TIER_COLORS[tieredBlock.getTier() - 1];
                }

                if (itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof TieredBlock tieredBlock && tieredBlock.getTier() <= TIER_COLORS.length) {
                    return TIER_COLORS[tieredBlock.getTier() - 1];
                }

                return Colors.WHITE;
            });
        }
    }
}
