package li.cil.ocreloaded.minecraft.common.menu;

import java.util.List;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ComponentQuickMove {
    
    public static ItemStack quickMoveStack(List<Slot> slots, Player player, int index) {
        // TODO: Prefer exact tier match - can make isBetterMatch method
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
            if ((slot instanceof ComponentSlot) != isComponentSlot) continue;
            if (!slot.mayPlace(sourceStack)) continue;
            if (slot.hasItem() && !ItemStack.isSameItem(slot.getItem(), sourceStack)) continue;
            if (slot.hasItem() && slot.getItem().getCount() >= slot.getMaxStackSize()) continue;
            if (!slot.hasItem() && target != -1) continue;
            if (slot.hasItem()) return i;
            target = i;
        }

        return target;
    }

}
