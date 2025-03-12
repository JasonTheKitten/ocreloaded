package li.cil.ocreloaded.core.component;

import li.cil.ocreloaded.core.machine.PersistenceHolder;
import li.cil.ocreloaded.core.machine.component.AnnotatedComponent;
import li.cil.ocreloaded.core.machine.component.ComponentCall.ComponentCallResult;
import li.cil.ocreloaded.core.machine.component.ComponentCallArguments;
import li.cil.ocreloaded.core.machine.component.ComponentCallContext;
import li.cil.ocreloaded.core.machine.component.ComponentMethod;
import li.cil.ocreloaded.core.network.NetworkNode;

public class EepromComponent extends AnnotatedComponent {

    private final String defaultCode;

    private String code;
    private String data = "";
    
    public EepromComponent(NetworkNode networkNode, String defaultCode) {
        super("eeprom", networkNode);
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
        return ComponentCallResult.success(data);
    }

    @ComponentMethod(doc = "function(data:string) -- Overwrite the currently stored byte array.")
    public ComponentCallResult setData(ComponentCallContext context, ComponentCallArguments arguments) {
        this.data = arguments.optionalString(0, "");
        return ComponentCallResult.success();
    }

    @Override
    public void load(PersistenceHolder holder) {
        super.load(holder);

        code = holder.hasKey("eeprom") ?
            holder.loadString("eeprom") :
            defaultCode;
        data = holder.loadString("userdata");
    }

    @Override
    public void save(PersistenceHolder holder) {
        super.save(holder);

        holder.storeString("eeprom", code);
        holder.storeString("userdata", data);
    }

}
