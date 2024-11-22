package li.cil.ocreloaded.minecraft.common.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import li.cil.ocreloaded.core.graphics.TextModeBuffer;
import li.cil.ocreloaded.core.machine.component.Component;
import li.cil.ocreloaded.minecraft.common.block.ScreenBlock;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import li.cil.ocreloaded.minecraft.common.util.RotationHelper;
import li.cil.ocreloaded.minecraft.server.component.ScreenComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;


public class ScreenBlockEntity extends BlockEntity implements ComponentBlockEntity {

    private UUID id = UUID.randomUUID();
    private TextModeBuffer screenBuffer;

    private int width = 1, height = 1;

    private ScreenBlockEntity origin;
    private Set<ScreenBlockEntity> screens = new HashSet<ScreenBlockEntity>();

    public ScreenBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CommonRegistered.SCREEN_BLOCK_ENTITY.get(), blockPos, blockState);

        this.origin = this;
        screens.add(this);
    }

    @Override
    public Component initComponent() {
        return new ScreenComponent(id, screenBuffer);
    }

    @Override
    public void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compoundTag = super.getUpdateTag();
        return compoundTag;
    }

    // Converts global coordinates to local coordinates
    private LocalPosition project(GlobalPosition position) {
        Direction globalX = toGlobal(Direction.EAST);
        Direction globalY = toGlobal(Direction.UP);
        Direction globalZ = toGlobal(Direction.SOUTH);

        int x = position.x * globalX.getStepX() + position.y * globalY.getStepX() + position.z * globalZ.getStepX();
        int y = position.x * globalX.getStepY() + position.y * globalY.getStepY() + position.z * globalZ.getStepY();
        int z = position.x * globalX.getStepZ() + position.y * globalY.getStepZ() + position.z * globalZ.getStepZ();

        return new LocalPosition(x, y, z);
    }

    // Converts local coordinates to global coordinates
    private GlobalPosition unproject(LocalPosition position) {
        Direction localX = toLocal(Direction.EAST);
        Direction localY = toLocal(Direction.UP);
        Direction localZ = toLocal(Direction.SOUTH);

        int x = position.x * localX.getStepX() + position.y * localY.getStepX() + position.z * localZ.getStepX();
        int y = position.x * localX.getStepY() + position.y * localY.getStepY() + position.z * localZ.getStepY();
        int z = position.x * localX.getStepZ() + position.y * localY.getStepZ() + position.z * localZ.getStepZ();

        return new GlobalPosition(x, y, z);
    }

    // Change the given direction to the global direction.
    private Direction toGlobal(Direction direction) {
        return RotationHelper.toGlobal(direction, getBlockState().getValue(ScreenBlock.FACING), getBlockState().getValue(ScreenBlock.ATTACH_FACE));
    }

    // Change the given direction to the local direction.
    private Direction toLocal(Direction direction) {
        return RotationHelper.toLocal(direction, getBlockState().getValue(ScreenBlock.FACING), getBlockState().getValue(ScreenBlock.ATTACH_FACE));
    }
    
    private record LocalPosition(int x, int y, int z) {}
    private record GlobalPosition(int x, int y, int z) {}

}
