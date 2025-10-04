package li.cil.ocreloaded.minecraft.common.item;

import li.cil.ocreloaded.core.component.EepromComponent;
import li.cil.ocreloaded.core.network.NetworkNode;
import li.cil.ocreloaded.core.network.NetworkNode.Visibility;
import li.cil.ocreloaded.minecraft.common.component.ComponentNetworkNode;
import net.minecraft.world.item.Item;

public class EepromItem extends Item implements SlotItem, ComponentItem {

    public EepromItem(Properties properties) {
        super(properties);
    }

    @Override
    public NetworkNode newNetworkNode() {
        return new ComponentNetworkNode(node -> new EepromComponent(node, ""), Visibility.NEIGHBORS);
    }
    
}
