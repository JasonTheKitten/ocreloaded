package li.cil.ocreloaded.minecraft.common.item;

import java.util.UUID;

import li.cil.ocreloaded.core.component.FileSystemComponent;
import li.cil.ocreloaded.core.misc.Label;
import li.cil.ocreloaded.core.network.NetworkNode;
import li.cil.ocreloaded.core.network.NetworkNode.Visibility;
import li.cil.ocreloaded.core.network.NetworkNodes;
import li.cil.ocreloaded.core.machine.filesystem.FileSystemSupplierRegistry;
import net.minecraft.world.item.Item;

public class FloppyDiskItem extends Item implements SlotItem, ComponentItem {

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
    public NetworkNode newNetworkNode(UUID id) {
        return NetworkNodes.component(id, node_ -> new FileSystemComponent(
            node_,
            () -> fileSystemFactory.createFileSystem(tag, node_.id()),
            Label.create()), Visibility.NEIGHBORS);
    }

}
