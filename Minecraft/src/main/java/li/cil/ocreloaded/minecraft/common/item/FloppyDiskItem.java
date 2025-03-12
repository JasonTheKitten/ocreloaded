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

public class FloppyDiskItem extends Item implements ComponentItem {

    private static FileSystemSupplierRegistry fileSystemFactory = FileSystemSupplierRegistry.getDefaultInstance();

    private final String tag;

    public FloppyDiskItem(Properties properties) {
        this(properties, "default");
    }

    public FloppyDiskItem(Properties properties, String tag) {
        super(properties);
        this.tag = tag;
    }

    @Override
    public NetworkNode newNetworkNode() {
        AtomicReference<UUID> nodeID = new AtomicReference<>();
        NetworkNode node = new ComponentNetworkNode(node_ -> new FileSystemComponent(
            node_,
            () -> fileSystemFactory.createFileSystem(tag, node_.id()),
            Label.create()), Visibility.NEIGHBORS);
        nodeID.set(node.id());

        return node;
    }

}
