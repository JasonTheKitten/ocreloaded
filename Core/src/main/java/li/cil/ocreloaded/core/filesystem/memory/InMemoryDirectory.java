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

    public void makeDirectory(String string) {
        children.put(string, new InMemoryDirectory());
    }

}
