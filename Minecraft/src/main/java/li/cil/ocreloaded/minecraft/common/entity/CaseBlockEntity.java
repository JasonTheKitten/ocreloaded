package li.cil.ocreloaded.minecraft.common.entity;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import li.cil.ocreloaded.core.component.ComputerComponent;
import li.cil.ocreloaded.core.component.FileSystemComponent;
import li.cil.ocreloaded.core.filesystem.InMemoryFileSystem;
import li.cil.ocreloaded.core.machine.Machine;
import li.cil.ocreloaded.core.machine.MachineCodeRegistry;
import li.cil.ocreloaded.core.machine.MachineParameters;
import li.cil.ocreloaded.core.machine.MachineRegistry;
import li.cil.ocreloaded.core.machine.MachineRegistryEntry;
import li.cil.ocreloaded.core.machine.component.Component;
import li.cil.ocreloaded.core.machine.imp.MachineProcessorImp;
import li.cil.ocreloaded.core.misc.Label;
import li.cil.ocreloaded.core.network.NetworkNode;
import li.cil.ocreloaded.core.network.NetworkNode.Visibility;
import li.cil.ocreloaded.minecraft.common.SettingsConstants;
import li.cil.ocreloaded.minecraft.common.block.CaseBlock;
import li.cil.ocreloaded.minecraft.common.component.ComponentNetworkNode;
import li.cil.ocreloaded.minecraft.common.component.ComponentNetworkUtil;
import li.cil.ocreloaded.minecraft.common.item.ComponentItem;
import li.cil.ocreloaded.minecraft.common.persistence.NBTPersistenceHolder;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CaseBlockEntity extends BlockEntityWithTick implements ComponentTileEntity {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaseBlockEntity.class);

    private static final String TAG_POWERED = "ocreloaded:powered";
    private Optional<Machine> machine = Optional.empty();

    private final NonNullList<ItemStack> items = NonNullList.withSize(10, ItemStack.EMPTY);
    private final NetworkNode networkNode = new ComponentNetworkNode(node -> new ComputerComponent(node, () -> machine), Visibility.NETWORK);
    private final NetworkNode tmpFsNode = new ComponentNetworkNode(node -> new FileSystemComponent(node, () -> new InMemoryFileSystem(), Label.create()), Visibility.NEIGHBORS);
    private final MachineProcessorImp processor = new MachineProcessorImp(MachineRegistry.getDefaultInstance());

    private Map<ItemStack, NetworkNode> loadedComponents = new HashMap<>();
    private boolean powered;

    public CaseBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CommonRegistered.CASE_BLOCK_ENTITY.get(), blockPos, blockState);
        networkNode.connect(tmpFsNode);
    }

    @Override
    public NetworkNode networkNode() {
        return this.networkNode;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        networkNode.load(new NBTPersistenceHolder(compoundTag, SettingsConstants.namespace));

        ContainerHelper.loadAllItems(compoundTag, this.items);
        this.powered = compoundTag.getBoolean(TAG_POWERED);
        updateBlockState();
    }

    @Override
    public void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        networkNode.save(new NBTPersistenceHolder(compoundTag, SettingsConstants.namespace));

        ContainerHelper.saveAllItems(compoundTag, this.items);
        compoundTag.putBoolean(TAG_POWERED, this.powered);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compoundTag = super.getUpdateTag();
        compoundTag.putBoolean(TAG_POWERED, this.powered);

        return compoundTag;
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if (level == null) return;

        if (!level.isClientSide()) {
            this.level.addBlockEntityTicker(new BlockEntityTicker(this));
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        this.machine.ifPresent(Machine::stop);
        networkNode.remove();
    }

    @Override
    public void tick() {
        machine.ifPresent(Machine::runSync);
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public boolean isPowered() {
        return this.powered;
    }

    public void setPowered(boolean b) {
        this.powered = b;
        setChanged();
        updateBlockState();
    }

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
            if (!(itemStack.getItem() instanceof ComponentItem componentHolder)) continue;

            if (loadedComponents.containsKey(itemStack)) {
                components.put(itemStack, loadedComponents.remove(itemStack));
                continue;
            }

            NetworkNode networkNode = componentHolder.newNetworkNode();
            if (!networkNode.component().isPresent()) continue;
            Component component = networkNode.component().get();

            CompoundTag tag = itemStack.getOrCreateTag();
            component.load(new NBTPersistenceHolder(tag, SettingsConstants.namespace));
            // TODO: When should states be stored?
            component.save(new NBTPersistenceHolder(tag, SettingsConstants.namespace));
            // TODO: Reset component on fresh boot?

            components.put(itemStack, networkNode);
            this.networkNode.connect(networkNode);
        }

        for (Entry<ItemStack, NetworkNode> entry : loadedComponents.entrySet()) {
            this.networkNode.disconnect(entry.getValue());
        }

        this.loadedComponents = components;
    }

    private Optional<Machine> createMachine() {
        String architecture = processor.getArchitecture();
        Optional<Supplier<Optional<InputStream>>> codeStreamSupplier = MachineCodeRegistry
            .getDefaultInstance()
            .getMachineCodeSupplier(architecture);

        if (codeStreamSupplier.isEmpty()) return Optional.empty();

        ExecutorService threadService = Executors.newCachedThreadPool(); // TODO: Custom thread pool
        MachineParameters parameters = new MachineParameters(
            networkNode, tmpFsNode, codeStreamSupplier.get(), threadService, processor);

        return
            MachineRegistry.getDefaultInstance().getEntry(architecture)
                .filter(MachineRegistryEntry::isSupported)
                .flatMap(entry -> entry.createMachine(parameters));
    }
    
}
