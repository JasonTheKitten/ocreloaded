package li.cil.ocreloaded.core.machine.filesystem;

import java.io.IOException;
import java.util.List;

public interface FileSystem {

    boolean isReadOnly();

    boolean exists(String path);

    long size(String path);

    boolean isDirectory(String path);

    long lastModified(String path);

    List<String> list(String path);

    boolean makeDirectory(String path);
    
    int open(String path, Mode mode) throws IOException;

    FileHandle getHandle(int handle);

    void close() throws IOException;

    public static enum Mode {
        READ, WRITE, APPEND
    }

}
