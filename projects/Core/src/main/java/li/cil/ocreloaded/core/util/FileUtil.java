package li.cil.ocreloaded.core.util;

import java.util.ArrayList;
import java.util.List;

public final class FileUtil {
    
    private FileUtil() {}

    public static List<String> splitPath(String path) {
        List<String> parts = new ArrayList<>();
        for (String part : path.split("/")) {
            if (part.equals("..")) {
                if (!parts.isEmpty()) {
                    parts.remove(parts.size() - 1);
                }
            } else if (!part.isEmpty() && !part.equals(".")) {
                parts.add(part);
            }
        }

        return parts;
    }

}
