package li.cil.ocreloaded.minecraft.common.block;

import net.minecraft.world.level.block.Block;

public class ScreenBlock extends Block implements TieredBlock {

    private final int tier;

    public ScreenBlock(Properties properties, int tier) {
        super(properties);
        this.tier = tier;
    }

    @Override
    public int getTier() {
        return this.tier;
    }
    
}
