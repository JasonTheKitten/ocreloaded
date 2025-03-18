package li.cil.ocreloaded.minecraft.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;

public class BlockEntityTicker implements TickingBlockEntity {

    private BlockEntity blockEntity;

    public BlockEntityTicker(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    @Override
    public void tick() {
        ((TickableEntity) blockEntity).tick();
    }

    @Override
    public boolean isRemoved() {
        return blockEntity.isRemoved();
    }

    @Override
    public BlockPos getPos() {
        return blockEntity.getBlockPos();
    }

    @Override
    public String getType() {
        return blockEntity.getType().toString();
    }
    
}
