package li.cil.ocreloaded.minecraft.common.item;

import java.util.UUID;

import li.cil.ocreloaded.core.network.NetworkNode;

public interface ComponentItem {
    
    NetworkNode newNetworkNode(UUID id);

}
