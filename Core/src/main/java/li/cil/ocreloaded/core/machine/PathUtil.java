package li.cil.ocreloaded.core.machine;

import java.util.ArrayList;
import java.util.List;

public final class PathUtil {

    private PathUtil() {}
    
    public static String minimizePath(final String path) {
        List<String> parts = new ArrayList<>();
        for (String part : path.split("/")) {
            if (part.isEmpty() || part.equals(".")) {
                continue;
            }
            if (part.equals("..")) {
                if (!parts.isEmpty()) {
                    parts.remove(parts.size() - 1);
                }
                continue;
            }
            parts.add(part);
        }

        return String.join("/", parts);
    }

}
