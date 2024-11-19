package li.cil.ocreloaded.minecraft.common.entity;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import li.cil.ocreloaded.core.machine.Machine;
import li.cil.ocreloaded.core.machine.MachineParameters;
import li.cil.ocreloaded.core.machine.MachineRegistry;
import li.cil.ocreloaded.core.machine.MachineRegistryEntry;
import li.cil.ocreloaded.core.machine.MachineStartCodeSupplierRegistry;
import li.cil.ocreloaded.core.machine.architecture.component.Component;
import li.cil.ocreloaded.minecraft.common.block.CaseBlock;
import li.cil.ocreloaded.minecraft.common.item.ComponentItem;
import li.cil.ocreloaded.minecraft.common.persistence.NBTPersistenceHolder;
import li.cil.ocreloaded.minecraft.common.registry.CommonRegistered;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CaseBlockEntity extends BlockEntity {

    private static final Logger logger = LoggerFactory.getLogger(CaseBlockEntity.class);

    private static final String TAG_POWERED = "ocreloaded:powered";

    private final NonNullList<ItemStack> items = NonNullList.withSize(10, ItemStack.EMPTY);

    private boolean powered;
    private Optional<Machine> machine = Optional.empty();
    private Level oldLevel;

    private Map<UUID, Component> components = new HashMap<>();

    public CaseBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CommonRegistered.CASE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);

        ContainerHelper.loadAllItems(compoundTag, this.items);
        this.powered = compoundTag.getBoolean(TAG_POWERED);
        updateBlockState();
    }

    @Override
    public void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);

        ContainerHelper.saveAllItems(compoundTag, this.items);
        compoundTag.putBoolean(TAG_POWERED, this.powered);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compoundTag = super.getUpdateTag();
        compoundTag.putBoolean(TAG_POWERED, this.powered);

        return compoundTag;
    }

    public void tick() {
        machine.ifPresent(Machine::runSync);
    }

    public String getArchitecture() {
        return "lua52";
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

    public String getId() {
        return "AAAAAAAAAAHHHHHHHHHHH";
    }

    public Map<UUID, Component> scanComponents() {
        return components;
    }

    private void updateBlockState() {
        if (this.level == null || level.isClientSide) return;
        if (!(this.level.isLoaded(this.worldPosition) && this.getBlockState().getBlock() instanceof CaseBlock)) return;

        if (this.oldLevel != this.level) {
            this.oldLevel = this.level;
            this.level.addBlockEntityTicker(new CaseBlockEntityTicker(this));
        }

        BlockState newBlockState = level.getBlockState(this.worldPosition).setValue(CaseBlock.RUNNING, this.powered);
        level.setBlock(this.worldPosition, newBlockState, 3);

        if (this.powered && this.machine.isEmpty()) {
            this.machine = createMachine();
            loadComponents();
            boolean started = this.machine.map(Machine::start).orElse(false);
            if (!started) {
                this.powered = false;
                logger.error("Failed to start machine for case at {}.", this.worldPosition);
                // TODO: Indicate that the machine could not be started.
            }
        } else if (!this.powered && this.machine.isPresent()) {
            this.machine.ifPresent(Machine::stop);
            this.machine = Optional.empty();
        }
    }

    private void loadComponents() {
        // TODO: Ensure only one instance gets instantiated per item
        components.clear();
        for (ItemStack itemStack : this.items) {
            if (itemStack.isEmpty()) continue;

            if (!(itemStack.getItem() instanceof ComponentItem componentItem)) continue;

            Component component = componentItem.initComponent();

            CompoundTag tag = itemStack.getOrCreateTag();
            component.loadFromState(new NBTPersistenceHolder(tag));

            components.put(component.getId(), component);
        }
    }

    private Optional<Machine> createMachine() {
        Optional<Supplier<Optional<InputStream>>> codeStreamSupplier = MachineStartCodeSupplierRegistry
            .getDefaultInstance()
            .getSupplier(getArchitecture());

        if (codeStreamSupplier.isEmpty()) return Optional.empty();

        ExecutorService threadService = Executors.newCachedThreadPool(); // TODO: Custom thread pool
        MachineParameters parameters = new MachineParameters(getId(), codeStreamSupplier.get(), this::scanComponents, threadService);

        return
            MachineRegistry.getDefaultInstance().getEntry(getArchitecture())
                .filter(MachineRegistryEntry::isSupported)
                .flatMap(entry -> entry.createMachine(parameters));
    }
    
}
