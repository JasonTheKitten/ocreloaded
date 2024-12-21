package li.cil.ocreloaded.minecraft.common.item;

import java.util.Optional;

import li.cil.ocreloaded.core.component.DataCardComponent;
import li.cil.ocreloaded.core.network.NetworkNode;
import li.cil.ocreloaded.core.network.NetworkNode.Visibility;
import li.cil.ocreloaded.minecraft.common.component.ComponentNetworkNode;
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
    public NetworkNode newNetworkNode() {
        return new ComponentNetworkNode(Optional.of(new DataCardComponent()), Visibility.NEIGHBORS);
    }
    
}
