package li.cil.ocreloaded.core.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import li.cil.ocreloaded.core.filesystem.OutputStreamFileHandle.LengthSupplier;
import li.cil.ocreloaded.core.machine.filesystem.FileHandle;

public class FSStreamHandleTracker {
    
    private Map<Integer, FileHandle> handles = new HashMap<>();
    // TODO: Handle files that are already open

    public int openInput(InputStreamSupplier streamSupplier, long length) throws IOException {
        int handle = -1;
        do {
            handle = (int) (Math.random() * Integer.MAX_VALUE);
        } while (handles.containsKey(handle));

        int finalHandle = handle;
        handles.put(handle, new InputStreamFileHandle(streamSupplier.get(), length, () -> handles.remove(finalHandle)));

        return handle;
    }

    public int openOutput(OutputStreamSupplier streamSupplier, LengthSupplier lengthSupplier) throws IOException {
        int handle = -1;
        do {
            handle = (int) (Math.random() * Integer.MAX_VALUE);
        } while (handles.containsKey(handle));

        int finalHandle = handle;
        handles.put(handle, new OutputStreamFileHandle(streamSupplier.get(), lengthSupplier, () -> handles.remove(finalHandle)));

        return handle;
    }

    public FileHandle getHandle(int handle) {
        return handles.get(handle);
    }

    public void close() throws IOException {
        for (FileHandle handle : handles.values()) {
            handle.close();
        }
        handles.clear();
    }

    public static interface InputStreamSupplier {
        InputStream get() throws IOException;
    }

    public static interface OutputStreamSupplier {
        OutputStream get() throws IOException;
    }
    
}
