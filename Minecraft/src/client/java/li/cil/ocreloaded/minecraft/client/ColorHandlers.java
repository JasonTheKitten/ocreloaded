package li.cil.ocreloaded.minecraft.client;

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
        registerCaseBlockColorHandler("case1", CommonRegistered.CASE_BLOCK_TIER_1);
        registerCaseBlockColorHandler("case2", CommonRegistered.CASE_BLOCK_TIER_2);
        registerCaseBlockColorHandler("case3", CommonRegistered.CASE_BLOCK_TIER_3);
        registerCaseBlockColorHandler("casecreative", CommonRegistered.CASE_BLOCK_CREATIVE);

        registerItemColorHandler("case1", CommonRegistered.CASE_BLOCK_ITEM_TIER_1);
        registerItemColorHandler("case2", CommonRegistered.CASE_BLOCK_ITEM_TIER_2);
        registerItemColorHandler("case3", CommonRegistered.CASE_BLOCK_ITEM_TIER_3);
        registerItemColorHandler("casecreative", CommonRegistered.CASE_BLOCK_ITEM_CREATIVE);
    }

    private static void registerCaseBlockColorHandler(String string, Block block) {
        ClientPlatformSpecific.get().registerBlockColorHandler(block, (blockState, blockAndTintGetter, blockPos, tintIndex) -> {
            if (blockState.getBlock() instanceof TieredBlock tieredBlock && tieredBlock.getTier() <= TIER_COLORS.length) {
                return TIER_COLORS[tieredBlock.getTier() - 1];
            }
            
            return Colors.WHITE;
        });
    }

    private static void registerItemColorHandler(String string, Item item) {
        ClientPlatformSpecific.get().registerItemColorHandler(item, (stack, tintIndex) -> {
            if (stack.getItem() instanceof TieredBlock tieredBlock && tieredBlock.getTier() <= TIER_COLORS.length) {
                return TIER_COLORS[tieredBlock.getTier() - 1];
            }
            if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof TieredBlock tieredBlock && tieredBlock.getTier() <= TIER_COLORS.length) {
                return TIER_COLORS[tieredBlock.getTier() - 1];
            }
            
            return Colors.WHITE;
        });
    }

}
