package li.cil.ocreloaded.core.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import li.cil.ocreloaded.core.filesystem.memory.InMemoryDirectory;
import li.cil.ocreloaded.core.filesystem.memory.InMemoryFile;
import li.cil.ocreloaded.core.filesystem.memory.InMemoryNode;
import li.cil.ocreloaded.core.machine.filesystem.FileHandle;
import li.cil.ocreloaded.core.machine.filesystem.FileSystem;
import li.cil.ocreloaded.core.util.FileUtil;

public class InMemoryFileSystem implements FileSystem {

    private final InMemoryDirectory root = new InMemoryDirectory();
    private final FSStreamHandleTracker handleTracker = new FSStreamHandleTracker();

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public boolean exists(String path) {
        return nodeByPath(path) != null;
    }

    @Override
    public long size(String path) {
        InMemoryNode node = nodeByPath(path);
        if (node instanceof InMemoryFile file) {
            return file.length();
        } else {
            return 0;
        }
    }

    @Override
    public boolean isDirectory(String path) {
        return nodeByPath(path) instanceof InMemoryDirectory;
    }

    @Override
    public long lastModified(String path) {
        InMemoryNode node = nodeByPath(path);
        if (node instanceof InMemoryFile file) {
            return file.lastModified();
        } else {
            return 0;
        }
    }

    @Override
    public List<String> list(String path) {
        InMemoryNode node = nodeByPath(path);
        if (node instanceof InMemoryDirectory inMemoryDirectory) {
            return inMemoryDirectory.list();
        } else {
            return null;
        }
    }

    @Override
    public boolean makeDirectory(String path) {
        InMemoryNode node = parentByPath(path);
        if (node instanceof InMemoryDirectory parent) {
            parent.makeDirectory(FileUtil.splitPath(path).get(FileUtil.splitPath(path).size() - 1));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int open(String path, Mode mode) throws IOException {
        InMemoryNode node = nodeByPath(path);
        if (node == null) {
            throw new FileNotFoundException(path);
        }
        if (!(node instanceof InMemoryFile file)) {
            throw new FileNotFoundException("Cannot open directory for reading.");
        }

        return switch (mode) {
            case APPEND -> handleTracker.openOutput(() -> file.openOutput(true), () -> file.length());
            case READ -> handleTracker.openInput(() -> openFileInput(file), file.length());
            case WRITE -> handleTracker.openOutput(() -> file.openOutput(false), () -> file.length());
        };
    }

    @Override
    public FileHandle getHandle(int handle) {
        return handleTracker.getHandle(handle);
    }

    @Override
    public void close() throws IOException {
        handleTracker.close();
    }

    private InputStream openFileInput(InMemoryFile file) {
        return file.openInput();
    }

    private InMemoryNode nodeByPath(String path) {
        InMemoryNode node = root;
        for (String part: FileUtil.splitPath(path)) {
            if (node instanceof InMemoryDirectory inMemoryDirectory) {
                node = inMemoryDirectory.node(part);
            } else {
                return null;
            }
        }

        return node;
    }

    private InMemoryDirectory parentByPath(String path) {
        InMemoryNode node = root;
        InMemoryDirectory parent = null;
        for (String part: FileUtil.splitPath(path)) {
            if (node instanceof InMemoryDirectory inMemoryDirectory) {
                parent = inMemoryDirectory;
                node = inMemoryDirectory.node(part);
            } else {
                return null;
            }
        }

        return parent;
    }
    
}
