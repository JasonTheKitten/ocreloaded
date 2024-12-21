package li.cil.ocreloaded.minecraft.common.item;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import li.cil.ocreloaded.core.component.FilesystemComponent;
import li.cil.ocreloaded.core.machine.fs.FileSystem;
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
        FileSystem fileSystem = fileSystemFactory.createFileSystem(tag, () -> nodeID.get());
        NetworkNode node = new ComponentNetworkNode(Optional.of(new FilesystemComponent(fileSystem)), Visibility.NEIGHBORS);
        nodeID.set(node.id());

        return node;
    }

}
