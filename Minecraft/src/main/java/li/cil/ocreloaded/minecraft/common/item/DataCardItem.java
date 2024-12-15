package li.cil.ocreloaded.minecraft.common.item;

import li.cil.ocreloaded.core.component.DataCardComponent;
import li.cil.ocreloaded.core.machine.component.Component;
import net.minecraft.world.item.Item;

public class DataCardItem extends Item implements TieredItem, ComponentItem {

    private final int tier;

    public DataCardItem(Properties properties, int tier) {
        super(properties);
        this.tier = tier;
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    @Override
    public Component initComponent() {
        return new DataCardComponent();
    }
    
}
