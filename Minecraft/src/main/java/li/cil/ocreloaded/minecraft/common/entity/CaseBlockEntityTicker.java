package li.cil.ocreloaded.minecraft.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.TickingBlockEntity;

public class CaseBlockEntityTicker implements TickingBlockEntity {

    private CaseBlockEntity caseBlockEntity;

    public CaseBlockEntityTicker(CaseBlockEntity caseBlockEntity) {
        this.caseBlockEntity = caseBlockEntity;
    }

    @Override
    public void tick() {
        caseBlockEntity.tick();
    }

    @Override
    public boolean isRemoved() {
        return caseBlockEntity.isRemoved();
    }

    @Override
    public BlockPos getPos() {
        return caseBlockEntity.getBlockPos();
    }

    @Override
    public String getType() {
        return caseBlockEntity.getType().toString();
    }
    
}
