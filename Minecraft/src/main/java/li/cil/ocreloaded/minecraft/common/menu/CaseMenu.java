package li.cil.ocreloaded.minecraft.common.menu;

import li.cil.ocreloaded.minecraft.common.container.BasicContainer;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class CaseMenu extends AbstractContainerMenu {

    private Inventory inventory;
    private Container container;

    public CaseMenu(int id, Inventory inventory) {
        super(CommonRegistered.CASE_MENU_TYPE, id);
        this.inventory = inventory;
        this.container = new BasicContainer(9);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return inventory.stillValid(player);
    }
    
}
