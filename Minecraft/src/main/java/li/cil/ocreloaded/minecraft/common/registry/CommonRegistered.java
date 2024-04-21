package li.cil.ocreloaded.minecraft.common.registry;

import java.util.List;
import java.util.Set;

import li.cil.ocreloaded.minecraft.common.PlatformSpecific;
import li.cil.ocreloaded.minecraft.common.block.CaseBlock;
import li.cil.ocreloaded.minecraft.common.entity.CaseBlockEntity;
import li.cil.ocreloaded.minecraft.common.item.EepromItem;
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
    public static final Item EEPROM = new EepromItem(new Item.Properties());

    public static final List<Named<Item>> ALL_ITEMS = List.of(
        new Named<>(CASE_BLOCK_ITEM_TIER_1, "case1"),
        new Named<>(CASE_BLOCK_ITEM_TIER_2, "case2"),
        new Named<>(CASE_BLOCK_ITEM_TIER_3, "case3"),
        new Named<>(CASE_BLOCK_ITEM_CREATIVE, "casecreative"),
        new Named<>(EEPROM, "eeprom")
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
