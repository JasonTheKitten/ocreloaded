package li.cil.ocreloaded.minecraft.common.menu;

import javax.annotation.Nonnull;

import li.cil.ocreloaded.minecraft.common.block.ScreenBlock;
import li.cil.ocreloaded.minecraft.common.entity.ScreenBlockEntity;
import li.cil.ocreloaded.minecraft.common.network.NetworkUtil;
import li.cil.ocreloaded.minecraft.common.network.screen.ScreenNetworkInputMessages;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class ScreenMenu extends AbstractContainerMenu {

    private final ScreenBlockEntity blockEntity;
    private final Inventory inventory;

    public ScreenMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        super(CommonRegistered.SCREEN_MENU_TYPE.get(), id);

        this.blockEntity = (ScreenBlockEntity) inventory.player.level().getBlockEntity(data.readBlockPos());
        this.inventory = inventory;
    }

    @Override
    public ItemStack quickMoveStack(@Nonnull Player var1, int var2) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@Nonnull Player var1) {
        return inventory.stillValid(var1);
    }

    public ScreenBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public void onKeyPressed(int charCode, int keyCode) {
        NetworkUtil.getInstance().messageServer(
            ScreenNetworkInputMessages.createKeyPressedMessage(blockEntity.getBlockPos(), charCode, keyCode));
    }
    
    public void onKeyReleased(int keyCode) {
        NetworkUtil.getInstance().messageServer(
            ScreenNetworkInputMessages.createKeyReleasedMessage(blockEntity.getBlockPos(), keyCode));
    }

    public void onMousePressed(int button, double x, double y) {
        if (!allowMouseEvents()) return;
        NetworkUtil.getInstance().messageServer(
            ScreenNetworkInputMessages.createMouseMessage(ScreenNetworkInputMessages.MOUSE_PRESSED, blockEntity.getBlockPos(), button, x, y));
    }

    public void onMouseReleased(int button, double x, double y) {
        if (!allowMouseEvents()) return;
        NetworkUtil.getInstance().messageServer(
            ScreenNetworkInputMessages.createMouseMessage(ScreenNetworkInputMessages.MOUSE_RELEASED, blockEntity.getBlockPos(), button, x, y));
    }

    public void onMouseDragged(int button, double x, double y) {
        if (!allowMouseEvents()) return;
        NetworkUtil.getInstance().messageServer(
            ScreenNetworkInputMessages.createMouseMessage(ScreenNetworkInputMessages.MOUSE_DRAGGED, blockEntity.getBlockPos(), button, x, y));
    }

    public void onMouseScrolled(int button, double x, double y) {
        if (!allowMouseEvents()) return;
        NetworkUtil.getInstance().messageServer(
            ScreenNetworkInputMessages.createMouseMessage(ScreenNetworkInputMessages.MOUSE_SCROLLED, blockEntity.getBlockPos(), button, x, y));
    }

    private boolean allowMouseEvents() {
        return
            blockEntity.getBlockState().getBlock() instanceof ScreenBlock screenBlock
            && screenBlock.getTier() > 1;
    }


    public void onKeyboardClipboard(String clipboard) {
        NetworkUtil.getInstance().messageServer(
                ScreenNetworkInputMessages.createClipboardPasteMessage(blockEntity.getBlockPos(), clipboard));
    }
    
}
