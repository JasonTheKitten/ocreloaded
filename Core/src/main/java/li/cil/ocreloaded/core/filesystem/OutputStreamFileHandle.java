package li.cil.ocreloaded.core.filesystem;

import java.io.IOException;
import java.io.OutputStream;

import li.cil.ocreloaded.core.machine.filesystem.FileHandle;

public class OutputStreamFileHandle implements FileHandle {
    
    private final OutputStream stream;
    private final LengthSupplier lengthSupplier;
    private final Runnable onClose;

    private long position;
        
    public OutputStreamFileHandle(OutputStream stream, LengthSupplier lengthSupplier, Runnable onClose) {
        this.stream = stream;
        this.lengthSupplier = lengthSupplier;
        this.onClose = onClose;
    }

    @Override
    public long length() throws IOException {
        return lengthSupplier.get();
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
        throw new IOException("bad file descriptor");
    }

    @Override
    public int read(byte[] buffer) throws IOException {
        throw new IOException("bad file descriptor");
    }

    @Override
    public void write(byte[] buffer) throws IOException {
        stream.write(buffer);
        position += buffer.length;
    }

    public static interface LengthSupplier {
        long get() throws IOException;
    }

}
