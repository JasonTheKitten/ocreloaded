package li.cil.ocreloaded.minecraft.server.fs;

import java.io.IOException;
import java.io.InputStream;

import li.cil.ocreloaded.core.machine.fs.FileHandle;

public class InputStreamFileHandle implements FileHandle {
    
    private final InputStream stream;
    private final long length;
    private final Runnable onClose;

    private long position;
        
    public InputStreamFileHandle(InputStream stream, long length, Runnable onClose) {
        this.stream = stream;
        this.length = length;
        this.onClose = onClose;
    }

    @Override
    public long length() {
        return length;
    }

    @Override
    public long position() {
        return position;
    }

    @Override
    public void close() throws IOException {
        stream.close();
        onClose.run();
    }

    @Override
    public long seek(long position) throws IOException {
        if (position < this.position) {
            stream.reset();
            stream.skip(position);
        } else {
            stream.skip(position - this.position);
        }

        return this.position = position;
    }

    @Override
    public int read(byte[] buffer) throws IOException {
        int read = stream.read(buffer);
        if (read > 0) {
            position += read;
        }

        return read;
    }

    @Override
    public void write(byte[] buffer) throws IOException {
        throw new IOException("bad file descriptor");
    }

}
