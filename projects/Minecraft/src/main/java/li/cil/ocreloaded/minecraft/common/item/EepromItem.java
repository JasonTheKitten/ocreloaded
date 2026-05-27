package li.cil.ocreloaded.minecraft.common.item;

import java.util.UUID;

import li.cil.ocreloaded.core.component.EepromComponent;
import li.cil.ocreloaded.core.network.NetworkNode;
import li.cil.ocreloaded.core.network.NetworkNode.Visibility;
import li.cil.ocreloaded.core.network.NetworkNodes;
import net.minecraft.world.item.Item;

public class EepromItem extends Item implements ComponentItem {
    public EepromItem(Properties properties) {
        super(properties);
    }

    @Override
    public NetworkNode newNetworkNode(UUID id) {
        return NetworkNodes.component(id, node -> new EepromComponent(node, ""), Visibility.NEIGHBORS);
    }
}
