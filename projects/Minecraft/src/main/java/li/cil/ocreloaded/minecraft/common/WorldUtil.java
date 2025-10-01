package li.cil.ocreloaded.minecraft.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public final class WorldUtil {
    
    private WorldUtil() {}

    public static boolean isPlayerCloseEnough(Player player, BlockPos position) {
        return player.distanceToSqr(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5) <= WorldConstants.MAX_PLAYER_RADIUS;
    }

}
