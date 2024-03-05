package li.cil.ocreloaded.forge;


import li.cil.ocreloaded.minecraft.OCReloadedCommon;
import li.cil.ocreloaded.minecraft.block.BlockInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod(OCReloaded.MOD_ID)
public class OCReloaded {

    public static final String MOD_ID = "ocreloaded";

    private final OCReloadedCommon common = new OCReloadedCommon();

    public OCReloaded() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void register(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.BLOCKS, this::registerBlocks);
    }

    private void registerBlocks(RegisterEvent.RegisterHelper<Block> helper) {
        for (BlockInfo blockInfo : common.getBlockInfos()) {
            helper.register(
                new ResourceLocation(MOD_ID, blockInfo.blockName()),
                convertToBlock(blockInfo)
            );
        }
    }

    private Block convertToBlock(BlockInfo blockInfo) {
        return new Block(BlockBehaviour.Properties.of());
    }
}
