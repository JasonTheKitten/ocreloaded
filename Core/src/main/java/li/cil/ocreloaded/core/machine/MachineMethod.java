package li.cil.ocreloaded.core.machine;

import java.lang.reflect.InvocationTargetException;

public interface MachineMethod {
    void call() throws InvocationTargetException, IllegalAccessException;
    String doc();
    boolean mainthread();
}
