package li.cil.ocreloaded.minecraft.common.registry;

import java.util.List;

import li.cil.ocreloaded.minecraft.common.PlatformSpecific;
import li.cil.ocreloaded.minecraft.common.block.CaseBlock;
import li.cil.ocreloaded.minecraft.common.item.EepromItem;
import li.cil.ocreloaded.minecraft.common.menu.CaseMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class CommonRegistered {
    
    private static final Properties DEFAULT_BLOCK_PROPERTIES = Properties.of().strength(2f);

    public static final Block CASE_BLOCK = new CaseBlock(DEFAULT_BLOCK_PROPERTIES);

    public static final BlockItem CASE_BLOCK_ITEM = new BlockItem(CASE_BLOCK, new BlockItem.Properties());

    public static final Item EEPROM = new EepromItem(new Item.Properties());

    public static final MenuType<CaseMenu> CASE_MENU_TYPE = PlatformSpecific.get().createMenuTypeWithData(CaseMenu::new);

    public static final List<Named<Block>> ALL_BLOCKS = List.of(
        new Named<>(CASE_BLOCK, "case")
    );

    public static final List<Named<Item>> ALL_ITEMS = List.of(
        new Named<>(CASE_BLOCK_ITEM, "case"),
        new Named<>(EEPROM, "eeprom")
    );

    public static final List<Named<MenuType<?>>> ALL_MENU_TYPES = List.of(
        new Named<>(CASE_MENU_TYPE, "case")
    );

}
