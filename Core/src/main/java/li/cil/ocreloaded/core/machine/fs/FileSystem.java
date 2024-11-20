package li.cil.ocreloaded.core.machine.fs;

import java.io.IOException;
import java.util.List;

public interface FileSystem {

    boolean isDirectory(String path);

    List<String> list(String path);
    
    int open(String path, Mode mode) throws IOException;

    FileHandle getHandle(int handle);

    void close() throws IOException;

    public static enum Mode {
        READ, WRITE, APPEND
    }

}
