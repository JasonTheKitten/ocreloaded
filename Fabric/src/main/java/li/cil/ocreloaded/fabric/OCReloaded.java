package li.cil.ocreloaded.fabric;

import li.cil.ocreloaded.minecraft.OCReloadedCommon;
import li.cil.ocreloaded.minecraft.block.BlockInfo;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
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

    private final OCReloadedCommon common = new OCReloadedCommon();

    @Override
    public void onInitialize() {
        registerBlocks();
        registerCreativeTab();
    }

    private void registerBlocks() {
        for (BlockInfo blockInfo : common.getBlockInfos()) {
            Block block = convertToBlock(blockInfo);
            Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(OCReloadedCommon.MOD_ID, blockInfo.blockName()), block);

            BlockItem blockItem = new BlockItem(block, new BlockItem.Properties());
            Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(OCReloadedCommon.MOD_ID, blockInfo.blockName()), blockItem);
        }
    }

    private Block convertToBlock(BlockInfo blockInfo) {
        return new Block(FabricBlockSettings.create()
            .strength(blockInfo.strength()));
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
        for (BlockInfo blockInfo : common.getBlockInfos()) {
            if (blockInfo.hasCreativeTab()) {
                ResourceLocation blockResource = new ResourceLocation(OCReloadedCommon.MOD_ID, blockInfo.blockName());
                Block block = BuiltInRegistries.BLOCK.get(blockResource);
                output.accept(block);
            }
        }
    }

}
