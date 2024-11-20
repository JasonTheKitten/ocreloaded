package li.cil.ocreloaded.core.machine.architecture.component;

import li.cil.ocreloaded.core.machine.architecture.component.base.AnnotatedComponent;
import li.cil.ocreloaded.core.machine.architecture.component.base.ComponentCall.ComponentCallResult;
import li.cil.ocreloaded.core.machine.architecture.component.base.ComponentCallArguments;
import li.cil.ocreloaded.core.machine.architecture.component.base.ComponentCallContext;
import li.cil.ocreloaded.core.machine.architecture.component.base.ComponentMethod;

public class ComputerComponent extends AnnotatedComponent {
    
    public ComputerComponent() {
        super("computer");
    }

    @ComponentMethod(doc = "function([frequency:string or number[, duration:number]]) -- Plays a tone, useful to alert users via audible feedback.")
    public ComponentCallResult beep(ComponentCallContext context, ComponentCallArguments arguments) {
        // TODO: Implement
        return ComponentCallResult.success();
    }

}
