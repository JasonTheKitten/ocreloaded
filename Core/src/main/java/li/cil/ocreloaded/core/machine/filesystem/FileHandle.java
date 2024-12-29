package li.cil.ocreloaded.core.machine.filesystem;

import java.io.IOException;

public interface FileHandle {
    
    long length();

    long position();

    void close() throws IOException;

    long seek(long position) throws IOException;

    int read(byte[] buffer) throws IOException;

    void write(byte[] buffer) throws IOException;

}
