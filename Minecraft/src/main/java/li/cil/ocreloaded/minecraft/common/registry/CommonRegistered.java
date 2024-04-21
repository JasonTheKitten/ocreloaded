package li.cil.ocreloaded.minecraft.common.registry;

import java.util.List;
import java.util.Set;

import li.cil.ocreloaded.minecraft.common.PlatformSpecific;
import li.cil.ocreloaded.minecraft.common.block.CaseBlock;
import li.cil.ocreloaded.minecraft.common.entity.CaseBlockEntity;
import li.cil.ocreloaded.minecraft.common.item.CPUItem;
import li.cil.ocreloaded.minecraft.common.item.CardItem;
import li.cil.ocreloaded.minecraft.common.item.EepromItem;
import li.cil.ocreloaded.minecraft.common.item.HardDiskItem;
import li.cil.ocreloaded.minecraft.common.item.MemoryItem;
import li.cil.ocreloaded.minecraft.common.menu.CaseMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class CommonRegistered {
    
    private static final Properties DEFAULT_BLOCK_PROPERTIES = Properties.of().strength(2f);

    // Blocks
    public static final Block CASE_BLOCK_TIER_1 = new CaseBlock(DEFAULT_BLOCK_PROPERTIES, 1);
    public static final Block CASE_BLOCK_TIER_2 = new CaseBlock(DEFAULT_BLOCK_PROPERTIES, 2);
    public static final Block CASE_BLOCK_TIER_3 = new CaseBlock(DEFAULT_BLOCK_PROPERTIES, 3);
    public static final Block CASE_BLOCK_CREATIVE = new CaseBlock(DEFAULT_BLOCK_PROPERTIES, 4);

    public static final List<Named<Block>> ALL_BLOCKS = List.of(
        new Named<>(CASE_BLOCK_TIER_1, "case1"),
        new Named<>(CASE_BLOCK_TIER_2, "case2"),
        new Named<>(CASE_BLOCK_TIER_3, "case3"),
        new Named<>(CASE_BLOCK_CREATIVE, "casecreative")
    );

    // Items
    public static final BlockItem CASE_BLOCK_ITEM_TIER_1 = new BlockItem(CASE_BLOCK_TIER_1, new BlockItem.Properties());
    public static final BlockItem CASE_BLOCK_ITEM_TIER_2 = new BlockItem(CASE_BLOCK_TIER_2, new BlockItem.Properties());
    public static final BlockItem CASE_BLOCK_ITEM_TIER_3 = new BlockItem(CASE_BLOCK_TIER_3, new BlockItem.Properties());
    public static final BlockItem CASE_BLOCK_ITEM_CREATIVE = new BlockItem(CASE_BLOCK_CREATIVE, new BlockItem.Properties());
    public static final Item CPU_TIER_1 = new CPUItem(new Item.Properties(), 1);
    public static final Item CPU_TIER_2 = new CPUItem(new Item.Properties(), 2);
    public static final Item CPU_TIER_3 = new CPUItem(new Item.Properties(), 3);
    public static final Item DATACARD_TIER_1 = new CardItem(new Item.Properties(), 1);
    public static final Item DATACARD_TIER_2 = new CardItem(new Item.Properties(), 2);
    public static final Item DATACARD_TIER_3 = new CardItem(new Item.Properties(), 3);
    public static final Item EEPROM = new EepromItem(new Item.Properties());
    public static final Item GRAPHICSCARD_TIER_1 = new CardItem(new Item.Properties(), 1);
    public static final Item GRAPHICSCARD_TIER_2 = new CardItem(new Item.Properties(), 2);
    public static final Item GRAPHICSCARD_TIER_3 = new CardItem(new Item.Properties(), 3);
    public static final Item HARDDISKDRIVE_TIER_1 = new HardDiskItem(new Item.Properties(), 1);
    public static final Item HARDDISKDRIVE_TIER_2 = new HardDiskItem(new Item.Properties(), 2);
    public static final Item HARDDISKDRIVE_TIER_3 = new HardDiskItem(new Item.Properties(), 3);
    public static final Item MEMORY_TIER_1 = new MemoryItem(new Item.Properties(), 1);
    public static final Item MEMORY_TIER_1_5 = new MemoryItem(new Item.Properties(), 1);
    public static final Item MEMORY_TIER_2 = new MemoryItem(new Item.Properties(), 2);
    public static final Item MEMORY_TIER_2_5 = new MemoryItem(new Item.Properties(), 2);
    public static final Item MEMORY_TIER_3 = new MemoryItem(new Item.Properties(), 3);
    public static final Item MEMORY_TIER_3_5 = new MemoryItem(new Item.Properties(), 3);

    public static final List<Named<Item>> ALL_ITEMS = List.of(
        new Named<>(CASE_BLOCK_ITEM_TIER_1, "case1"),
        new Named<>(CASE_BLOCK_ITEM_TIER_2, "case2"),
        new Named<>(CASE_BLOCK_ITEM_TIER_3, "case3"),
        new Named<>(CASE_BLOCK_ITEM_CREATIVE, "casecreative"),
        new Named<>(CPU_TIER_1, "cpu1"),
        new Named<>(CPU_TIER_2, "cpu2"),
        new Named<>(CPU_TIER_3, "cpu3"),
        new Named<>(GRAPHICSCARD_TIER_1, "graphicscard1"),
        new Named<>(GRAPHICSCARD_TIER_2, "graphicscard2"),
        new Named<>(GRAPHICSCARD_TIER_3, "graphicscard3"),
        new Named<>(HARDDISKDRIVE_TIER_1, "harddiskdrive1"),
        new Named<>(HARDDISKDRIVE_TIER_2, "harddiskdrive2"),
        new Named<>(HARDDISKDRIVE_TIER_3, "harddiskdrive3"),
        new Named<>(EEPROM, "eeprom"),
        new Named<>(DATACARD_TIER_1, "datacard1"),
        new Named<>(DATACARD_TIER_2, "datacard2"),
        new Named<>(DATACARD_TIER_3, "datacard3"),
        new Named<>(MEMORY_TIER_1, "memory1"),
        new Named<>(MEMORY_TIER_1_5, "memory1_5"),
        new Named<>(MEMORY_TIER_2, "memory2"),
        new Named<>(MEMORY_TIER_2_5, "memory2_5"),
        new Named<>(MEMORY_TIER_3, "memory3"),
        new Named<>(MEMORY_TIER_3_5, "memory3_5")
    );

    // Menu types
    public static final MenuType<CaseMenu> CASE_MENU_TYPE = PlatformSpecific.get().createMenuTypeWithData(CaseMenu::new);

    public static final List<Named<MenuType<?>>> ALL_MENU_TYPES = List.of(
        new Named<>(CASE_MENU_TYPE, "case")
    );

    // Block entities
    public static final BlockEntityType<CaseBlockEntity> CASE_BLOCK_ENTITY = PlatformSpecific.get().createBlockEntityType(CaseBlockEntity::new, Set.of(
        CASE_BLOCK_TIER_1, CASE_BLOCK_TIER_2, CASE_BLOCK_TIER_3, CASE_BLOCK_CREATIVE));


    public static final List<Named<BlockEntityType<?>>> ALL_BLOCK_ENTITIES = List.of(
        new Named<>(CASE_BLOCK_ENTITY, "case")
    );

}
