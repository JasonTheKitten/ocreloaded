package li.cil.ocreloaded.minecraft.common.entity;

import java.util.UUID;

import li.cil.ocreloaded.core.graphics.TextModeBuffer;
import li.cil.ocreloaded.core.machine.component.Component;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import li.cil.ocreloaded.minecraft.server.component.ScreenComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ScreenBlockEntity extends BlockEntity implements ComponentBlockEntity {

    private UUID id = UUID.randomUUID();
    private TextModeBuffer screenBuffer;
    
    public ScreenBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CommonRegistered.SCREEN_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public Component initComponent() {
        return new ScreenComponent(id, screenBuffer);
    }

}
