package li.cil.ocreloaded.minecraft;

import java.util.List;

import li.cil.ocreloaded.minecraft.block.CaseBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class OCReloadedCommon {

    public static final String MOD_ID = "ocreloaded";

    private static final Properties DEFAULT_BLOCK_PROPERTIES = Properties.of().strength(2f);

    private List <BlockEntry> BLOCKS;
    
    public List<BlockEntry> getBlocks() {
        if (BLOCKS != null) return BLOCKS;
        BLOCKS = List.of(
            BlockEntry.itemBlock("case", new CaseBlock(DEFAULT_BLOCK_PROPERTIES))
        );
        return BLOCKS;
    }

    public static record BlockEntry(String name, Block block, boolean hasItem, boolean hasCreativeTab) {
        public static BlockEntry itemBlock(String name, Block block) {
            return new BlockEntry(name, block, true, true);
        }
    }

}
