package li.cil.ocreloaded.minecraft.common.component;

import li.cil.ocreloaded.core.network.NetworkNode;
import li.cil.ocreloaded.minecraft.common.entity.ComponentTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ComponentNetworkUtil {
    
    private ComponentNetworkUtil() {}

    public static void connectToNeighbors(Level level, BlockPos pos) {
        if (level == null) return;
        for (Direction facing : Direction.values()) {
            BlockPos neighborPos = pos.relative(facing);
            if (!level.isLoaded(neighborPos)) continue;
            BlockEntity neighborEntity = level.getBlockEntity(neighborPos);
            if (!(neighborEntity instanceof ComponentTileEntity neighborComponent)) continue;
            NetworkNode neighborNode = neighborComponent.networkNode();
            NetworkNode thisNode = ((ComponentTileEntity) level.getBlockEntity(pos)).networkNode();
            thisNode.connect(neighborNode);
        }
    }

}
