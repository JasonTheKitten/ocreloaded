package li.cil.ocreloaded.core.filesystem;

import java.util.List;

import li.cil.ocreloaded.core.machine.filesystem.FileHandle;
import li.cil.ocreloaded.core.machine.filesystem.FileSystem;

public class DirectoryFileSystem implements FileSystem {

    public DirectoryFileSystem() {

    }

    @Override
    public boolean isReadOnly() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isReadOnly'");
    }

    @Override
    public boolean exists(String path) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exists'");
    }

    @Override
    public boolean isDirectory(String path) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isDirectory'");
    }

    @Override
    public List<String> list(String path) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'list'");
    }
    
    @Override
    public boolean makeDirectory(String parentPath) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'makeDirectory'");
    }

    @Override
    public int open(String path, Mode mode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'open'");
    }

    @Override
    public FileHandle getHandle(int handle) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHandle'");
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
    }
    
}
