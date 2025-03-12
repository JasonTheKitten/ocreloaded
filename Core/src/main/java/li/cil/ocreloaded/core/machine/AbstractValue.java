package li.cil.ocreloaded.core.machine;

import li.cil.ocreloaded.core.machine.component.ComponentCallArguments;
import li.cil.ocreloaded.core.machine.component.ComponentCallContext;

public abstract class AbstractValue implements Value {
    @Override
    public Object apply(ComponentCallContext context, ComponentCallArguments arguents) {
        return null;
    }

    @Override
    public void unapply(ComponentCallContext context, ComponentCallArguments arguments) {}

    @Override
    public Object[] call(ComponentCallContext context, ComponentCallArguments arguments) {
        throw new RuntimeException("trying to call a non-callable value");
    }

    @Override
    public void dispose(ComponentCallContext context) {}

    @Override
    public void load(PersistenceHolder holder) {}

    @Override
    public void save(PersistenceHolder holder) {}
}
