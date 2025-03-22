package li.cil.ocreloaded.core.machine.filesystem;

import java.io.IOException;
import java.util.List;

public interface FileSystem {

    boolean isReadOnly();

    boolean exists(String path);

    long size(String path);

    long getCapacity();

    long getUsedSpace();

    boolean isDirectory(String path);

    long lastModified(String path);

    List<String> list(String path) throws IOException;

    boolean makeDirectory(String path);

    boolean remove(String path) throws IOException;
    
    int open(String path, Mode mode) throws IOException;

    FileHandle getHandle(int handle);

    void close() throws IOException;

    public static enum Mode {
        READ, WRITE, APPEND
    }

}
