package li.cil.ocreloaded.minecraft.common.item;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

public class EnergyBlockItem extends BlockItem {

    private final Supplier<List<Component>> tooltipSupplier;

    public EnergyBlockItem(Block block, Item.Properties properties, Supplier<List<Component>> tooltipSupplier) {
        super(block, properties);
        this.tooltipSupplier = tooltipSupplier;
    }

    @Override
    public void appendHoverText(
        @Nonnull ItemStack stack,
        @Nonnull Item.TooltipContext context,
        @Nonnull List<Component> tooltip,
        @Nonnull TooltipFlag flag
    ) {
        tooltip.addAll(tooltipSupplier.get());
    }

    public static Component tooltip(String key, Object... args) {
        return Component.translatable(key, args).withStyle(ChatFormatting.GRAY);
    }

}
