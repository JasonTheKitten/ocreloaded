package li.cil.ocreloaded.minecraft.common.registry;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import li.cil.ocreloaded.minecraft.common.block.CaseBlock;
import li.cil.ocreloaded.minecraft.common.block.KeyboardBlock;
import li.cil.ocreloaded.minecraft.common.block.ScreenBlock;
import li.cil.ocreloaded.minecraft.common.entity.CaseBlockEntity;
import li.cil.ocreloaded.minecraft.common.entity.ScreenBlockEntity;
import li.cil.ocreloaded.minecraft.common.item.CPUItem;
import li.cil.ocreloaded.minecraft.common.item.DataCardItem;
import li.cil.ocreloaded.minecraft.common.item.EepromItem;
import li.cil.ocreloaded.minecraft.common.item.FloppyDiskItem;
import li.cil.ocreloaded.minecraft.common.item.GraphicsCardItem;
import li.cil.ocreloaded.minecraft.common.item.HardDiskItem;
import li.cil.ocreloaded.minecraft.common.item.LuaEepromItem;
import li.cil.ocreloaded.minecraft.common.item.MemoryItem;
import li.cil.ocreloaded.minecraft.common.menu.CaseMenu;
import li.cil.ocreloaded.minecraft.common.menu.ScreenMenu;
import li.cil.ocreloaded.minecraft.common.network.NetworkUtil;
import li.cil.ocreloaded.minecraft.common.network.power.PowerNetworkHandler;
import li.cil.ocreloaded.minecraft.common.network.screen.ScreenNetworkHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class CommonRegistered {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(OCReloadedCommon.MOD_ID, Registries.BLOCK);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(OCReloadedCommon.MOD_ID, Registries.ITEM);
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(OCReloadedCommon.MOD_ID, Registries.MENU);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(OCReloadedCommon.MOD_ID, Registries.BLOCK_ENTITY_TYPE);
    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(OCReloadedCommon.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static void initialize() {
        BLOCKS.register();
        ITEMS.register();
        MENUS.register();
        BLOCK_ENTITIES.register();
        CREATIVE_TABS.register();

        NetworkUtil.getInstance().registerHandler("ocreloaded:power", new PowerNetworkHandler());
        NetworkUtil.getInstance().registerHandler("ocreloaded:screen", new ScreenNetworkHandler());
    }

    // Creative mode tabs
    public static final RegistrySupplier<CreativeModeTab> CREATIVE_TAB = CREATIVE_TABS.register("creative", () -> CreativeTabRegistry.create(
        Component.translatable("title.ocreloaded"),
        () -> new ItemStack(CommonRegistered.CASE_BLOCK_ITEM_TIER_1.get())
    ));

    // Default properties
    private static final BlockBehaviour.Properties DEFAULT_BLOCK_PROPERTIES = BlockBehaviour.Properties.of().strength(2f).noOcclusion();
    private static final Item.Properties DEFAULT_ITEM_PROPERTIES = new Item.Properties().arch$tab(CREATIVE_TAB);

    // Blocks
    public static final RegistrySupplier<Block> CASE_BLOCK_TIER_1 = BLOCKS.register("case1", () -> new CaseBlock(DEFAULT_BLOCK_PROPERTIES, 1));
    public static final RegistrySupplier<Block> CASE_BLOCK_TIER_2 = BLOCKS.register("case2", () -> new CaseBlock(DEFAULT_BLOCK_PROPERTIES, 2));
    public static final RegistrySupplier<Block> CASE_BLOCK_TIER_3 = BLOCKS.register("case3", () -> new CaseBlock(DEFAULT_BLOCK_PROPERTIES, 3));
    public static final RegistrySupplier<Block> CASE_BLOCK_CREATIVE = BLOCKS.register("casecreative", () -> new CaseBlock(DEFAULT_BLOCK_PROPERTIES, 4));
    public static final RegistrySupplier<Block> SCREEN_BLOCK_TIER_1 = BLOCKS.register("screen1", () -> new ScreenBlock(DEFAULT_BLOCK_PROPERTIES, 1));
    public static final RegistrySupplier<Block> SCREEN_BLOCK_TIER_2 = BLOCKS.register("screen2", () -> new ScreenBlock(DEFAULT_BLOCK_PROPERTIES, 2));
    public static final RegistrySupplier<Block> SCREEN_BLOCK_TIER_3 = BLOCKS.register("screen3", () -> new ScreenBlock(DEFAULT_BLOCK_PROPERTIES, 3));
    public static final RegistrySupplier<Block> KEYBOARD_BLOCK = BLOCKS.register("keyboard", () -> new KeyboardBlock(DEFAULT_BLOCK_PROPERTIES.noCollission()));

    // Items
    public static final RegistrySupplier<Item> CASE_BLOCK_ITEM_TIER_1 = ITEMS.register("case1", () -> new BlockItem(CASE_BLOCK_TIER_1.value(), DEFAULT_ITEM_PROPERTIES));
    public static final RegistrySupplier<Item> CASE_BLOCK_ITEM_TIER_2 = ITEMS.register("case2", () -> new BlockItem(CASE_BLOCK_TIER_2.value(), DEFAULT_ITEM_PROPERTIES));
    public static final RegistrySupplier<Item> CASE_BLOCK_ITEM_TIER_3 = ITEMS.register("case3", () -> new BlockItem(CASE_BLOCK_TIER_3.value(), DEFAULT_ITEM_PROPERTIES));
    public static final RegistrySupplier<Item> CASE_BLOCK_ITEM_CREATIVE = ITEMS.register("casecreative", () -> new BlockItem(CASE_BLOCK_CREATIVE.value(), DEFAULT_ITEM_PROPERTIES));

    public static final RegistrySupplier<Item> SCREEN_BLOCK_ITEM_TIER_1 = ITEMS.register("screen1", () -> new BlockItem(SCREEN_BLOCK_TIER_1.value(), DEFAULT_ITEM_PROPERTIES));
    public static final RegistrySupplier<Item> SCREEN_BLOCK_ITEM_TIER_2 = ITEMS.register("screen2", () -> new BlockItem(SCREEN_BLOCK_TIER_2.value(), DEFAULT_ITEM_PROPERTIES));
    public static final RegistrySupplier<Item> SCREEN_BLOCK_ITEM_TIER_3 = ITEMS.register("screen3", () -> new BlockItem(SCREEN_BLOCK_TIER_3.value(), DEFAULT_ITEM_PROPERTIES));

    public static final RegistrySupplier<Item> KEYBOARD_BLOCK_ITEM = ITEMS.register("keyboard", () -> new BlockItem(KEYBOARD_BLOCK.value(), DEFAULT_ITEM_PROPERTIES));

    public static final RegistrySupplier<Item> CPU_TIER_1 = ITEMS.register("cpu1", () -> new CPUItem(DEFAULT_ITEM_PROPERTIES, 1));
    public static final RegistrySupplier<Item> CPU_TIER_2 = ITEMS.register("cpu2", () -> new CPUItem(DEFAULT_ITEM_PROPERTIES, 2));
    public static final RegistrySupplier<Item> CPU_TIER_3 = ITEMS.register("cpu3", () -> new CPUItem(DEFAULT_ITEM_PROPERTIES, 3));

    public static final RegistrySupplier<Item> MEMORY_TIER_1 = ITEMS.register("memory1", () -> new MemoryItem(DEFAULT_ITEM_PROPERTIES, 1));
    public static final RegistrySupplier<Item> MEMORY_TIER_1_5 = ITEMS.register("memory1_5", () -> new MemoryItem(DEFAULT_ITEM_PROPERTIES, 1));
    public static final RegistrySupplier<Item> MEMORY_TIER_2 = ITEMS.register("memory2", () -> new MemoryItem(DEFAULT_ITEM_PROPERTIES, 2));
    public static final RegistrySupplier<Item> MEMORY_TIER_2_5 = ITEMS.register("memory2_5", () -> new MemoryItem(DEFAULT_ITEM_PROPERTIES, 2));
    public static final RegistrySupplier<Item> MEMORY_TIER_3 = ITEMS.register("memory3", () -> new MemoryItem(DEFAULT_ITEM_PROPERTIES, 3));
    public static final RegistrySupplier<Item> MEMORY_TIER_3_5 = ITEMS.register("memory3_5", () -> new MemoryItem(DEFAULT_ITEM_PROPERTIES, 3));

    public static final RegistrySupplier<Item> DATACARD_TIER_1 = ITEMS.register("datacard1", () -> new DataCardItem(DEFAULT_ITEM_PROPERTIES, 1));
    public static final RegistrySupplier<Item> DATACARD_TIER_2 = ITEMS.register("datacard2", () -> new DataCardItem(DEFAULT_ITEM_PROPERTIES, 2));
    public static final RegistrySupplier<Item> DATACARD_TIER_3 = ITEMS.register("datacard3", () -> new DataCardItem(DEFAULT_ITEM_PROPERTIES, 3));
    public static final RegistrySupplier<Item> GRAPHICSCARD_TIER_1 = ITEMS.register("graphicscard1", () -> new GraphicsCardItem(DEFAULT_ITEM_PROPERTIES, 1));
    public static final RegistrySupplier<Item> GRAPHICSCARD_TIER_2 = ITEMS.register("graphicscard2", () -> new GraphicsCardItem(DEFAULT_ITEM_PROPERTIES, 2));
    public static final RegistrySupplier<Item> GRAPHICSCARD_TIER_3 = ITEMS.register("graphicscard3", () -> new GraphicsCardItem(DEFAULT_ITEM_PROPERTIES, 3));

    public static final RegistrySupplier<Item> EEPROM = ITEMS.register("eeprom", () -> new EepromItem(DEFAULT_ITEM_PROPERTIES));
    public static final RegistrySupplier<Item> FLOPPY = ITEMS.register("floppy", () -> new FloppyDiskItem(DEFAULT_ITEM_PROPERTIES));
    public static final RegistrySupplier<Item> HARDDISKDRIVE_TIER_1 = ITEMS.register("harddiskdrive1", () -> new HardDiskItem(DEFAULT_ITEM_PROPERTIES, 1));
    public static final RegistrySupplier<Item> HARDDISKDRIVE_TIER_2 = ITEMS.register("harddiskdrive2", () -> new HardDiskItem(DEFAULT_ITEM_PROPERTIES, 2));
    public static final RegistrySupplier<Item> HARDDISKDRIVE_TIER_3 = ITEMS.register("harddiskdrive3", () -> new HardDiskItem(DEFAULT_ITEM_PROPERTIES, 3));

    public static final RegistrySupplier<Item> FLOPPY_OPENOS = ITEMS.register("floppy_openos", () -> new FloppyDiskItem(DEFAULT_ITEM_PROPERTIES, "loot:openos"));
    public static final RegistrySupplier<Item> FLOPPY_PLAN9K = ITEMS.register("floppy_plan9k", () -> new FloppyDiskItem(DEFAULT_ITEM_PROPERTIES, "loot:plan9k"));

    public static final RegistrySupplier<Item> EEPROM_LUA = ITEMS.register("eeprom_lua", () -> new LuaEepromItem(DEFAULT_ITEM_PROPERTIES));

    // Menu types
    public static final RegistrySupplier<MenuType<CaseMenu>> CASE_MENU_TYPE = MENUS.register("case", () -> MenuRegistry.ofExtended(CaseMenu::new));
    public static final RegistrySupplier<MenuType<ScreenMenu>> SCREEN_MENU_TYPE = MENUS.register("screen", () -> MenuRegistry.ofExtended(ScreenMenu::new));

    // Block entities
    public static final RegistrySupplier<BlockEntityType<CaseBlockEntity>> CASE_BLOCK_ENTITY = BLOCK_ENTITIES.register("case", () -> BlockEntityType.Builder
        .of(CaseBlockEntity::new, CASE_BLOCK_TIER_1.get(), CASE_BLOCK_TIER_2.get(), CASE_BLOCK_TIER_3.get(), CASE_BLOCK_CREATIVE.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<ScreenBlockEntity>> SCREEN_BLOCK_ENTITY = BLOCK_ENTITIES.register("screen", () -> BlockEntityType.Builder
        .of(ScreenBlockEntity::new, SCREEN_BLOCK_TIER_1.get(), SCREEN_BLOCK_TIER_2.get(), SCREEN_BLOCK_TIER_3.get()).build(null));

}
