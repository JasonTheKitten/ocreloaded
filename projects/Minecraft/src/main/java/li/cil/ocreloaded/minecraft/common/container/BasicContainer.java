package li.cil.ocreloaded.minecraft.common.container;

import javax.annotation.Nonnull;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BasicContainer implements Container {

    private final NonNullList<ItemStack> items;
    private final Runnable onChanged;

    public BasicContainer(NonNullList<ItemStack> items, Runnable onChanged) {
        this.items = items;
        this.onChanged = onChanged;
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
        this.onChanged.run();
    }

    @Override
    public void setItem(int index, @Nonnull ItemStack itemStack) {
        this.items.set(index, itemStack);
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return true;
    }
    
}
