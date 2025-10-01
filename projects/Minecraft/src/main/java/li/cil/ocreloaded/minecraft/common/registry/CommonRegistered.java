package li.cil.ocreloaded.minecraft.common.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import li.cil.ocreloaded.minecraft.common.OCReloadedCommon;
import li.cil.ocreloaded.minecraft.common.block.CaseBlock;
import li.cil.ocreloaded.minecraft.common.block.KeyboardBlock;
import li.cil.ocreloaded.minecraft.common.block.ScreenBlock;
import li.cil.ocreloaded.minecraft.common.entity.CaseBlockEntity;
import li.cil.ocreloaded.minecraft.common.entity.KeyboardBlockEntity;
import li.cil.ocreloaded.minecraft.common.entity.ScreenBlockEntity;
import li.cil.ocreloaded.minecraft.common.item.CPUItem;
import li.cil.ocreloaded.minecraft.common.item.DataCardItem;
import li.cil.ocreloaded.minecraft.common.item.EepromItem;
import li.cil.ocreloaded.minecraft.common.item.FloppyDiskItem;
import li.cil.ocreloaded.minecraft.common.item.GraphicsCardItem;
import li.cil.ocreloaded.minecraft.common.item.HardDiskItem;
import li.cil.ocreloaded.minecraft.common.item.LuaEepromItem;
import li.cil.ocreloaded.minecraft.common.item.ManualItem;
import li.cil.ocreloaded.minecraft.common.item.MemoryItem;
import li.cil.ocreloaded.minecraft.common.menu.CaseMenu;
import li.cil.ocreloaded.minecraft.common.menu.ScreenMenu;
import li.cil.ocreloaded.minecraft.common.network.NetworkUtil;
import li.cil.ocreloaded.minecraft.registration.RegistrationProvider;
import li.cil.ocreloaded.minecraft.registration.RegistryObject;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

@SuppressWarnings("unused")
public class CommonRegistered {

    private static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.get(Registries.BLOCK, OCReloadedCommon.MOD_ID);
    private static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registries.ITEM, OCReloadedCommon.MOD_ID);
    private static final RegistrationProvider<MenuType<?>> MENUS = RegistrationProvider.get(Registries.MENU, OCReloadedCommon.MOD_ID);
    private static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistrationProvider.get(Registries.BLOCK_ENTITY_TYPE, OCReloadedCommon.MOD_ID);
    private static final RegistrationProvider<DataComponentType<?>> DATA_COMPONENT_TYPES = RegistrationProvider.get(Registries.DATA_COMPONENT_TYPE, OCReloadedCommon.MOD_ID);
    private static final RegistrationProvider<CreativeModeTab> CREATIVE_TABS = RegistrationProvider.get(Registries.CREATIVE_MODE_TAB, OCReloadedCommon.MOD_ID);

    private static final List<RegistryObject<Item, Item>> CREATIVE_TAB_ITEMS = new ArrayList<>();

    public static void initialize() {
        NetworkUtil.initialize();

        /*NetworkUtil.getInstance().registerHandler("ocreloaded:power", new PowerNetworkHandler());
        NetworkUtil.getInstance().registerHandler("ocreloaded:screen", new ScreenNetworkHandler());
        NetworkUtil.getInstance().registerHandler("ocreloaded:sound", new SoundNetworkHandler());*/
    }

    // Creative mode tabs
    public static final RegistryObject<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = CREATIVE_TABS.register("creative", () -> IPlatformRegistryHelper.INSTANCE.constructTabBuilder()
            .title(Component.translatable("title.ocreloaded"))
            .icon(() -> new ItemStack(CommonRegistered.CASE_BLOCK_ITEM_TIER_1.get()))
            .displayItems((parameters, output) -> {
                // TODO: Order items like they previously were
                for (RegistryObject<Item, ? extends Item> entry : CREATIVE_TAB_ITEMS) {
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
    public static final RegistryObject<Item, Item> CASE_BLOCK_ITEM_TIER_1 = registerItem("case1", () -> new BlockItem(CASE_BLOCK_TIER_1.get(), DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> CASE_BLOCK_ITEM_TIER_2 = registerItem("case2", () -> new BlockItem(CASE_BLOCK_TIER_2.get(), DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> CASE_BLOCK_ITEM_TIER_3 = registerItem("case3", () -> new BlockItem(CASE_BLOCK_TIER_3.get(), DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> CASE_BLOCK_ITEM_CREATIVE = registerItem("casecreative", () -> new BlockItem(CASE_BLOCK_CREATIVE.get(), DEFAULT_ITEM_PROPERTIES));

    public static final RegistryObject<Item, Item> KEYBOARD_BLOCK_ITEM = registerItem("keyboard", () -> new BlockItem(KEYBOARD_BLOCK.get(), DEFAULT_ITEM_PROPERTIES));

    public static final RegistryObject<Item, Item> SCREEN_BLOCK_ITEM_TIER_1 = registerItem("screen1", () -> new BlockItem(SCREEN_BLOCK_TIER_1.get(), DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> SCREEN_BLOCK_ITEM_TIER_2 = registerItem("screen2", () -> new BlockItem(SCREEN_BLOCK_TIER_2.get(), DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> SCREEN_BLOCK_ITEM_TIER_3 = registerItem("screen3", () -> new BlockItem(SCREEN_BLOCK_TIER_3.get(), DEFAULT_ITEM_PROPERTIES));

    public static final RegistryObject<Item, Item> ARITHMETIC_LOGIC_UNIT = registerItem("alu", () -> new Item(DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> ARROW_KEYS = registerItem("arrow_keys", () -> new Item(DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> BUTTON_GROUP = registerItem("button_group", () -> new Item(DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> CARD = registerItem("card", () -> new Item(DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> CONTROL_UNIT = registerItem("cu", () -> new Item(DEFAULT_ITEM_PROPERTIES));

    public static final RegistryObject<Item, Item> CUTTING_WIRE = registerItem("cutting_wire", () -> new Item(DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> CHIP_DIAMOND = registerItem("chip_diamond", () -> new Item(DEFAULT_ITEM_PROPERTIES));

    public static final RegistryObject<Item, Item> DISK = registerItem("disk", () -> new Item(DEFAULT_ITEM_PROPERTIES));

    public static final RegistryObject<Item, Item> CHIP_TIER_1 = registerItem("chip1", () -> new CPUItem(DEFAULT_ITEM_PROPERTIES, 1));
    public static final RegistryObject<Item, Item> CHIP_TIER_2 = registerItem("chip2", () -> new CPUItem(DEFAULT_ITEM_PROPERTIES, 2));
    public static final RegistryObject<Item, Item> CHIP_TIER_3 = registerItem("chip3", () -> new CPUItem(DEFAULT_ITEM_PROPERTIES, 3));

    public static final RegistryObject<Item, Item> NUMPAD = registerItem("numpad", () -> new Item(DEFAULT_ITEM_PROPERTIES));

    public static final RegistryObject<Item, Item> PRINTED_CIRCUIT_BOARD = registerItem("printed_circuit_board", () -> new Item(DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> RAW_CIRCUIT_BOARD = registerItem("raw_circuit_board", () -> new Item(DEFAULT_ITEM_PROPERTIES));

    public static final RegistryObject<Item, Item> TRANSISTOR = registerItem("transistor", () -> new Item(DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> MANUAL = registerItem("manual", () -> new ManualItem(DEFAULT_ITEM_PROPERTIES));

    public static final RegistryObject<Item, Item> CPU_TIER_1 = registerItem("cpu1", () -> new CPUItem(DEFAULT_ITEM_PROPERTIES, 1));
    public static final RegistryObject<Item, Item> CPU_TIER_2 = registerItem("cpu2", () -> new CPUItem(DEFAULT_ITEM_PROPERTIES, 2));
    public static final RegistryObject<Item, Item> CPU_TIER_3 = registerItem("cpu3", () -> new CPUItem(DEFAULT_ITEM_PROPERTIES, 3));

    public static final RegistryObject<Item, Item> MEMORY_TIER_1 = registerItem("memory1", () -> new MemoryItem(DEFAULT_ITEM_PROPERTIES, 1));
    public static final RegistryObject<Item, Item> MEMORY_TIER_1_5 = registerItem("memory1_5", () -> new MemoryItem(DEFAULT_ITEM_PROPERTIES, 1));
    public static final RegistryObject<Item, Item> MEMORY_TIER_2 = registerItem("memory2", () -> new MemoryItem(DEFAULT_ITEM_PROPERTIES, 2));
    public static final RegistryObject<Item, Item> MEMORY_TIER_2_5 = registerItem("memory2_5", () -> new MemoryItem(DEFAULT_ITEM_PROPERTIES, 2));
    public static final RegistryObject<Item, Item> MEMORY_TIER_3 = registerItem("memory3", () -> new MemoryItem(DEFAULT_ITEM_PROPERTIES, 3));
    public static final RegistryObject<Item, Item> MEMORY_TIER_3_5 = registerItem("memory3_5", () -> new MemoryItem(DEFAULT_ITEM_PROPERTIES, 3));

    public static final RegistryObject<Item, Item> DATACARD_TIER_1 = registerItem("datacard1", () -> new DataCardItem(DEFAULT_ITEM_PROPERTIES, 1));
    public static final RegistryObject<Item, Item> DATACARD_TIER_2 = registerItem("datacard2", () -> new DataCardItem(DEFAULT_ITEM_PROPERTIES, 2));
    public static final RegistryObject<Item, Item> DATACARD_TIER_3 = registerItem("datacard3", () -> new DataCardItem(DEFAULT_ITEM_PROPERTIES, 3));
    public static final RegistryObject<Item, Item> GRAPHICSCARD_TIER_1 = registerItem("graphicscard1", () -> new GraphicsCardItem(DEFAULT_ITEM_PROPERTIES, 1));
    public static final RegistryObject<Item, Item> GRAPHICSCARD_TIER_2 = registerItem("graphicscard2", () -> new GraphicsCardItem(DEFAULT_ITEM_PROPERTIES, 2));
    public static final RegistryObject<Item, Item> GRAPHICSCARD_TIER_3 = registerItem("graphicscard3", () -> new GraphicsCardItem(DEFAULT_ITEM_PROPERTIES, 3));

    public static final RegistryObject<Item, Item> EEPROM = registerItem("eeprom", () -> new EepromItem(DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> FLOPPY = registerItem("floppy", () -> new FloppyDiskItem(DEFAULT_ITEM_PROPERTIES));
    public static final RegistryObject<Item, Item> HARDDISKDRIVE_TIER_1 = registerItem("harddiskdrive1", () -> new HardDiskItem(DEFAULT_ITEM_PROPERTIES, 1));
    public static final RegistryObject<Item, Item> HARDDISKDRIVE_TIER_2 = registerItem("harddiskdrive2", () -> new HardDiskItem(DEFAULT_ITEM_PROPERTIES, 2));
    public static final RegistryObject<Item, Item> HARDDISKDRIVE_TIER_3 = registerItem("harddiskdrive3", () -> new HardDiskItem(DEFAULT_ITEM_PROPERTIES, 3));

    public static final RegistryObject<Item, Item> FLOPPY_OPENOS = registerItem("floppy_openos", () -> new FloppyDiskItem(DEFAULT_ITEM_PROPERTIES, "loot:openos"));
    public static final RegistryObject<Item, Item> FLOPPY_PLAN9K = registerItem("floppy_plan9k", () -> new FloppyDiskItem(DEFAULT_ITEM_PROPERTIES, "loot:plan9k"));

    public static final RegistryObject<Item, Item> EEPROM_LUA = registerItem("eeprom_lua", () -> new LuaEepromItem(DEFAULT_ITEM_PROPERTIES));

    // Menu types
    public static final RegistryObject<MenuType<?>, MenuType<CaseMenu>> CASE_MENU_TYPE = MENUS.register("case", () -> IPlatformRegistryHelper.INSTANCE.registerMenuType(CaseMenu::new));
    public static final RegistryObject<MenuType<?>, MenuType<ScreenMenu>> SCREEN_MENU_TYPE = MENUS.register("screen", () -> IPlatformRegistryHelper.INSTANCE.registerMenuType(ScreenMenu::new));

    // Block entities
    public static final RegistryObject<BlockEntityType<?>, BlockEntityType<CaseBlockEntity>> CASE_BLOCK_ENTITY = BLOCK_ENTITIES.register("case", () -> IPlatformRegistryHelper.INSTANCE.createBlockEntityType(CaseBlockEntity::new, CASE_BLOCK_TIER_1.get(), CASE_BLOCK_TIER_2.get(), CASE_BLOCK_TIER_3.get(), CASE_BLOCK_CREATIVE.get()));
    public static final RegistryObject<BlockEntityType<?>, BlockEntityType<ScreenBlockEntity>> SCREEN_BLOCK_ENTITY = BLOCK_ENTITIES.register("screen", () -> IPlatformRegistryHelper.INSTANCE.createBlockEntityType(ScreenBlockEntity::new, SCREEN_BLOCK_TIER_1.get(), SCREEN_BLOCK_TIER_2.get(), SCREEN_BLOCK_TIER_3.get()));
    public static final RegistryObject<BlockEntityType<?>, BlockEntityType<KeyboardBlockEntity>> KEYBOARD_BLOCK_ENTITY = BLOCK_ENTITIES.register("keyboard", () -> IPlatformRegistryHelper.INSTANCE.createBlockEntityType(KeyboardBlockEntity::new, KEYBOARD_BLOCK.get()));

    // Data component types
    public static final RegistryObject<DataComponentType<?>, DataComponentType<CompoundTag>> NBT_DATA_TYPE = DATA_COMPONENT_TYPES.register("custom_nbt", () -> DataComponentType.<CompoundTag>builder().persistent(CompoundTag.CODEC).build());

    private static RegistryObject<Item, Item> registerItem(String name, Supplier<? extends Item> supplier) {
        RegistryObject<Item, Item> registryObject = ITEMS.register(name, supplier);
        CREATIVE_TAB_ITEMS.add(registryObject);
        return registryObject;
    }

}
