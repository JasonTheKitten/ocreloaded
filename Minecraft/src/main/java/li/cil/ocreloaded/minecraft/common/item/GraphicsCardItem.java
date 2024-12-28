package li.cil.ocreloaded.minecraft.common.item;

import java.util.Optional;

import li.cil.ocreloaded.core.component.GraphicsCardComponent;
import li.cil.ocreloaded.core.network.NetworkNode;
import li.cil.ocreloaded.core.network.NetworkNode.Visibility;
import li.cil.ocreloaded.minecraft.common.component.ComponentNetworkNode;
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
    public NetworkNode newNetworkNode() {
        return new ComponentNetworkNode(Optional.of(new GraphicsCardComponent(new int[] { 50, 16 })), Visibility.NEIGHBORS);
    }
    
}
