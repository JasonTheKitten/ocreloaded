package li.cil.ocreloaded.minecraft.common.menu;

import java.util.List;

import javax.annotation.Nonnull;

import li.cil.ocreloaded.minecraft.common.assets.SharedTextures;
import li.cil.ocreloaded.minecraft.common.container.BasicContainer;
import li.cil.ocreloaded.minecraft.common.entity.CaseBlockEntity;
import li.cil.ocreloaded.minecraft.common.item.CardItem;
import li.cil.ocreloaded.minecraft.common.item.EepromItem;
import li.cil.ocreloaded.minecraft.common.item.FloppyDiskItem;
import li.cil.ocreloaded.minecraft.common.item.HardDiskItem;
import li.cil.ocreloaded.minecraft.common.item.MemoryItem;
import li.cil.ocreloaded.minecraft.common.item.ProcessorProviderItem;
import li.cil.ocreloaded.minecraft.common.item.SlotItem;
import li.cil.ocreloaded.minecraft.common.network.IPlatformNetworkHelper;
import li.cil.ocreloaded.minecraft.common.network.packets.PowerPacket;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CaseMenu extends AbstractContainerMenu {

    private static final List<List<ComponentSlotEntry>> COMPONENT_SLOTS = List.of(
        List.of(
            new ComponentSlotEntry(0, 0, CardItem.class, SharedTextures.CARD_ICON, 1),
            new ComponentSlotEntry(0, 1, CardItem.class, SharedTextures.CARD_ICON, 1),
            new ComponentSlotEntry(1, 0, ProcessorProviderItem.class, SharedTextures.CPU_ICON, 1),
            new ComponentSlotEntry(1, 1, MemoryItem.class, SharedTextures.MEMORY_ICON, 1),
            new ComponentSlotEntry(1, 2, MemoryItem.class, SharedTextures.MEMORY_ICON, 1),
            new ComponentSlotEntry(2, 0, HardDiskItem.class, SharedTextures.HDD_ICON, 1)
        ),
        List.of(
            new ComponentSlotEntry(0, 0, CardItem.class, SharedTextures.CARD_ICON, 2),
            new ComponentSlotEntry(0, 1, CardItem.class, SharedTextures.CARD_ICON, 1),
            new ComponentSlotEntry(1, 0, ProcessorProviderItem.class, SharedTextures.CPU_ICON, 2),
            new ComponentSlotEntry(1, 1, MemoryItem.class, SharedTextures.MEMORY_ICON, 2),
            new ComponentSlotEntry(1, 2, MemoryItem.class, SharedTextures.MEMORY_ICON, 2),
            new ComponentSlotEntry(2, 0, HardDiskItem.class, SharedTextures.HDD_ICON, 2),
            new ComponentSlotEntry(2, 1, HardDiskItem.class, SharedTextures.HDD_ICON, 1)
        ),
        List.of(
            new ComponentSlotEntry(0, 0, CardItem.class, SharedTextures.CARD_ICON, 3),
            new ComponentSlotEntry(0, 1, CardItem.class, SharedTextures.CARD_ICON, 2),
            new ComponentSlotEntry(0, 2, CardItem.class, SharedTextures.CARD_ICON, 2),
            new ComponentSlotEntry(1, 0, ProcessorProviderItem.class, SharedTextures.CPU_ICON, 3),
            new ComponentSlotEntry(1, 1, MemoryItem.class, SharedTextures.MEMORY_ICON, 3),
            new ComponentSlotEntry(1, 2, MemoryItem.class, SharedTextures.MEMORY_ICON, 3),
            new ComponentSlotEntry(2, 0, HardDiskItem.class, SharedTextures.HDD_ICON, 3),
            new ComponentSlotEntry(2, 1, HardDiskItem.class, SharedTextures.HDD_ICON, 2),
            new ComponentSlotEntry(2, 2, FloppyDiskItem.class, SharedTextures.FLOPPY_ICON, 1)
        ),
        List.of(
            new ComponentSlotEntry(0, 0, CardItem.class, SharedTextures.CARD_ICON, 3),
            new ComponentSlotEntry(0, 1, CardItem.class, SharedTextures.CARD_ICON, 3),
            new ComponentSlotEntry(0, 2, CardItem.class, SharedTextures.CARD_ICON, 3),
            new ComponentSlotEntry(1, 0, ProcessorProviderItem.class, SharedTextures.CPU_ICON, 3),
            new ComponentSlotEntry(1, 1, MemoryItem.class, SharedTextures.MEMORY_ICON, 3),
            new ComponentSlotEntry(1, 2, MemoryItem.class, SharedTextures.MEMORY_ICON, 3),
            new ComponentSlotEntry(2, 0, HardDiskItem.class, SharedTextures.HDD_ICON, 3),
            new ComponentSlotEntry(2, 1, HardDiskItem.class, SharedTextures.HDD_ICON, 3),
            new ComponentSlotEntry(2, 2, FloppyDiskItem.class, SharedTextures.FLOPPY_ICON, 1)
        )
    );

    private final CaseBlockEntity blockEntity;
    private final Inventory inventory;
    private final Container container;
    private final DataSlot power;

    private final int tier;

    public CaseMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        super(CommonRegistered.CASE_MENU_TYPE.get(), id);

        this.blockEntity = (CaseBlockEntity) inventory.player.level().getBlockEntity(data.readBlockPos());
        this.inventory = inventory;
        this.container = new BasicContainer(blockEntity.getItems(), () -> blockEntity.setChanged());
        this.power = addDataSlot(DataSlot.standalone());
        this.tier = data.readInt();

        power.set(blockEntity.isPowered() ? 1 : 0);

        addContainerSlots();
        addInventorySlots();
        addHotbarSlots();
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return inventory.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        return ComponentQuickMove.quickMoveStack(slots, player, index);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        int newPower = blockEntity.isPowered() ? 1 : 0;
        power.checkAndClearUpdateFlag();
        if (power.get() != newPower) {
            power.set(newPower);
        }
    }

    public DataSlot getPower() {
        return power;
    }

    public void sendServerPowerState() {
        BlockPos targetBlockPos = blockEntity.getBlockPos();
        IPlatformNetworkHelper.INSTANCE.sendToServer(new PowerPacket(targetBlockPos, power.get() == 1));
    }

    private void addContainerSlots() {
        this.addSlot(new ComponentSlot(container, 0, 48, 34, EepromItem.class, SharedTextures.EEPROM_ICON));

        List<ComponentSlotEntry> componentSlots = COMPONENT_SLOTS.get(tier - 1);
        for (ComponentSlotEntry entry : componentSlots) {
            addComponentSlot(entry);
        }
    }

    private void addInventorySlots() {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(inventory, x + (1 + y) * 9, 8 + x * 18, 84 + y * 18));
            }
        }
    }

    private void addHotbarSlots() {
        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(inventory, x, 8 + x * 18, 142));
        }
    }

    private void addComponentSlot(ComponentSlotEntry entry) {
        int x = entry.x();
        int y = entry.y();
        this.addSlot(new ComponentSlot(
            container, 1 + x + y * 3,
            98 + x * 22, 17 + y * 18,
            entry.itemClass(), entry.texture(),
            entry.teir()));
    }

    private record ComponentSlotEntry(int x, int y, Class<? extends SlotItem> itemClass, ResourceLocation texture, int teir) { }
    
}
