package li.cil.ocreloaded.minecraft.server.machine.fssup;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import li.cil.ocreloaded.core.machine.filesystem.FileSystem;

public class FileSystemSupplierRegistry {
    
    private static final FileSystemSupplierRegistry INSTANCE = new FileSystemSupplierRegistry();

    private final Map<String, FileSystemSupplier> suppliers = new HashMap<>();

    private FileSystemSupplierRegistry() {}

    public void register(String name, FileSystemSupplier supplier) {
        suppliers.put(name, supplier);
    }

    public FileSystem createFileSystem(String fullTag, UUID uuid) {
        String[] parts = fullTag.split(":", 2);
        String name = parts[0];
        String tag = parts.length == 2 ? parts[1] : "";

        FileSystemSupplier supplier = suppliers.get(name);
        if (supplier == null) {
            throw new IllegalArgumentException("No file system supplier registered for '" + name + "'.");
        }

        return supplier.createFileSystem(uuid, tag);
    }

    public static FileSystemSupplierRegistry getDefaultInstance() {
        return INSTANCE;
    }

    public static interface FileSystemSupplier {
        
        FileSystem createFileSystem(UUID uuid, String tag);

    }

}
