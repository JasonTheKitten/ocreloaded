package li.cil.ocreloaded.fabric;

import li.cil.ocreloaded.minecraft.Entities;
import li.cil.ocreloaded.minecraft.Entities.Named;
import li.cil.ocreloaded.minecraft.OCReloadedCommon;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class OCReloaded implements ModInitializer {

    @Override
    public void onInitialize() {
        registerBlocks();
        registerCreativeTab();
    }

    private void registerBlocks() {
        for (Named<Block> namedBlock : Entities.ALL_BLOCKS) {
            Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(OCReloadedCommon.MOD_ID, namedBlock.name()), namedBlock.entity());
        }
        for (Named<BlockItem> namedBlockItem : Entities.ALL_BLOCK_ITEMS) {
            Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(OCReloadedCommon.MOD_ID, namedBlockItem.name()), namedBlockItem.entity());
        }
    }

     private void registerCreativeTab() {
        CreativeModeTab tab = FabricItemGroup.builder()
            .icon(() ->
                new ItemStack(BuiltInRegistries.BLOCK.get(new ResourceLocation(OCReloadedCommon.MOD_ID, "case"))))
            .title(Component.translatable("title.ocreloaded"))
            .displayItems((params, output) -> {
                addCreativeTabItems(output);
            })
            .build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(OCReloadedCommon.MOD_ID, "ocreloaded"), tab);
    }

    private void addCreativeTabItems(Output output) {
        for (Named<Block> namedBlock : Entities.ALL_BLOCKS) {
            output.accept(namedBlock.entity());
        }
    }

}
