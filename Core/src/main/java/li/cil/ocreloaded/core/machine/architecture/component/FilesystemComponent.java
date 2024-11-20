package li.cil.ocreloaded.core.machine.architecture.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import li.cil.ocreloaded.core.machine.PathUtil;
import li.cil.ocreloaded.core.machine.architecture.component.base.AnnotatedComponent;
import li.cil.ocreloaded.core.machine.architecture.component.base.ComponentCall.ComponentCallResult;
import li.cil.ocreloaded.core.machine.architecture.component.base.ComponentCallArguments;
import li.cil.ocreloaded.core.machine.architecture.component.base.ComponentCallContext;
import li.cil.ocreloaded.core.machine.architecture.component.base.ComponentMethod;
import li.cil.ocreloaded.core.machine.fs.FileSystem;

public class FilesystemComponent extends AnnotatedComponent {

    private final FileSystem filesystem;

    private final List<Integer> openFiles = new ArrayList<>();
    
    public FilesystemComponent(FileSystem filesystem) {
        super("filesystem");

        this.filesystem = filesystem;
    }

    @ComponentMethod(direct = true, doc = "function(path:string):boolean -- Returns whether the object at the specified absolute path in the file system is a directory.")
    public ComponentCallResult isDirectory(ComponentCallContext context, ComponentCallArguments arguments) {
        String path = PathUtil.minimizePath(arguments.checkString(0));
        return ComponentCallResult.success(filesystem.isDirectory(path));
    }

    @ComponentMethod(direct = true, doc = "function(path:string):table -- Returns a list of names of objects in the directory at the specified absolute path in the file system.")
    public ComponentCallResult list(ComponentCallContext context, ComponentCallArguments arguments) {
        String path = PathUtil.minimizePath(arguments.checkString(0));
        return ComponentCallResult.success(filesystem.list(path));
    }

    @ComponentMethod(direct = true, doc = "function(handle:userdata) -- Closes an open file descriptor with the specified handle.")
    public ComponentCallResult close(ComponentCallContext context, ComponentCallArguments arguments) {
        int handle = arguments.checkInteger(0);
        
        try {
            filesystem.getHandle(handle).close();
            openFiles.remove((Integer) handle);
        } catch (IOException e) {
            return ComponentCallResult.failure(e.getMessage());
        }

        return ComponentCallResult.success();
    }

    @ComponentMethod(direct = true, doc = "function(path:string[, mode:string='r']):userdata -- Opens a new file descriptor and returns its handle.")
    public ComponentCallResult open(ComponentCallContext context, ComponentCallArguments arguments) {
        String path = PathUtil.minimizePath(arguments.checkString(0));
        String mode = arguments.optionalString(1, "r");
        int handle;
        try {
            handle = filesystem.open(path, parseMode(mode));
        } catch (IOException e) {
            return ComponentCallResult.failure(e.getMessage());
        }

        openFiles.add(handle);

        return ComponentCallResult.success(handle);
    }

    @ComponentMethod(direct = true, doc = "function(handle:userdata, count:number):string or nil -- Reads up to the specified amount of data from an open file descriptor with the specified handle. Returns nil when EOF is reached.")
    public ComponentCallResult read(ComponentCallContext context, ComponentCallArguments arguments) {
        int handle = arguments.checkInteger(0);
        // TODO: Don't hardcode buffer sizes
        int count = Math.min(Math.max(arguments.checkInteger(1), 0), 2048);
        
        try {
            byte[] data = new byte[count];
            int read = filesystem.getHandle(handle).read(data);
            if (read == -1) {
                return ComponentCallResult.success((Object) null);
            }
            
            return ComponentCallResult.success(new String(data, 0, read));
        } catch (IOException e) {
            return ComponentCallResult.failure(e.getMessage());
        }
    }

    @Override
    public void dispose() {
        try {
            for (int handle : openFiles) {
                filesystem.getHandle(handle).close();
            }
            openFiles.clear();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FileSystem.Mode parseMode(final String mode) {
        return switch (mode) {
            case "r", "rb" -> FileSystem.Mode.READ;
            case "w", "wb" -> FileSystem.Mode.WRITE;
            case "a", "ab" -> FileSystem.Mode.APPEND;
            default -> throw new IllegalArgumentException("Invalid mode: " + mode);
        };
    }

}
