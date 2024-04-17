package li.cil.ocreloaded.minecraft.common.block;

import io.netty.buffer.Unpooled;
import li.cil.ocreloaded.minecraft.common.PlatformSpecific;
import li.cil.ocreloaded.minecraft.common.PlatformSpecificImp.NetworkMenuProvider;
import li.cil.ocreloaded.minecraft.common.menu.CaseMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CaseBlock extends Block implements NetworkMenuProvider {

    private static final Component MENU_NAME = Component.translatable("gui.ocreloaded.computer");

    public CaseBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            MenuProvider menuProvider = state.getMenuProvider(level, pos);
            if (menuProvider != null) {
                PlatformSpecific.get().openMenu(player, (NetworkMenuProvider) menuProvider);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
        writeData(player, data);
        return new CaseMenu(windowId, playerInventory, data);
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return this;
    }

    @Override
    public Component getDisplayName() {
        return MENU_NAME;
    }

    @Override
    public void writeData(Player player, FriendlyByteBuf data) {
        data.writeInt(1);
    }
    
}
