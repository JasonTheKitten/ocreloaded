package li.cil.ocreloaded.minecraft;

import java.util.List;

import li.cil.ocreloaded.minecraft.block.CaseBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class Entities {
    
    private static final Properties DEFAULT_BLOCK_PROPERTIES = Properties.of().strength(2f);

    public static final Named<Block> CASE_BLOCK = new Named<>(new CaseBlock(DEFAULT_BLOCK_PROPERTIES), "case");

    public static final Named<BlockItem> CASE_BLOCK_ITEM = new Named<>(new BlockItem(CASE_BLOCK.entity(), new BlockItem.Properties()), "case");

    public static final List<Named<Block>> ALL_BLOCKS = List.of(
        CASE_BLOCK
    );

    public static final List<Named<BlockItem>> ALL_BLOCK_ITEMS = List.of(
        CASE_BLOCK_ITEM
    );

    public static record Named<T>(T entity, String name) {

    }

}
