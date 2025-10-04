package li.cil.ocreloaded.minecraft.common.item;

import li.cil.ocreloaded.core.component.GraphicsCardComponent;
import li.cil.ocreloaded.core.network.NetworkNode;
import li.cil.ocreloaded.core.network.NetworkNode.Visibility;
import li.cil.ocreloaded.minecraft.common.component.ComponentNetworkNode;
import net.minecraft.world.item.Item;

public class APUItem extends Item implements ProcessorProviderItem, ComponentItem {

    //TODO: Move this to a conifg
    public static int[][] TIER_RESOLUTIONS = new int[][] {
        new int[] { 50, 16 },
        new int[] { 80, 25 },
        new int[] { 160, 50 },
    };

    public static int[] TIER_DEPTHS = new int[] { 1, 4, 8 };

    private final int tier;

    public APUItem(Properties properties, int tier) {
        super(properties);
        this.tier = tier;
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    @Override
    public NetworkNode newNetworkNode() {
        return new ComponentNetworkNode(
            node -> new GraphicsCardComponent(node, TIER_RESOLUTIONS[tier - 1], TIER_DEPTHS[tier - 1]),
            Visibility.NEIGHBORS);
    }
    
}
