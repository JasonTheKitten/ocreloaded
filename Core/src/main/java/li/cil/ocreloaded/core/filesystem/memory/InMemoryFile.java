package li.cil.ocreloaded.core.filesystem.memory;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class InMemoryFile implements InMemoryNode {

    private ByteBuffer data = ByteBuffer.allocate(16);

    public long length() {
        return data.limit();
    }

    public InputStream openInput() {
        data.rewind();
        
        return new InputStream() {
            @Override
            public int read() {
                if (!data.hasRemaining()) {
                    return -1;
                }
                return data.get() & 0xFF;
            }

            @Override
            public int read(byte[] b, int off, int len) {
                len = Math.min(len, data.remaining());
                data.get(b, off, len);
                return len;
            }
        };
    }

    public OutputStream openOutput(boolean preserveOld) {
        if (!preserveOld) {
            data = ByteBuffer.allocate(16);
        }

        return new OutputStream() {
            @Override
            public void write(int b) {
                reserve(1);
                data.put((byte) b);
            }

            @Override
            public void write(byte[] b, int off, int len) {
                reserve(len);
                data.put(b, off, len);
            }

            private void reserve(int len) {
                if (data.capacity() < data.limit() + len) {
                    int computedNewCapacity = data.capacity();
                    while (computedNewCapacity < data.limit() + len) {
                        computedNewCapacity *= 2;
                    }
                    ByteBuffer newData = ByteBuffer.allocate(computedNewCapacity);
                    data.flip();
                    newData.put(data);
                    data = newData;
                }
            }
        };
    }
    
}
