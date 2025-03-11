package li.cil.ocreloaded.minecraft.server.machine.fssup;

import java.nio.file.Path;
import java.util.UUID;

import li.cil.ocreloaded.core.filesystem.DirectoryFileSystem;
import li.cil.ocreloaded.core.machine.filesystem.FileSystem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

public final class LocalFileSystemSupplier {
    
    private LocalFileSystemSupplier() {}

    public static FileSystem createLocalFS(MinecraftServer server, UUID uuid) {
        Path computerPath = server.getWorldPath(LevelResource.ROOT).resolve("opencomputers").resolve(uuid.toString());
        return new DirectoryFileSystem(computerPath);
    }

}
