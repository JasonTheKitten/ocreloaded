package li.cil.ocreloaded.minecraft.common.menu.provider;

import io.netty.buffer.Unpooled;
import li.cil.ocreloaded.minecraft.common.menu.CaseMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class CaseMenuProvider implements MenuProvider {

    private static final Component MENU_NAME = Component.translatable("gui.ocreloaded.case");

    private final BlockPos blockPos;
    private final int tier;

    public CaseMenuProvider(BlockPos blockPos, int tier) {
        this.blockPos = blockPos;
        this.tier = tier;
    }
    
    @Override
    public Component getDisplayName() {
        return MENU_NAME;
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
        writeData(data);
        return new CaseMenu(windowId, playerInventory, data);
    }

    public void writeData(FriendlyByteBuf data) {
        data.writeBlockPos(this.blockPos);
        data.writeInt(tier);
    }

}