package li.cil.ocreloaded.core.machine.architecture.component;

import li.cil.ocreloaded.core.machine.architecture.component.ComponentCall.ComponentCallResult;

public class EepromComponent extends AnnotatedComponent {

    public EepromComponent() {
        super("eeprom");
    }
    
    @ComponentMethod(direct = true, doc = "function():string -- Get the currently stored byte array.")
    public ComponentCallResult get() {
        return ComponentCallResult.success("error(\"Hello, World!\")");
    }
   
}
