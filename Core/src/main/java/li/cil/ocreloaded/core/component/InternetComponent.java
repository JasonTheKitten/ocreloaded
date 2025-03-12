package li.cil.ocreloaded.core.component;

import li.cil.ocreloaded.core.machine.component.*;
import li.cil.ocreloaded.core.network.NetworkNode;

public class InternetComponent extends AnnotatedComponent {
    private boolean TcpEnabled = true;
    private boolean HttpEnabled = true;
    public InternetComponent(NetworkNode networkNode) {
        super("internet", networkNode);
    }

    public InternetComponent(NetworkNode networkNode, boolean tcpEnabled, boolean httpEnabled) {
        super("internet", networkNode);
        this.TcpEnabled = tcpEnabled;
        this.HttpEnabled = httpEnabled;
    }

    @ComponentMethod(direct = true, doc = "isTcpEnabled():boolean -- Returns whether TCP connections can be made (config setting).")
    public ComponentCall.ComponentCallResult isTcpEnabled(ComponentCallContext context, ComponentCallArguments arguments) {
        return ComponentCall.ComponentCallResult.success(this.TcpEnabled);
    }

    @ComponentMethod(direct = true, doc = "isHttpEnabled():boolean -- Returns whether Http connections can be made (config setting).")
    public ComponentCall.ComponentCallResult isHttpEnabled(ComponentCallContext context, ComponentCallArguments arguments) {
        return ComponentCall.ComponentCallResult.success(this.HttpEnabled);
    }
}
