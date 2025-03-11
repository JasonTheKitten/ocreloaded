package li.cil.ocreloaded.minecraft.common.item;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import li.cil.ocreloaded.core.component.FileSystemComponent;
import li.cil.ocreloaded.core.misc.Label;
import li.cil.ocreloaded.core.network.NetworkNode;
import li.cil.ocreloaded.core.network.NetworkNode.Visibility;
import li.cil.ocreloaded.minecraft.common.component.ComponentNetworkNode;
import li.cil.ocreloaded.minecraft.server.machine.fssup.FileSystemSupplierRegistry;
import net.minecraft.world.item.Item;

public class HardDiskItem extends Item implements TieredItem, ComponentItem {

    private static FileSystemSupplierRegistry fileSystemFactory = FileSystemSupplierRegistry.getDefaultInstance();

    private final int tier;

    public HardDiskItem(Properties properties, int tier) {
        super(properties);
        this.tier = tier;
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    @Override
    public NetworkNode newNetworkNode() {
        AtomicReference<UUID> nodeID = new AtomicReference<>();
        NetworkNode node = new ComponentNetworkNode(node_ -> new FileSystemComponent(
            fileSystemFactory.createFileSystem("localfs", node_.id()),
            Label.create()), Visibility.NEIGHBORS);
        nodeID.set(node.id());

        return node;
    }
}
