package li.cil.ocreloaded.minecraft.common.entity;

import li.cil.ocreloaded.minecraft.common.block.CaseBlock;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CaseBlockEntity extends BlockEntity {

    private final NonNullList<ItemStack> items = NonNullList.withSize(10, ItemStack.EMPTY);
    private boolean powered;

    public CaseBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CommonRegistered.CASE_BLOCK_ENTITY, blockPos, blockState);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);

        ContainerHelper.loadAllItems(compoundTag, this.items);
        this.powered = compoundTag.getBoolean("ocreloaded:powered");
        updateBlockState();
    }

    @Override
    public void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);

        ContainerHelper.saveAllItems(compoundTag, this.items);
        compoundTag.putBoolean("ocreloaded:powered", this.powered);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compoundTag = super.getUpdateTag();
        compoundTag.putBoolean("ocreloaded:powered", this.powered);

        return compoundTag;
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public boolean isPowered() {
        return this.powered;
    }

    public void setPowered(boolean b) {
        this.powered = b;
        setChanged();
        updateBlockState();
    }

    private void updateBlockState() {
        if (this.level == null || !level.isClientSide) return;

        if (this.level.isLoaded(this.worldPosition) && this.getBlockState().getBlock() instanceof CaseBlock) {  
            BlockState newBlockState = level.getBlockState(this.worldPosition).setValue(CaseBlock.RUNNING, this.powered);
            level.setBlock(this.worldPosition, newBlockState, 3);
        }
    }
    
}
