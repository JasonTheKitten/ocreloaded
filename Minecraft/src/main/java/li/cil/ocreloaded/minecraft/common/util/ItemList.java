package li.cil.ocreloaded.minecraft.common.util;

import java.util.Arrays;
import java.util.List;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class ItemList extends NonNullList<ItemStack> {

    private final ItemChangeListener listener;
    
    public ItemList(List<ItemStack> items, ItemChangeListener listener) {
        super(items, ItemStack.EMPTY);
        this.listener = listener;
    }

    @Override
    public ItemStack set(int index, ItemStack stack) {
        ItemStack oldStack = super.set(index, stack);
        listener.onItemChange(index, oldStack, stack);
        return oldStack;
    }

    @Override
    public void add(int index, ItemStack stack) {
        super.add(index, stack);
        listener.onItemChange(index, ItemStack.EMPTY, stack);
    }

    @Override
    public ItemStack remove(int index) {
        ItemStack oldStack = super.remove(index);
        listener.onItemChange(index, oldStack, ItemStack.EMPTY);
        return oldStack;
    }

    public static ItemList withSize(int size, ItemChangeListener listener) {
        ItemStack[] items = new ItemStack[size];
        Arrays.fill(items, ItemStack.EMPTY);
        return new ItemList(Arrays.asList(items), listener);
    }

    public static interface ItemChangeListener {
        void onItemChange(int slot, ItemStack oldStack, ItemStack newStack);
    }

}
