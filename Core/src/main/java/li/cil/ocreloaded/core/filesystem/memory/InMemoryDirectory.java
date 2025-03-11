package li.cil.ocreloaded.core.filesystem.memory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryDirectory implements InMemoryNode {
    
    private final Map<String, InMemoryNode> children = new HashMap<>();

    public InMemoryNode node(String name) {
        return children.get(name);
    }

    public List<String> list() {
        return children.keySet().stream()
            .map(name -> children.get(name) instanceof InMemoryDirectory ? name + "/" : name)
            .toList();
    }

    public InMemoryDirectory makeDirectory(String string) {
        if (children.containsKey(string)) {
            return (InMemoryDirectory) children.get(string);
        }
        InMemoryDirectory directory = new InMemoryDirectory();
        children.put(string, directory);
        return directory;
    }

    public InMemoryNode makeFile(String string) {
        InMemoryFile file = new InMemoryFile();
        children.put(string, file);
        return file;
    }

    public boolean remove(InMemoryNode node) {
        return children.values().remove(node);
    }

}
