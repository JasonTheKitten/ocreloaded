package li.cil.ocreloaded.minecraft.common.item;

import net.minecraft.world.item.Item;

public class CPUItem extends Item implements TieredItem {

    private final int tier;

    public CPUItem(Properties properties, int tier) {
        super(properties);
        this.tier = tier;
    }

    @Override
    public int getTier() {
        return this.tier;
    }
    
}
