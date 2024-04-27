package li.cil.ocreloaded.minecraft.common.registry;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import li.cil.ocreloaded.minecraft.common.block.CaseBlock;
import li.cil.ocreloaded.minecraft.common.entity.CaseBlockEntity;
import li.cil.ocreloaded.minecraft.common.item.CPUItem;
import li.cil.ocreloaded.minecraft.common.item.CardItem;
import li.cil.ocreloaded.minecraft.common.item.EepromItem;
import li.cil.ocreloaded.minecraft.common.item.HardDiskItem;
import li.cil.ocreloaded.minecraft.common.item.MemoryItem;
import li.cil.ocreloaded.minecraft.common.menu.CaseMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class CommonRegistered {

    private static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(OCReloadedCommon.MOD_ID));
    private static final Registrar<Block> BLOCKS = MANAGER.get().get(Registries.BLOCK);
    public static final Registrar<Item> ITEMS = MANAGER.get().get(Registries.ITEM);
    private static final Registrar<MenuType<?>> MENUS = MANAGER.get().get(Registries.MENU);
    private static final Registrar<BlockEntityType<?>> BLOCK_ENTITIES = MANAGER.get().get(Registries.BLOCK_ENTITY_TYPE);
    private static final Registrar<CreativeModeTab> CREATIVE_TABS = MANAGER.get().get(Registries.CREATIVE_MODE_TAB);

    private static final Properties DEFAULT_BLOCK_PROPERTIES = Properties.of().strength(2f);

    // Blocks
    public static final RegistrySupplier<Block> CASE_BLOCK_TIER_1 = BLOCKS.register(name("case1"), () -> new CaseBlock(DEFAULT_BLOCK_PROPERTIES, 1));
    public static final RegistrySupplier<Block> CASE_BLOCK_TIER_2 = BLOCKS.register(name("case2"), () -> new CaseBlock(DEFAULT_BLOCK_PROPERTIES, 2));
    public static final RegistrySupplier<Block> CASE_BLOCK_TIER_3 = BLOCKS.register(name("case3"), () -> new CaseBlock(DEFAULT_BLOCK_PROPERTIES, 3));
    public static final RegistrySupplier<Block> CASE_BLOCK_CREATIVE = BLOCKS.register(name("casecreative"), () -> new CaseBlock(DEFAULT_BLOCK_PROPERTIES, 4));

    // Items
    public static final RegistrySupplier<Item> CASE_BLOCK_ITEM_TIER_1 = ITEMS.register(name("case1"), () -> new BlockItem(CASE_BLOCK_TIER_1.value(), new BlockItem.Properties()));
    public static final RegistrySupplier<Item> CASE_BLOCK_ITEM_TIER_2 = ITEMS.register(name("case2"), () -> new BlockItem(CASE_BLOCK_TIER_2.value(), new BlockItem.Properties()));
    public static final RegistrySupplier<Item> CASE_BLOCK_ITEM_TIER_3 = ITEMS.register(name("case3"), () -> new BlockItem(CASE_BLOCK_TIER_3.value(), new BlockItem.Properties()));
    public static final RegistrySupplier<Item> CASE_BLOCK_ITEM_CREATIVE = ITEMS.register(name("casecreative"), () -> new BlockItem(CASE_BLOCK_CREATIVE.value(), new BlockItem.Properties()));
    public static final RegistrySupplier<Item> CPU_TIER_1 = ITEMS.register(name("cpu1"), () -> new CPUItem(new Item.Properties(), 1));
    public static final RegistrySupplier<Item> CPU_TIER_2 = ITEMS.register(name("cpu2"), () -> new CPUItem(new Item.Properties(), 2));
    public static final RegistrySupplier<Item> CPU_TIER_3 = ITEMS.register(name("cpu3"), () -> new CPUItem(new Item.Properties(), 3));
    public static final RegistrySupplier<Item> DATACARD_TIER_1 = ITEMS.register(name("datacard1"), () -> new CardItem(new Item.Properties(), 1));
    public static final RegistrySupplier<Item> DATACARD_TIER_2 = ITEMS.register(name("datacard2"), () -> new CardItem(new Item.Properties(), 2));
    public static final RegistrySupplier<Item> DATACARD_TIER_3 = ITEMS.register(name("datacard3"), () -> new CardItem(new Item.Properties(), 3));
    public static final RegistrySupplier<Item> EEPROM = ITEMS.register(name("eeprom"), () -> new EepromItem(new Item.Properties()));
    public static final RegistrySupplier<Item> GRAPHICSCARD_TIER_1 = ITEMS.register(name("graphicscard1"), () -> new CardItem(new Item.Properties(), 1));
    public static final RegistrySupplier<Item> GRAPHICSCARD_TIER_2 = ITEMS.register(name("graphicscard2"), () -> new CardItem(new Item.Properties(), 2));
    public static final RegistrySupplier<Item> GRAPHICSCARD_TIER_3 = ITEMS.register(name("graphicscard3"), () -> new CardItem(new Item.Properties(), 3));
    public static final RegistrySupplier<Item> HARDDISKDRIVE_TIER_1 = ITEMS.register(name("harddiskdrive1"), () -> new HardDiskItem(new Item.Properties(), 1));
    public static final RegistrySupplier<Item> HARDDISKDRIVE_TIER_2 = ITEMS.register(name("harddiskdrive2"), () -> new HardDiskItem(new Item.Properties(), 2));
    public static final RegistrySupplier<Item> HARDDISKDRIVE_TIER_3 = ITEMS.register(name("harddiskdrive3"), () -> new HardDiskItem(new Item.Properties(), 3));
    public static final RegistrySupplier<Item> MEMORY_TIER_1 = ITEMS.register(name("memory1"), () -> new MemoryItem(new Item.Properties(), 1));
    public static final RegistrySupplier<Item> MEMORY_TIER_1_5 = ITEMS.register(name("memory1_5"), () -> new MemoryItem(new Item.Properties(), 1));
    public static final RegistrySupplier<Item> MEMORY_TIER_2 = ITEMS.register(name("memory2"), () -> new MemoryItem(new Item.Properties(), 2));
    public static final RegistrySupplier<Item> MEMORY_TIER_2_5 = ITEMS.register(name("memory2_5"), () -> new MemoryItem(new Item.Properties(), 2));
    public static final RegistrySupplier<Item> MEMORY_TIER_3 = ITEMS.register(name("memory3"), () -> new MemoryItem(new Item.Properties(), 3));
    public static final RegistrySupplier<Item> MEMORY_TIER_3_5 = ITEMS.register(name("memory3_5"), () -> new MemoryItem(new Item.Properties(), 3));

    // Menu types
    public static final RegistrySupplier<MenuType<CaseMenu>> CASE_MENU_TYPE = MENUS.register(name("case"), () -> MenuRegistry.ofExtended(CaseMenu::new));


    // Block entities
    private BlockEntityType.BlockEntitySupplier<CaseBlockEntity> caseBlockEntitySupplier = null;
    public static final RegistrySupplier<BlockEntityType<CaseBlockEntity>> CASE_BLOCK_ENTITY = BLOCK_ENTITIES.register(name("case"), () -> BlockEntityType.Builder
            .of(CaseBlockEntity::new, CASE_BLOCK_TIER_1.get(), CASE_BLOCK_TIER_2.get(), CASE_BLOCK_TIER_3.get(), CASE_BLOCK_CREATIVE.get()).build(null));

    // Creative mode tabs
    public static final RegistrySupplier<CreativeModeTab> CREATIVE_TAB = CREATIVE_TABS.register(name("creative"), () -> CreativeTabRegistry.create(
            Component.translatable("title.ocreloaded"),
            () -> new ItemStack(CASE_BLOCK_ITEM_TIER_1.get())
    ));

    private static ResourceLocation name(String name) {
        return new ResourceLocation(OCReloadedCommon.MOD_ID, name);
    }
}
