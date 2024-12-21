package li.cil.ocreloaded.minecraft.common.item;

import java.util.Optional;

import li.cil.ocreloaded.core.component.EepromComponent;
import li.cil.ocreloaded.core.network.NetworkNode;
import li.cil.ocreloaded.core.network.NetworkNode.Visibility;
import li.cil.ocreloaded.minecraft.common.component.ComponentNetworkNode;
import net.minecraft.world.item.Item;

public class EepromItem extends Item implements ComponentItem {

    public EepromItem(Properties properties) {
        super(properties);
    }

    @Override
    public NetworkNode newNetworkNode() {
        return new ComponentNetworkNode(Optional.of(new EepromComponent("")), Visibility.NEIGHBORS);
    }
    
}
