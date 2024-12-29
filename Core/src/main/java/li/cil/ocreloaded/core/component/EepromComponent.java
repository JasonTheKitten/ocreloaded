package li.cil.ocreloaded.core.component;

import li.cil.ocreloaded.core.machine.PersistenceHolder;
import li.cil.ocreloaded.core.machine.component.AnnotatedComponent;
import li.cil.ocreloaded.core.machine.component.ComponentCall.ComponentCallResult;
import li.cil.ocreloaded.core.machine.component.ComponentCallArguments;
import li.cil.ocreloaded.core.machine.component.ComponentCallContext;
import li.cil.ocreloaded.core.machine.component.ComponentMethod;

public class EepromComponent extends AnnotatedComponent {

    private final String defaultCode;

    private String code;
    private String data;
    
    public EepromComponent(String defaultCode) {
        super("eeprom");
        this.defaultCode = defaultCode;
    }
    
    @ComponentMethod(direct = true, doc = "function():string -- Get the currently stored byte array.")
    public ComponentCallResult get(ComponentCallContext context, ComponentCallArguments arguments) {
        return ComponentCallResult.success(code);
    }

    @ComponentMethod(direct = true, doc = "function(data:string) -- Overwrite the currently stored byte array.")
    public ComponentCallResult set(ComponentCallContext context, ComponentCallArguments arguments) {
        this.code = arguments.optionalString(0, "");
        return ComponentCallResult.success();
    }

    @ComponentMethod(direct = true, doc = "function():string -- Get the currently stored byte array.")
    public ComponentCallResult getData(ComponentCallContext context, ComponentCallArguments arguments) {
        if (data.isEmpty()) {
            return ComponentCallResult.success();
        }
        return ComponentCallResult.success(data);
    }

    @ComponentMethod(direct = true, doc = "function(data:string) -- Overwrite the currently stored byte array.")
    public ComponentCallResult setData(ComponentCallContext context, ComponentCallArguments arguments) {
        this.data = arguments.optionalString(0, null);
        return ComponentCallResult.success();
    }

    @Override
    public void loadFromState(PersistenceHolder holder) {
        super.loadFromState(holder);

        code = holder.hasKey("eeprom") ?
            holder.loadString("eeprom") :
            defaultCode;
        data = holder.loadString("userdata");
    }

    @Override
    public void storeIntoState(PersistenceHolder holder) {
        super.storeIntoState(holder);

        holder.storeString("eeprom", code);
        holder.storeString("userdata", data);
    }

}
