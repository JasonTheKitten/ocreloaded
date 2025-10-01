package li.cil.ocreloaded.minecraft.common.menu;

import javax.annotation.Nonnull;

import li.cil.ocreloaded.minecraft.common.item.TieredItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ComponentSlot extends Slot {

    private final Class<? extends Item> itemClass;
    private final ResourceLocation texture;
    private final int teir;

    public ComponentSlot(Container container, int index, int x, int y, Class<? extends Item> itemClass, ResourceLocation texture, int tier) {
        super(container, index, x, y);
        this.itemClass = itemClass;
        this.texture = texture;
        this.teir = tier;
    }

    public ComponentSlot(Container container, int index, int x, int y, Class<? extends Item> itemClass, ResourceLocation texture) {
        this(container, index, x, y, itemClass, texture, 0);
    }
    
    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return itemClass.isInstance(stack.getItem()) && (
            !(stack.getItem() instanceof TieredItem) ||
            ((TieredItem) stack.getItem()).getTier() <= teir);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public int getTier() {
        return teir;
    }

}
