package li.cil.ocreloaded.minecraft.common.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import li.cil.ocreloaded.core.graphics.TextModeBuffer;
import li.cil.ocreloaded.core.machine.component.Component;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import li.cil.ocreloaded.minecraft.server.component.ScreenComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;


public class ScreenBlockEntity extends BlockEntity implements ComponentBlockEntity {

    private UUID id = UUID.randomUUID();
    private TextModeBuffer screenBuffer = TextModeBuffer.create();

    private Set<ScreenBlockEntity> screens = new HashSet<ScreenBlockEntity>();

    public ScreenBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CommonRegistered.SCREEN_BLOCK_ENTITY.get(), blockPos, blockState);

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

    public TextModeBuffer getScreenBuffer() {
        return screenBuffer;
    }

}
