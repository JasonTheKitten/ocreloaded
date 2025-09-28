package li.cil.ocreloaded.minecraft.common.menu;

import java.util.List;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ComponentQuickMove {
    
    public static ItemStack quickMoveStack(List<Slot> slots, Player player, int index) {
        Slot slot = slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack sourceStack = slot.getItem();
        ItemStack originalStack = sourceStack.copy();

        boolean isComponentSlot = slot instanceof ComponentSlot;

        boolean success = moveToTarget(slots, sourceStack, !isComponentSlot);
        if (!success) {
            return ItemStack.EMPTY;
        }

        if (sourceStack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        }
        slot.setChanged();

        return originalStack;
    }

    private static boolean moveToTarget(List<Slot> slots, ItemStack sourceStack, boolean targetsComponentSlot) {
        int targetSlot = findTargetSlot(slots, sourceStack, targetsComponentSlot);
        if (targetSlot == -1) return false;

        Slot target = slots.get(targetSlot);
        ItemStack targetStack = slots.get(targetSlot).getItem();
        ItemStack moveStack = sourceStack.split(1);
        if (targetStack.isEmpty()) {
            slots.get(targetSlot).set(moveStack);
        } else {
            targetStack.grow(1);
        }
        target.setChanged();

        return true;
    }

    private static int findTargetSlot(List<Slot> slots, ItemStack sourceStack, boolean isComponentSlot) {
        int target = -1;
        for (int i = 0; i < slots.size(); i++) {
            Slot slot = slots.get(i);
            
            // Must be type specified in passed flag
            if (slot instanceof ComponentSlot != isComponentSlot) continue;

            // Must pass the slot's checks
            if (!slot.mayPlace(sourceStack)) continue;

            // If occupied, must be the same item
            if (slot.hasItem() && !ItemStack.isSameItem(slot.getItem(), sourceStack)) continue;

            // If occupied, must fit the slot's stack size
            if (slot.hasItem() && slot.getItem().getCount() >= slot.getMaxStackSize()) continue;

            // If and a target is already found, and target is a component slot, then must be of a lower tier than the chosen target
            if (
                isComponentSlot
                && target != -1 
                && ((ComponentSlot) slot).getTier() >= ((ComponentSlot) slots.get(target)).getTier()
            ) continue;

            target = i;
        }

        return target;
    }

}
