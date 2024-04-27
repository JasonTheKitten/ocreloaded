package li.cil.ocreloaded.fabric.common;

import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import li.cil.ocreloaded.minecraft.common.PlatformSpecific;
import li.cil.ocreloaded.minecraft.common.network.NetworkHandler;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import li.cil.ocreloaded.minecraft.common.registry.Named;
import li.cil.ocreloaded.minecraft.server.CommonServerHooks;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
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

import java.util.Map;

public class OCReloaded implements ModInitializer {

    @Override
    public void onInitialize() {
        registerBlocks();
        registerCreativeTab();
        registerNetworkHandlers();

        runServerHandlers();
    }

    private void registerBlocks() {

    }

     private void registerCreativeTab() {
        CreativeModeTab tab = FabricItemGroup.builder()
            .icon(() ->
                new ItemStack(BuiltInRegistries.BLOCK.get(new ResourceLocation(OCReloadedCommon.MOD_ID, "case1"))))
            .title(Component.translatable("title.ocreloaded"))
            .displayItems((params, output) -> {
                addCreativeTabItems(output);
            })
            .build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(OCReloadedCommon.MOD_ID, "ocreloaded"), tab);
    }

    private void addCreativeTabItems(Output output) {
        for (Map.Entry<ResourceKey<Item>, Item> namedItem : CommonRegistered.ITEMS.entrySet()) {
            output.accept(namedItem.getValue());
        }
    }

    private void registerNetworkHandlers() {
        for (NetworkHandler<?> handler : OCReloadedCommon.NETWORK_HANDLERS) {
            PlatformSpecific.get().getNetworkInterface().registerNetworkHandler(handler);
        }
    }

    private void runServerHandlers() {
        ServerWorldEvents.LOAD.register((server, level) -> {
            CommonServerHooks.setup(server);
        });
    }

}
