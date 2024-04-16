package li.cil.ocreloaded.minecraft.common.container;

import java.util.List;
import java.util.ArrayList;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BasicContainer implements Container {

    private final List<ItemStack> items;

    public BasicContainer(int size) {
        this.items = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            this.items.add(ItemStack.EMPTY);
        }
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < this.items.size(); i++) {
            this.items.set(i, ItemStack.EMPTY);
        }
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public ItemStack getItem(int index) {
        return this.items.get(index);
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack item : this.items) {
            if (!item.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack removeItem(int index, int amount) {
        ItemStack item = this.items.get(index);
        if (item.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (item.getCount() <= amount) {
            this.items.set(index, ItemStack.EMPTY);
            return item;
        }

        ItemStack result = item.split(amount);
        if (item.isEmpty()) {
            this.items.set(index, ItemStack.EMPTY);
        }

        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return this.items.set(index, ItemStack.EMPTY);
    }

    @Override
    public void setChanged() {
        // No-op
    }

    @Override
    public void setItem(int index, ItemStack itemStack) {
        this.items.set(index, itemStack);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
    
}
