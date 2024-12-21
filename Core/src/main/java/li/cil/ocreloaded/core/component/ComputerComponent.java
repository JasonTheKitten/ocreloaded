package li.cil.ocreloaded.core.component;

import li.cil.ocreloaded.core.machine.component.AnnotatedComponent;
import li.cil.ocreloaded.core.machine.component.ComponentCall.ComponentCallResult;
import li.cil.ocreloaded.core.machine.component.ComponentCallArguments;
import li.cil.ocreloaded.core.machine.component.ComponentCallContext;
import li.cil.ocreloaded.core.machine.component.ComponentMethod;

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
