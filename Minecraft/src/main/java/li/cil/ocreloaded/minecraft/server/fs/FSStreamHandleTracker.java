package li.cil.ocreloaded.minecraft.server.fs;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import li.cil.ocreloaded.core.machine.fs.FileHandle;

public class FSStreamHandleTracker {
    
    private Map<Integer, FileHandle> handles = new HashMap<>();
    // TODO: Handle files that are already open

    public int openInput(InputStreamSupplier streamSupplier, int length) throws IOException {
        int handle = -1;
        do {
            handle = (int) (Math.random() * Integer.MAX_VALUE);
        } while (handles.containsKey(handle));

        int finalHandle = handle;
        handles.put(handle, new InputStreamFileHandle(streamSupplier.get(), length, () -> handles.remove(finalHandle)));

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
    
}
