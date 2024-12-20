package li.cil.ocreloaded.minecraft.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.TickingBlockEntity;

public class BlockEntityTicker implements TickingBlockEntity {

    private BlockEntityWithTick blockEntity;

    public BlockEntityTicker(BlockEntityWithTick blockEntity) {
        this.blockEntity = blockEntity;
    }

    @Override
    public void tick() {
        blockEntity.tick();
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
