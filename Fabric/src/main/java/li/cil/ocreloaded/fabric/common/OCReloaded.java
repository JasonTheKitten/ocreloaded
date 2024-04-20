package li.cil.ocreloaded.fabric.common;

import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import li.cil.ocreloaded.minecraft.common.PlatformSpecific;
import li.cil.ocreloaded.minecraft.common.network.NetworkHandler;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import li.cil.ocreloaded.minecraft.common.registry.Named;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class OCReloaded implements ModInitializer {

    @Override
    public void onInitialize() {
        registerBlocks();
        registerCreativeTab();
        registerNetworkHandlers();
    }

    private void registerBlocks() {
        for (Named<Block> namedBlock : CommonRegistered.ALL_BLOCKS) {
            Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(OCReloadedCommon.MOD_ID, namedBlock.name()), namedBlock.entity());
        }
        for (Named<Item> namedItem : CommonRegistered.ALL_ITEMS) {
            Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(OCReloadedCommon.MOD_ID, namedItem.name()), namedItem.entity());
        }
        for (Named<MenuType<?>> namedMenu : CommonRegistered.ALL_MENU_TYPES) {
            Registry.register(BuiltInRegistries.MENU, new ResourceLocation(OCReloadedCommon.MOD_ID, namedMenu.name()), namedMenu.entity());
        }
        for (Named<BlockEntityType<?>> namedBlockEntity : CommonRegistered.ALL_BLOCK_ENTITIES) {
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(OCReloadedCommon.MOD_ID, namedBlockEntity.name()), namedBlockEntity.entity());
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
        for (Named<Item> namedItem : CommonRegistered.ALL_ITEMS) {
            output.accept(namedItem.entity());
        }
    }

    private void registerNetworkHandlers() {
        for (NetworkHandler<?> handler : OCReloadedCommon.NETWORK_HANDLERS) {
            PlatformSpecific.get().getNetworkInterface().registerNetworkHandler(handler);
        }
    }

}
