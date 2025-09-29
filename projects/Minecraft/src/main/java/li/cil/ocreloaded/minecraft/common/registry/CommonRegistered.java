package li.cil.ocreloaded.minecraft.common.registry;

import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import li.cil.ocreloaded.minecraft.common.block.CaseBlock;
import li.cil.ocreloaded.minecraft.common.block.KeyboardBlock;
import li.cil.ocreloaded.minecraft.common.block.ScreenBlock;
import li.cil.ocreloaded.minecraft.common.entity.CaseBlockEntity;
import li.cil.ocreloaded.minecraft.common.entity.KeyboardBlockEntity;
import li.cil.ocreloaded.minecraft.common.entity.ScreenBlockEntity;
import li.cil.ocreloaded.minecraft.common.item.*;
import li.cil.ocreloaded.minecraft.common.menu.CaseMenu;
import li.cil.ocreloaded.minecraft.common.menu.ScreenMenu;
import li.cil.ocreloaded.minecraft.common.network.NetworkUtil;
import li.cil.ocreloaded.minecraft.common.network.power.PowerNetworkHandler;
import li.cil.ocreloaded.minecraft.common.network.screen.ScreenNetworkHandler;
import li.cil.ocreloaded.minecraft.common.network.sound.SoundNetworkHandler;
import li.cil.ocreloaded.minecraft.registration.RegistrationProvider;
import li.cil.ocreloaded.minecraft.registration.RegistryObject;
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

@SuppressWarnings("unused") // Used in recipes, I think.
public class CommonRegistered {

    private static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.get(Registries.BLOCK, OCReloadedCommon.MOD_ID);
    private static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registries.ITEM, OCReloadedCommon.MOD_ID);
    private static final RegistrationProvider<MenuType<?>> MENUS = RegistrationProvider.get(Registries.MENU, OCReloadedCommon.MOD_ID);
    private static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistrationProvider.get(Registries.BLOCK_ENTITY_TYPE, OCReloadedCommon.MOD_ID);
    private static final RegistrationProvider<CreativeModeTab> CREATIVE_TABS = RegistrationProvider.get(Registries.CREATIVE_MODE_TAB, OCReloadedCommon.MOD_ID);

    public static void initialize() {
        NetworkUtil.getInstance().registerHandler("ocreloaded:power", new PowerNetworkHandler());
        NetworkUtil.getInstance().registerHandler("ocreloaded:screen", new ScreenNetworkHandler());
        NetworkUtil.getInstance().registerHandler("ocreloaded:sound", new SoundNetworkHandler());
    }

    // Creative mode tabs
    public static final RegistryObject<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = CREATIVE_TABS.register("creative", () -> IPlatformRegistryHelper.INSTANCE.constructTabBuilder()
            .title(Component.translatable("title.ocreloaded"))
            .icon(() -> new ItemStack(CommonRegistered.CASE_BLOCK_ITEM_TIER_1.get()))
            .displayItems((parameters, output) -> {
                // TODO: Order items like they previously were
                for (RegistryObject<Item, ? extends Item> entry : ITEMS.getEntries()) {
                    output.accept(entry.get());
                }
            })
            .build());

    // Default properties
    private static final BlockBehaviour.Properties DEFAULT_BLOCK_PROPERTIES = BlockBehaviour.Properties.of().strength(2f).noOcclusion();
    private static final Item.Properties DEFAULT_ITEM_PROPERTIES = new Item.Properties();//.arch$tab(CREATIVE_TAB);

    // Blocks
    public static final RegistryObject<Block, CaseBlock> CASE_BLOCK_TIER_1 = BLOCKS.register("case1", () -> new CaseBlock(DEFAULT_BLOCK_PROPERTIES.strength(2.5f), 1));
    public static final RegistryObject<Block, CaseBlock> CASE_BLOCK_TIER_2 = BLOCKS.register("case2", () -> new CaseBlock(DEFAULT_BLOCK_PROPERTIES.strength(2.5f), 2));
    public static final RegistryObject<Block, CaseBlock> CASE_BLOCK_TIER_3 = BLOCKS.register("case3", () -> new CaseBlock(DEFAULT_BLOCK_PROPERTIES, 3));
    public static final RegistryObject<Block, CaseBlock> CASE_BLOCK_CREATIVE = BLOCKS.register("casecreative", () -> new CaseBlock(DEFAULT_BLOCK_PROPERTIES, 4));
    public static final RegistryObject<Block, ScreenBlock> SCREEN_BLOCK_TIER_1 = BLOCKS.register("screen1", () -> new ScreenBlock(DEFAULT_BLOCK_PROPERTIES, 1));
    public static final RegistryObject<Block, ScreenBlock> SCREEN_BLOCK_TIER_2 = BLOCKS.register("screen2", () -> new ScreenBlock(DEFAULT_BLOCK_PROPERTIES, 2));
    public static final RegistryObject<Block, ScreenBlock> SCREEN_BLOCK_TIER_3 = BLOCKS.register("screen3", () -> new ScreenBlock(DEFAULT_BLOCK_PROPERTIES, 3));
    public static final RegistryObject<Block, KeyboardBlock> KEYBOARD_BLOCK = BLOCKS.register("keyboard", () -> new KeyboardBlock(DEFAULT_BLOCK_PROPERTIES.noCollission()));

    // Items
    public static final RegistryObject<Item, Item> CASE_BLOCK_ITEM_TIER_1 = ITEMS.register("case1", () -> new BlockItem(CASE_BLOCK_TIER_1.get(), DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> CASE_BLOCK_ITEM_TIER_2 = ITEMS.register("case2", () -> new BlockItem(CASE_BLOCK_TIER_2.get(), DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> CASE_BLOCK_ITEM_TIER_3 = ITEMS.register("case3", () -> new BlockItem(CASE_BLOCK_TIER_3.get(), DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> CASE_BLOCK_ITEM_CREATIVE = ITEMS.register("casecreative", () -> new BlockItem(CASE_BLOCK_CREATIVE.get(), DEFAULT_ITEM_PROPERTIES));

    public static final RegistryObject<Item, Item> KEYBOARD_BLOCK_ITEM = ITEMS.register("keyboard", () -> new BlockItem(KEYBOARD_BLOCK.get(), DEFAULT_ITEM_PROPERTIES));

    public static final RegistryObject<Item, Item> SCREEN_BLOCK_ITEM_TIER_1 = ITEMS.register("screen1", () -> new BlockItem(SCREEN_BLOCK_TIER_1.get(), DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> SCREEN_BLOCK_ITEM_TIER_2 = ITEMS.register("screen2", () -> new BlockItem(SCREEN_BLOCK_TIER_2.get(), DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> SCREEN_BLOCK_ITEM_TIER_3 = ITEMS.register("screen3", () -> new BlockItem(SCREEN_BLOCK_TIER_3.get(), DEFAULT_ITEM_PROPERTIES));

    public static final RegistryObject<Item, Item> ARITHMETIC_LOGIC_UNIT = ITEMS.register("alu", () -> new Item(DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> ARROW_KEYS = ITEMS.register("arrow_keys", () -> new Item(DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> BUTTON_GROUP = ITEMS.register("button_group", () -> new Item(DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> CARD = ITEMS.register("card", () -> new Item(DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> CONTROL_UNIT = ITEMS.register("cu", () -> new Item(DEFAULT_ITEM_PROPERTIES));

    public static final RegistryObject<Item, Item> CUTTING_WIRE = ITEMS.register("cutting_wire", () -> new Item(DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> CHIP_DIAMOND = ITEMS.register("chip_diamond", () -> new Item(DEFAULT_ITEM_PROPERTIES));

    public static final RegistryObject<Item, Item> DISK = ITEMS.register("disk", () -> new Item(DEFAULT_ITEM_PROPERTIES));

    public static final RegistryObject<Item, Item> CHIP_TIER_1 = ITEMS.register("chip1", () -> new CPUItem(DEFAULT_ITEM_PROPERTIES, 1));
    public static final RegistryObject<Item, Item> CHIP_TIER_2 = ITEMS.register("chip2", () -> new CPUItem(DEFAULT_ITEM_PROPERTIES, 2));
    public static final RegistryObject<Item, Item> CHIP_TIER_3 = ITEMS.register("chip3", () -> new CPUItem(DEFAULT_ITEM_PROPERTIES, 3));

    public static final RegistryObject<Item, Item> NUMPAD = ITEMS.register("numpad", () -> new Item(DEFAULT_ITEM_PROPERTIES));

    public static final RegistryObject<Item, Item> PRINTED_CIRCUIT_BOARD = ITEMS.register("printed_circuit_board", () -> new Item(DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> RAW_CIRCUIT_BOARD = ITEMS.register("raw_circuit_board", () -> new Item(DEFAULT_ITEM_PROPERTIES));

    public static final RegistryObject<Item, Item> TRANSISTOR = ITEMS.register("transistor", () -> new Item(DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> MANUAL = ITEMS.register("manual", () -> new ManualItem(DEFAULT_ITEM_PROPERTIES));

    public static final RegistryObject<Item, Item> CPU_TIER_1 = ITEMS.register("cpu1", () -> new CPUItem(DEFAULT_ITEM_PROPERTIES, 1));
    public static final RegistryObject<Item, Item> CPU_TIER_2 = ITEMS.register("cpu2", () -> new CPUItem(DEFAULT_ITEM_PROPERTIES, 2));
    public static final RegistryObject<Item, Item> CPU_TIER_3 = ITEMS.register("cpu3", () -> new CPUItem(DEFAULT_ITEM_PROPERTIES, 3));

    public static final RegistryObject<Item, Item> MEMORY_TIER_1 = ITEMS.register("memory1", () -> new MemoryItem(DEFAULT_ITEM_PROPERTIES, 1));
    public static final RegistryObject<Item, Item> MEMORY_TIER_1_5 = ITEMS.register("memory1_5", () -> new MemoryItem(DEFAULT_ITEM_PROPERTIES, 1));
    public static final RegistryObject<Item, Item> MEMORY_TIER_2 = ITEMS.register("memory2", () -> new MemoryItem(DEFAULT_ITEM_PROPERTIES, 2));
    public static final RegistryObject<Item, Item> MEMORY_TIER_2_5 = ITEMS.register("memory2_5", () -> new MemoryItem(DEFAULT_ITEM_PROPERTIES, 2));
    public static final RegistryObject<Item, Item> MEMORY_TIER_3 = ITEMS.register("memory3", () -> new MemoryItem(DEFAULT_ITEM_PROPERTIES, 3));
    public static final RegistryObject<Item, Item> MEMORY_TIER_3_5 = ITEMS.register("memory3_5", () -> new MemoryItem(DEFAULT_ITEM_PROPERTIES, 3));

    public static final RegistryObject<Item, Item> DATACARD_TIER_1 = ITEMS.register("datacard1", () -> new DataCardItem(DEFAULT_ITEM_PROPERTIES, 1));
    public static final RegistryObject<Item, Item> DATACARD_TIER_2 = ITEMS.register("datacard2", () -> new DataCardItem(DEFAULT_ITEM_PROPERTIES, 2));
    public static final RegistryObject<Item, Item> DATACARD_TIER_3 = ITEMS.register("datacard3", () -> new DataCardItem(DEFAULT_ITEM_PROPERTIES, 3));
    public static final RegistryObject<Item, Item> GRAPHICSCARD_TIER_1 = ITEMS.register("graphicscard1", () -> new GraphicsCardItem(DEFAULT_ITEM_PROPERTIES, 1));
    public static final RegistryObject<Item, Item> GRAPHICSCARD_TIER_2 = ITEMS.register("graphicscard2", () -> new GraphicsCardItem(DEFAULT_ITEM_PROPERTIES, 2));
    public static final RegistryObject<Item, Item> GRAPHICSCARD_TIER_3 = ITEMS.register("graphicscard3", () -> new GraphicsCardItem(DEFAULT_ITEM_PROPERTIES, 3));

    public static final RegistryObject<Item, Item> EEPROM = ITEMS.register("eeprom", () -> new EepromItem(DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> FLOPPY = ITEMS.register("floppy", () -> new FloppyDiskItem(DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> HARDDISKDRIVE_TIER_1 = ITEMS.register("harddiskdrive1", () -> new HardDiskItem(DEFAULT_ITEM_PROPERTIES, 1));
    public static final RegistryObject<Item, Item> HARDDISKDRIVE_TIER_2 = ITEMS.register("harddiskdrive2", () -> new HardDiskItem(DEFAULT_ITEM_PROPERTIES, 2));
    public static final RegistryObject<Item, Item> HARDDISKDRIVE_TIER_3 = ITEMS.register("harddiskdrive3", () -> new HardDiskItem(DEFAULT_ITEM_PROPERTIES, 3));

    public static final RegistryObject<Item, Item> FLOPPY_OPENOS = ITEMS.register("floppy_openos", () -> new FloppyDiskItem(DEFAULT_ITEM_PROPERTIES, "loot:openos"));
    public static final RegistryObject<Item, Item> FLOPPY_PLAN9K = ITEMS.register("floppy_plan9k", () -> new FloppyDiskItem(DEFAULT_ITEM_PROPERTIES, "loot:plan9k"));

    public static final RegistryObject<Item, Item> EEPROM_LUA = ITEMS.register("eeprom_lua", () -> new LuaEepromItem(DEFAULT_ITEM_PROPERTIES));

    // Menu types
    public static final RegistryObject<MenuType<?>, MenuType<CaseMenu>> CASE_MENU_TYPE = MENUS.register("case", () -> IPlatformRegistryHelper.INSTANCE.registerMenuType(CaseMenu::new));
    public static final RegistryObject<MenuType<?>, MenuType<ScreenMenu>> SCREEN_MENU_TYPE = MENUS.register("screen", () -> IPlatformRegistryHelper.INSTANCE.registerMenuType(ScreenMenu::new));

    // Block entities
    public static final RegistryObject<BlockEntityType<?>, BlockEntityType<CaseBlockEntity>> CASE_BLOCK_ENTITY = BLOCK_ENTITIES.register("case", () -> IPlatformRegistryHelper.INSTANCE.createBlockEntityType(CaseBlockEntity::new, CASE_BLOCK_TIER_1.get(), CASE_BLOCK_TIER_2.get(), CASE_BLOCK_TIER_3.get(), CASE_BLOCK_CREATIVE.get()));
    public static final RegistryObject<BlockEntityType<?>, BlockEntityType<ScreenBlockEntity>> SCREEN_BLOCK_ENTITY = BLOCK_ENTITIES.register("screen", () -> IPlatformRegistryHelper.INSTANCE.createBlockEntityType(ScreenBlockEntity::new, SCREEN_BLOCK_TIER_1.get(), SCREEN_BLOCK_TIER_2.get(), SCREEN_BLOCK_TIER_3.get()));
    public static final RegistryObject<BlockEntityType<?>, BlockEntityType<KeyboardBlockEntity>> KEYBOARD_BLOCK_ENTITY = BLOCK_ENTITIES.register("keyboard", () -> IPlatformRegistryHelper.INSTANCE.createBlockEntityType(KeyboardBlockEntity::new, KEYBOARD_BLOCK.get()));

}
