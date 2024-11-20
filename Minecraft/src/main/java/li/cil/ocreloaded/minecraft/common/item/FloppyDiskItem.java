package li.cil.ocreloaded.minecraft.common.item;

import li.cil.ocreloaded.core.component.FilesystemComponent;
import li.cil.ocreloaded.core.machine.component.Component;
import li.cil.ocreloaded.core.machine.fs.FileSystem;
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
    public Component initComponent() {
        FileSystem fileSystem = fileSystemFactory.createFileSystem(tag, null);
        return new FilesystemComponent(fileSystem);
    }

}
