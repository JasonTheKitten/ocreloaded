package li.cil.ocreloaded.core.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import li.cil.ocreloaded.core.machine.PathUtil;
import li.cil.ocreloaded.core.machine.component.AnnotatedComponent;
import li.cil.ocreloaded.core.machine.component.ComponentCall.ComponentCallResult;
import li.cil.ocreloaded.core.machine.component.ComponentCallArguments;
import li.cil.ocreloaded.core.machine.component.ComponentCallContext;
import li.cil.ocreloaded.core.machine.component.ComponentMethod;
import li.cil.ocreloaded.core.machine.filesystem.FileSystem;
import li.cil.ocreloaded.core.misc.Label;
import li.cil.ocreloaded.core.util.FileUtil;

public class FileSystemComponent extends AnnotatedComponent {

    private final FileSystem filesystem;
    private final Label label;

    private final List<Integer> openFiles = new ArrayList<>();
    
    public FileSystemComponent(FileSystem filesystem, Label label) {
        super("filesystem");

        this.filesystem = filesystem;
        this.label = label;
    }

    @ComponentMethod(direct = true, doc = "function():string -- Get the current label of the drive.")
    public ComponentCallResult getLabel(ComponentCallContext context, ComponentCallArguments arguments) {
        return ComponentCallResult.success(label == null ? null : label.get());
    }

    @ComponentMethod(doc = "function(value:string):string -- Sets the label of the drive. Returns the new value, which may be truncated.")
    public ComponentCallResult setLabel(ComponentCallContext context, ComponentCallArguments arguments) {
        if (label == null) {
            return ComponentCallResult.failure("drive does not support labeling");
        }

        String value = arguments.optionalString(0, null);
        label.set(value);

        return ComponentCallResult.success(value);
    }

    @ComponentMethod(direct = true, doc = "function():boolean -- Returns whether the file system is read-only.")
    public ComponentCallResult isReadOnly(ComponentCallContext context, ComponentCallArguments arguments) {
        return ComponentCallResult.success(filesystem.isReadOnly());
    }

    @ComponentMethod(direct = true, doc = "function(path:string):boolean -- Returns whether an object exists at the specified absolute path in the file system.")
    public ComponentCallResult exists(ComponentCallContext context, ComponentCallArguments arguments) {
        String path = PathUtil.minimizePath(arguments.checkString(0));
        return ComponentCallResult.success(filesystem.exists(path));
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

    @ComponentMethod(doc = "function(path:string):boolean -- Creates a directory at the specified absolute path in the file system. Creates parent directories, if necessary.")
    public ComponentCallResult makeDirectory(ComponentCallContext context, ComponentCallArguments arguments) {
        String path = PathUtil.minimizePath(arguments.checkString(0));
        List<String> pathParts = FileUtil.splitPath(path);
        StringBuilder currentPath = new StringBuilder();
        boolean success = true;
        for (int i = 1; i <= pathParts.size(); i++) {
            currentPath.append(pathParts.get(i - 1));
            String parentPath = currentPath.toString();
            success = filesystem.exists(parentPath) || filesystem.makeDirectory(parentPath);
            if (!success) {
                return ComponentCallResult.success(false);
            }
        }

        return ComponentCallResult.success(true);
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
