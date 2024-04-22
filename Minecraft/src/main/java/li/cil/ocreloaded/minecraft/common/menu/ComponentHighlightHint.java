package li.cil.ocreloaded.minecraft.common.menu;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.inventory.Slot;

public class ComponentHighlightHint {
    public static List<Slot> findHighlightHintSlots(List<Slot> slots, Slot hoveredSlot) {
        List<Slot> matchedSlots = new ArrayList<Slot>();
        // if no slot hovered, return empty list
        if (hoveredSlot == null) {
            return matchedSlots;
        }
        boolean isComponentSlot = hoveredSlot instanceof ComponentSlot;
        for (Slot slot: slots) {
            // The hovered slot is a component slot; we want to find items which COULD slot there
            if (isComponentSlot && hoveredSlot.mayPlace(slot.getItem())) {
                matchedSlots.add(slot);
            }
            // The hovered slot is an inventory slot; we want to find component slots which COULD hold the hovered item
            else if (!isComponentSlot && slot instanceof ComponentSlot && slot.mayPlace(hoveredSlot.getItem())) {
                matchedSlots.add(slot);
            }
        }
        return matchedSlots;
    }
}
