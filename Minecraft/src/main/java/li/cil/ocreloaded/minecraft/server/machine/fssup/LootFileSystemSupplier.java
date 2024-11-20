package li.cil.ocreloaded.minecraft.server.machine.fssup;

import java.util.HashMap;
import java.util.Map;

import li.cil.ocreloaded.core.machine.fs.FileSystem;
import li.cil.ocreloaded.minecraft.server.fs.ArchiveFileSystem;
import li.cil.ocreloaded.minecraft.server.fs.ArchiveFileSystem.Archive;
import li.cil.ocreloaded.minecraft.server.fs.ArchiveFileSystem.ArchiveEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

public final class LootFileSystemSupplier {
    
    private LootFileSystemSupplier() {}

    public static FileSystem createLootFS(MinecraftServer server, ResourceLocation resourceLocation, String data) {
        ResourceManager resourceManager = server.getResourceManager();

        String fullPath = resourceLocation.getPath() + "/" + data;
        Map<ResourceLocation, Resource> resources = resourceManager.listResources(fullPath, path -> true);

        Map<String, ArchiveEntry> entries = new HashMap<>();
        for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
            ResourceLocation location = entry.getKey();
            Resource resource = entry.getValue();
            ArchiveEntry archiveEntry = new ArchiveEntry(location.getPath(), (long) 0, () -> resource.open());
            entries.put(location.getPath(), archiveEntry);
        }
        
        return new ArchiveFileSystem(new Archive(entries, fullPath));
    }
    
}
