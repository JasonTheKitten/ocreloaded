package li.cil.ocreloaded.minecraft.common.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.AttachFace;

public class RotationHelper {
    public static Direction toLocal(Direction direction, Direction facing, AttachFace attachFace) {
        if (attachFace == AttachFace.WALL) {
            return WALL_TRANSLATIONS[facing.get2DDataValue()][direction.get3DDataValue()];
        } else if (attachFace == AttachFace.FLOOR) {
            return FLOOR_TRANSLATIONS[facing.get2DDataValue()][direction.get3DDataValue()];
        } else {
            return CEILING_TRANSLATIONS[facing.get2DDataValue()][direction.get3DDataValue()];
        }
    }

    public static Direction toGlobal(Direction direction, Direction facing, AttachFace attachFace) {
        Direction[] translations;
        if (attachFace == AttachFace.WALL) {
            translations = WALL_TRANSLATIONS[facing.get2DDataValue()];
        } else if (attachFace == AttachFace.FLOOR) {
            translations = FLOOR_TRANSLATIONS[facing.get2DDataValue()];
        } else {
            translations = CEILING_TRANSLATIONS[facing.get2DDataValue()];
        }
        for (int i = 0; i < translations.length; i++) {
            if (translations[i] == direction) {
                return Direction.from3DDataValue(i);
            }
        }
        throw new IllegalArgumentException("Invalid direction " + direction + " for facing " + facing + " and attach face " + attachFace);
    }

    // Translations of directions from global to local space for wall, ceiling and floor.
    private static final Direction[][] WALL_TRANSLATIONS = {
        // South
        {Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST},
        // West
        {Direction.DOWN, Direction.UP, Direction.WEST, Direction.EAST, Direction.SOUTH, Direction.NORTH},
        // North
        {Direction.DOWN, Direction.UP, Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST},
        // East
        {Direction.DOWN, Direction.UP, Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH},
    };
    private static final Direction[][] FLOOR_TRANSLATIONS = {
        // South
        {Direction.SOUTH, Direction.NORTH, Direction.DOWN, Direction.UP, Direction.WEST, Direction.EAST},
        // West
        {Direction.SOUTH, Direction.NORTH, Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP},
        // North
        {Direction.SOUTH, Direction.NORTH, Direction.UP, Direction.DOWN, Direction.EAST, Direction.WEST},
        // East
        {Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST, Direction.UP, Direction.DOWN},
    };
    private static final Direction[][] CEILING_TRANSLATIONS = {
        // South
        {Direction.NORTH, Direction.SOUTH, Direction.UP, Direction.DOWN, Direction.WEST, Direction.EAST},
        // West
        {Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.UP, Direction.DOWN},
        // North
        {Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP, Direction.EAST, Direction.WEST},
        // East
        {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.DOWN, Direction.UP},
    };
}
