package li.cil.ocreloaded.core.filesystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import li.cil.ocreloaded.core.machine.filesystem.FileHandle;
import li.cil.ocreloaded.core.machine.filesystem.FileSystem;

public class DirectoryFileSystem implements FileSystem {

    private final FSStreamHandleTracker handles = new FSStreamHandleTracker();

    private final Path root;

    public DirectoryFileSystem(Path root) {
        this.root = root;
        root.toFile().mkdirs();
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public boolean exists(String path) {
        Path resolved = root.resolve(path);
        return resolved.toFile().exists();
    }

    @Override
    public long size(String path) {
        Path resolved = root.resolve(path);
        return resolved.toFile().length();
    }

    @Override
    public boolean isDirectory(String path) {
        Path resolved = root.resolve(path);
        return resolved.toFile().isDirectory();
    }

    @Override
    public long lastModified(String path) {
        Path resolved = root.resolve(path);
        return resolved.toFile().lastModified();
    }

    @Override
    public List<String> list(String path) {
        Path resolved = root.resolve(path);
        List<String> files = List.of(resolved.toFile().list());
        return files;
    }
    
    @Override
    public boolean makeDirectory(String parentPath) {
        Path resolved = root.resolve(parentPath);
        return resolved.toFile().mkdir();
    }

    @Override
    public boolean remove(String path) throws IOException {
        Path resolved = root.resolve(path);
        return Files.deleteIfExists(resolved);
    }

    @Override
    public int open(String path, Mode mode) throws IOException {
        Path resolved = root.resolve(path);
        if (Files.notExists(resolved) && mode == Mode.APPEND) {
            resolved.toFile().createNewFile();
        }
        return switch (mode) {
            case READ -> handles.openInput(() -> Files.newInputStream(resolved), Files.size(resolved));
            case WRITE -> handles.openOutput(() -> Files.newOutputStream(resolved), () -> Files.size(resolved));
            case APPEND -> handles.openOutput(() -> Files.newOutputStream(resolved, java.nio.file.StandardOpenOption.APPEND), () -> Files.size(resolved));
        };
    }

    @Override
    public FileHandle getHandle(int handle) {
        return handles.getHandle(handle);
    }

    @Override
    public void close() throws IOException {
        handles.close();
    }
    
}
