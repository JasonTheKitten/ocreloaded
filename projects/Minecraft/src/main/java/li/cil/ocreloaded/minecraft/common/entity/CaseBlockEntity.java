package li.cil.ocreloaded.minecraft.common.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.Unpooled;
import li.cil.ocreloaded.core.component.ComputerComponent;
import li.cil.ocreloaded.core.component.FileSystemComponent;
import li.cil.ocreloaded.core.filesystem.InMemoryFileSystem;
import li.cil.ocreloaded.core.machine.Machine;
import li.cil.ocreloaded.core.machine.MachineBuilder;
import li.cil.ocreloaded.core.machine.Persistable;
import li.cil.ocreloaded.core.machine.PersistenceHolder;
import li.cil.ocreloaded.core.machine.component.Component;
import li.cil.ocreloaded.core.machine.imp.MachineProcessorImp;
import li.cil.ocreloaded.core.misc.Label;
import li.cil.ocreloaded.core.network.NetworkNode;
import li.cil.ocreloaded.core.network.NetworkNode.Visibility;
import li.cil.ocreloaded.core.network.NetworkNodes;
import li.cil.ocreloaded.minecraft.common.SettingsConstants;
import li.cil.ocreloaded.minecraft.common.block.CaseBlock;
import li.cil.ocreloaded.minecraft.common.component.ComponentNetworkUtil;
import li.cil.ocreloaded.core.network.LazyNetworkNode;
import li.cil.ocreloaded.core.network.NetworkNodePersistence;
import li.cil.ocreloaded.minecraft.common.item.ComponentItem;
import li.cil.ocreloaded.minecraft.common.menu.CaseMenu;
import li.cil.ocreloaded.minecraft.common.network.IPlatformNetworkHelper;
import li.cil.ocreloaded.minecraft.common.network.packets.SoundPacket;
import li.cil.ocreloaded.minecraft.common.persistence.NBTPersistenceHolder;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import li.cil.ocreloaded.minecraft.common.util.ItemList;
import li.cil.ocreloaded.minecraft.common.util.ItemList.ItemChangeListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CaseBlockEntity extends RandomizableContainerBlockEntity implements TickableEntity, ComponentTileEntity, ItemChangeListener, Persistable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaseBlockEntity.class);
    private static final net.minecraft.network.chat.Component MENU_NAME = net.minecraft.network.chat.Component.translatable("gui.ocreloaded.case");

    private static final String TAG_POWERED = "ocreloaded:powered";
    private Optional<Machine> machine = Optional.empty();

    private final ItemList items = ItemList.withSize(10, this);
    private final MachineProcessorImp processor = new MachineProcessorImp(MachineBuilder.getDefaultInstance());

    private final LazyNetworkNode networkNode = NetworkNodes.lazy(
        id -> NetworkNodes.component(id, node -> new ComputerComponent(node, () -> machine), Visibility.NETWORK));
    private final LazyNetworkNode tmpFsNode = NetworkNodes.lazy(
        id -> NetworkNodes.component(id, node -> new FileSystemComponent(node, InMemoryFileSystem::new, Label.create()), Visibility.NEIGHBORS));
    private boolean internalNodesConnected;
    private Map<ItemStack, NetworkNode> loadedComponents = new HashMap<>();
    private boolean powered;

    public CaseBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CommonRegistered.CASE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public NetworkNode networkNode() {
        ensureInternalNodesConnected();
        return this.networkNode.get();
    }

    @Override
    public void loadAdditional(@Nonnull CompoundTag compoundTag, @Nonnull HolderLookup.Provider registries) {
        super.loadAdditional(compoundTag, registries);
        ContainerHelper.loadAllItems(compoundTag, this.items, registries);
        load(new NBTPersistenceHolder(compoundTag, SettingsConstants.namespace));
        updateBlockState();
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compoundTag, @Nonnull HolderLookup.Provider registries) {
        super.saveAdditional(compoundTag, registries);
        ContainerHelper.saveAllItems(compoundTag, this.items, registries);
        save(new NBTPersistenceHolder(compoundTag, SettingsConstants.namespace));
    }

    @Override
    public CompoundTag getUpdateTag(@Nonnull HolderLookup.Provider registries) {
        CompoundTag compoundTag = super.getUpdateTag(registries);
        compoundTag.putBoolean(TAG_POWERED, this.powered);

        return compoundTag;
    }

    @Override
    public void setLevel(@Nonnull Level level) {
        super.setLevel(level);

        if (!level.isClientSide()) {
            level.addBlockEntityTicker(new BlockEntityTicker(this));
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        this.machine.ifPresent(Machine::stop);
        networkNode().remove();
    }

    @Override
    public void tick() {
        machine.ifPresent(Machine::runSync);
    }

    @Override
    public int getContainerSize() {
        return 10;
    }

    @Override
    protected void setItems(@Nonnull NonNullList<ItemStack> var1) {
        assert var1.size() == 10;
        for (int i = 0; i < 10; i++) {
            this.items.set(i, var1.get(i));
        }
    }

    @Override
    protected net.minecraft.network.chat.Component getDefaultName() {
        return MENU_NAME;
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowId, @Nonnull Inventory playerInventory) {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
        writeData(data);
        return new CaseMenu(windowId, playerInventory, data);
    }

    @Override
    @SuppressWarnings("null") // Linting being not smart
    public void onItemChange(int slot, ItemStack oldStack, ItemStack newStack) {
        if (level == null || level.isClientSide) return;
        NetworkNode oldNode = loadedComponents.remove(oldStack);
        if (oldNode != null) {
            oldNode.remove();
        }
        if (newStack.isEmpty()) return;
        loadComponent(newStack, loadedComponents);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public void save(PersistenceHolder holder) {
        networkNode.saveId(holder);
        holder.storeBool(TAG_POWERED, this.powered);
    }

    @Override
    public void load(PersistenceHolder holder) {
        networkNode.loadId(holder);
        ensureInternalNodesConnected();
        this.powered = holder.loadBool(TAG_POWERED);
        for (ItemStack itemStack : this.items) {
            loadComponent(itemStack, loadedComponents);
        }
    }

    public void writeData(FriendlyByteBuf data) {
        data.writeBlockPos(this.worldPosition);
        data.writeInt(((CaseBlock) this.getBlockState().getBlock()).getTier());
    }

    public boolean isPowered() {
        return this.powered;
    }

    public void setPowered(boolean b) {
        this.powered = b;
        setChanged();
        updateBlockState();
    }

    @SuppressWarnings("null") // Linting being not smart
    private void updateBlockState() {
        if (this.level == null || level.isClientSide) return;
        if (!(this.level.isLoaded(this.worldPosition) && this.getBlockState().getBlock() instanceof CaseBlock)) return;
        ComponentNetworkUtil.connectToNeighbors(level, worldPosition);

        BlockState newBlockState = level.getBlockState(this.worldPosition).setValue(CaseBlock.RUNNING, this.powered);
        level.setBlock(this.worldPosition, newBlockState, 3);

        if (this.powered && this.machine.isEmpty()) {
            this.machine = createMachine();
            loadComponents();
            boolean started = this.machine.map(Machine::start).orElse(false);
            if (!started) {
                this.powered = false;
                LOGGER.error("Failed to start machine for case at {}.", this.worldPosition);
                // TODO: Indicate that the machine could not be started.
            }
        } else if (!this.powered && this.machine.isPresent()) {
            this.machine.ifPresent(Machine::stop);
            this.machine = Optional.empty();
        }
    }

    private void loadComponents() {
        Map<ItemStack, NetworkNode> components = new HashMap<>();
        for (ItemStack itemStack : this.items) {
            if (itemStack.isEmpty()) continue;

            if (loadedComponents.containsKey(itemStack)) {
                components.put(itemStack, loadedComponents.remove(itemStack));
                continue;
            }

            loadComponent(itemStack, components);
        }

        for (Entry<ItemStack, NetworkNode> entry : loadedComponents.entrySet()) {
            this.networkNode().disconnect(entry.getValue());
        }

        this.loadedComponents = components;
    }

    private void loadComponent(ItemStack itemStack, Map<ItemStack, NetworkNode> components) {
        if (itemStack.isEmpty()) return;
        if (!(itemStack.getItem() instanceof ComponentItem componentHolder)) return;

        CompoundTag tag = itemStack.get(CommonRegistered.NBT_DATA_TYPE.get());
        if (tag == null) tag = new CompoundTag();
        NBTPersistenceHolder holder = new NBTPersistenceHolder(tag, SettingsConstants.namespace);

        NetworkNode networkNode = componentHolder.newNetworkNode(NetworkNodePersistence.loadIdOrRandom(holder));
        if (!networkNode.component().isPresent()) return;
        Component component = networkNode.component().get();

        // TODO: Better way to save and load (needs to save on shutdown too)
        component.load(holder);
        component.save(holder);
        itemStack.set(CommonRegistered.NBT_DATA_TYPE.get(), tag);
        // TODO: Reset component on fresh boot if tmp is not persistant

        components.put(itemStack, networkNode);
        this.networkNode().connect(networkNode);
    }

    private Optional<Machine> createMachine() {
        ExecutorService threadService = Executors.newCachedThreadPool(); // TODO: Custom thread pool
        return MachineBuilder.getDefaultInstance().createMachine(
            processor.getArchitecture(),
            networkNode(),
            tmpFsNode(),
            threadService,
            processor,
            this::beep);
    }

    @SuppressWarnings("null") // Linting being not smart
    private void beep(short frequency, short duration) {
        if (level == null) return;
        ChunkPos chunkPos = new ChunkPos(worldPosition);
        List<ServerPlayer> chunkTrackingPlayers = ((ServerLevel) level).getPlayers(player -> player.getChunkTrackingView().contains(chunkPos));

        IPlatformNetworkHelper.INSTANCE.sendToClients(SoundPacket.createBeepMessage(worldPosition, frequency, duration), chunkTrackingPlayers);
    }

    private void ensureInternalNodesConnected() {
        if (internalNodesConnected) return;

        networkNode.get().connect(tmpFsNode.get());
        internalNodesConnected = true;
    }

    private NetworkNode tmpFsNode() {
        ensureInternalNodesConnected();
        return tmpFsNode.get();
    }
}
