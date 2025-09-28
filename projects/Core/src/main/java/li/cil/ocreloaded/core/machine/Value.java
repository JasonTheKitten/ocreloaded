package li.cil.ocreloaded.core.machine;

import li.cil.ocreloaded.core.machine.component.ComponentCallArguments;
import li.cil.ocreloaded.core.machine.component.ComponentCallContext;

public interface Value extends Persistable {
    // this is used for indexing into a object eg: Value[thing]
    Object apply(ComponentCallContext context, ComponentCallArguments arguents);
    // this is used for assigning to a index eg: Value[thing] = new
    void unapply(ComponentCallContext context, ComponentCallArguments arguments);
    // this is used when calling the Value as a function eg: Value(IdiotBox)
    Object[] call(ComponentCallContext context, ComponentCallArguments arguments);
    // this is called to garbage collect the value
    void dispose(ComponentCallContext context);
}
