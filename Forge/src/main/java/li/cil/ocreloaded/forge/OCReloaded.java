package li.cil.ocreloaded.forge;


import li.cil.ocreloaded.minecraft.Entities;
import li.cil.ocreloaded.minecraft.Entities.Named;
import li.cil.ocreloaded.minecraft.OCReloadedCommon;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod(OCReloadedCommon.MOD_ID)
public class OCReloaded {

    private final OCReloadedCommon common = new OCReloadedCommon();

    public OCReloaded() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void register(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.BLOCKS, this::registerBlocks);
        event.register(ForgeRegistries.Keys.ITEMS, this::registerBlockItems);
        event.register(
            ResourceKey.createRegistryKey(new ResourceLocation("minecraft", "creative_mode_tab")),
            this::registerCreativeTab);
    }

    private void registerBlocks(RegisterEvent.RegisterHelper<Block> helper) {
        for (Named<Block> namedBlock : Entities.ALL_BLOCKS) {
            ResourceLocation blockResource = new ResourceLocation(OCReloadedCommon.MOD_ID, namedBlock.name());
            helper.register(blockResource, namedBlock.entity());
        }
    }

    private void registerBlockItems(RegisterEvent.RegisterHelper<Item> helper) {
        for (Named<BlockItem> namedBlockItem : Entities.ALL_BLOCK_ITEMS) {
            ResourceLocation blockResource = new ResourceLocation(OCReloadedCommon.MOD_ID, namedBlockItem.name());
            helper.register(blockResource, namedBlockItem.entity());
        }
    }

    private void registerCreativeTab(RegisterEvent.RegisterHelper<CreativeModeTab> event) {
        CreativeModeTab tab = CreativeModeTab.builder()
            .icon(() ->
                new ItemStack(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(OCReloadedCommon.MOD_ID, "case"))))
            .title(Component.translatable("title.ocreloaded"))
            .displayItems((params, output) -> {
                addCreativeTabItems(output);
            })
            .build();
        event.register(new ResourceLocation(OCReloadedCommon.MOD_ID, "creative_mode_tab"), tab);
        
    }

    private void addCreativeTabItems(Output output) {
        for (Named<Block> namedBlock : Entities.ALL_BLOCKS) {
            output.accept(namedBlock.entity());
        }
    }

}
