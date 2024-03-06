package li.cil.ocreloaded.fabric;

import li.cil.ocreloaded.minecraft.OCReloadedCommon;
import li.cil.ocreloaded.minecraft.block.BlockInfo;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class OCReloaded implements ModInitializer {

    private final OCReloadedCommon common = new OCReloadedCommon();

    @Override
    public void onInitialize() {
        registerBlocks();
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
        return new Block(FabricBlockSettings.create());
    }

}
