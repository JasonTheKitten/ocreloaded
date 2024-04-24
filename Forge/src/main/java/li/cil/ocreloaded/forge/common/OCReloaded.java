package li.cil.ocreloaded.forge.common;


import li.cil.ocreloaded.forge.client.OCReloadedClient;
import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import li.cil.ocreloaded.minecraft.common.PlatformSpecific;
import li.cil.ocreloaded.minecraft.common.network.NetworkHandler;
import li.cil.ocreloaded.minecraft.common.network.NetworkInterface;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import li.cil.ocreloaded.minecraft.common.registry.Named;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod(OCReloadedCommon.MOD_ID)
public class OCReloaded {

    public OCReloaded() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        MinecraftForge.EVENT_BUS.register(new ForgeEventBus());
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> OCReloadedClient::new);

        registerNetworkHandlers(PlatformSpecific.get().getNetworkInterface());
    }

    @SubscribeEvent
    public void register(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.BLOCKS, this::registerBlocks);
        event.register(ForgeRegistries.Keys.ITEMS, this::registerItems);
        event.register(ForgeRegistries.Keys.MENU_TYPES, this::registerMenuTypes);
        event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, this::registerBlockEntities);
        event.register(
            ResourceKey.createRegistryKey(new ResourceLocation("minecraft", "creative_mode_tab")),
            this::registerCreativeTab);
    }

    private void registerBlocks(RegisterEvent.RegisterHelper<Block> helper) {
        for (Named<Block> namedBlock : CommonRegistered.ALL_BLOCKS) {
            ResourceLocation blockResource = new ResourceLocation(OCReloadedCommon.MOD_ID, namedBlock.name());
            helper.register(blockResource, namedBlock.entity());
        }
    }

    private void registerItems(RegisterEvent.RegisterHelper<Item> helper) {
        for (Named<Item> namedItem : CommonRegistered.ALL_ITEMS) {
            ResourceLocation itemResource = new ResourceLocation(OCReloadedCommon.MOD_ID, namedItem.name());
            helper.register(itemResource, namedItem.entity());
        }
    }

    private void registerMenuTypes(RegisterEvent.RegisterHelper<MenuType<?>> event) {
        for (Named<MenuType<?>> namedMenuType : CommonRegistered.ALL_MENU_TYPES) {
            ResourceLocation menuTypeResource = new ResourceLocation(OCReloadedCommon.MOD_ID, namedMenuType.name());
            event.register(menuTypeResource, namedMenuType.entity());
        }
    }

    private void registerBlockEntities(RegisterEvent.RegisterHelper<BlockEntityType<?>> event) {
        for (Named<BlockEntityType<?>> namedBlockEntity : CommonRegistered.ALL_BLOCK_ENTITIES) {
            ResourceLocation blockEntityResource = new ResourceLocation(OCReloadedCommon.MOD_ID, namedBlockEntity.name());
            event.register(blockEntityResource, namedBlockEntity.entity());
        }
    }

    private void registerCreativeTab(RegisterEvent.RegisterHelper<CreativeModeTab> event) {
        CreativeModeTab tab = CreativeModeTab.builder()
            .icon(() ->
                new ItemStack(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(OCReloadedCommon.MOD_ID, "case1"))))
            .title(Component.translatable("title.ocreloaded"))
            .displayItems((params, output) -> {
                addCreativeTabItems(output);
            })
            .build();
        event.register(new ResourceLocation(OCReloadedCommon.MOD_ID, "creative_mode_tab"), tab);
        
    }

    private void addCreativeTabItems(Output output) {
        for (Named<Item> namedBlock : CommonRegistered.ALL_ITEMS) {
            output.accept(namedBlock.entity());
        }
    }

    private void registerNetworkHandlers(NetworkInterface networkInterface) {
        for (NetworkHandler<?> handler : OCReloadedCommon.NETWORK_HANDLERS) {
            networkInterface.registerNetworkHandler(handler);
        }
    }

}
