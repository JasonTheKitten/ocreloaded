package li.cil.ocreloaded.minecraft.common.menu;

import li.cil.ocreloaded.minecraft.common.entity.ScreenBlockEntity;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class ScreenMenu extends AbstractContainerMenu {

    private final ScreenBlockEntity blockEntity;
    private final Inventory inventory;

    public ScreenMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        super(CommonRegistered.SCREEN_MENU_TYPE.get(), id);

        this.blockEntity = (ScreenBlockEntity) inventory.player.level().getBlockEntity(data.readBlockPos());
        this.inventory = inventory;
    }

    @Override
    public ItemStack quickMoveStack(Player var1, int var2) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player var1) {
        return inventory.stillValid(var1);
    }

    public ScreenBlockEntity getBlockEntity() {
        return blockEntity;
    }
    
}
