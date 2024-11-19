package li.cil.ocreloaded.minecraft.common.item;

import li.cil.ocreloaded.core.machine.architecture.component.Component;
import li.cil.ocreloaded.core.machine.architecture.component.EepromComponent;
import net.minecraft.world.item.Item;

public class EepromItem extends Item implements ComponentItem {

    public EepromItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component initComponent() {
        return new EepromComponent();
    }
    
}
