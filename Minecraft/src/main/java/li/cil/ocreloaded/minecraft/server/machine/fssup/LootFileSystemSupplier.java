package li.cil.ocreloaded.minecraft.server.machine.fssup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import li.cil.ocreloaded.core.filesystem.ArchiveFileSystem;
import li.cil.ocreloaded.core.filesystem.ArchiveFileSystem.Archive;
import li.cil.ocreloaded.core.filesystem.ArchiveFileSystem.ArchiveEntry;
import li.cil.ocreloaded.core.machine.filesystem.FileSystem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

public final class LootFileSystemSupplier {
    
    private LootFileSystemSupplier() {}

    public static FileSystem createLootFS(MinecraftServer server, ResourceLocation resourceLocation, String data) {
        ResourceManager resourceManager = server.getResourceManager();

        String fullPath = resourceLocation.getPath() + "/" + data;
        Map<ResourceLocation, Resource> resources = resourceManager.listResources(fullPath, path -> path.getNamespace().equals(resourceLocation.getNamespace()));
        Map<String, String> remappings = getRemappings(resourceManager, resourceLocation, data);

        Map<String, ArchiveEntry> entries = new HashMap<>();
        for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
            ResourceLocation location = entry.getKey();
            Resource resource = entry.getValue();

            // TODO: Better way to get size of resource?
            long resourceSize = 0;
            try (InputStream stream = resource.open()) {
                int lastRead = 0;
                while (lastRead != -1) {
                    lastRead = stream.read(new byte[4096]);
                    resourceSize += Math.max(lastRead, 0);
                }
            } catch (IOException e) {}

            if (remappings.containsKey(location.getPath())) {
                entries.put(remappings.get(location.getPath()), new ArchiveEntry(remappings.get(location.getPath()), resourceSize, () -> resource.open()));
            } else {
                entries.put(location.getPath(), new ArchiveEntry(location.getPath(), resourceSize, () -> resource.open()));
            }
        }
        
        return new ArchiveFileSystem(new Archive(entries, fullPath));
    }
    
    private static Map<String, String> getRemappings(ResourceManager resourceManager, ResourceLocation resourceLocation, String data) {
        String remapPath = resourceLocation.getPath() + "/" + data + ".remap";
        Optional<Resource> remapResource = resourceManager.getResource(new ResourceLocation(resourceLocation.getNamespace(), remapPath));
        if (remapResource.isEmpty()) {
            return Map.of();
        }
        try (BufferedReader reader = remapResource.get().openAsReader()) {
            return reader.lines()
                .map(line -> line.split(":", 2))
                .collect(Collectors.toMap(parts -> parts[0], parts -> parts[1]));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load remap file for loot filesystem: " + remapPath, e);
        }
    }
    
}
