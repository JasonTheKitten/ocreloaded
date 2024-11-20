package li.cil.ocreloaded.minecraft.common.item;

import li.cil.ocreloaded.core.component.GraphicsCardComponent;
import li.cil.ocreloaded.core.machine.component.Component;
import net.minecraft.world.item.Item;

public class GraphicsCardItem extends Item implements TieredItem, ComponentItem {

    private final int tier;

    public GraphicsCardItem(Properties properties, int tier) {
        super(properties);
        this.tier = tier;
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    @Override
    public Component initComponent() {
        return new GraphicsCardComponent();
    }
    
}
