package li.cil.ocreloaded.core.component;

import java.util.Optional;
import java.util.function.Supplier;

import li.cil.ocreloaded.core.machine.Machine;
import li.cil.ocreloaded.core.machine.component.AnnotatedComponent;
import li.cil.ocreloaded.core.machine.component.ComponentCall.ComponentCallResult;
import li.cil.ocreloaded.core.machine.component.ComponentCallArguments;
import li.cil.ocreloaded.core.machine.component.ComponentCallContext;
import li.cil.ocreloaded.core.machine.component.ComponentMethod;
import li.cil.ocreloaded.core.network.NetworkMessage;
import li.cil.ocreloaded.core.network.NetworkNode;

public class ComputerComponent extends AnnotatedComponent {
    
    private final Supplier<Optional<Machine>> machineSupplier;

    public ComputerComponent(Supplier<Optional<Machine>> machineSupplier) {
        super("computer");
        this.machineSupplier = machineSupplier;
    }

    @ComponentMethod(doc = "function([frequency:string or number[, duration:number]]) -- Plays a tone, useful to alert users via audible feedback.")
    public ComponentCallResult beep(ComponentCallContext context, ComponentCallArguments arguments) {
        // TODO: Implement
        return ComponentCallResult.success();
    }

    @Override
    public void onMessage(NetworkMessage message, NetworkNode sender) {
        switch (message.name()) {
            case "computer.checked_signal": {
                // TODO: Check player perms
                Optional<Machine> machine = machineSupplier.get();
                if (machine.isPresent()) {
                    String name = (String) message.arguments()[1];
                    Object[] args = new Object[message.arguments().length - 1];
                    args[0] = sender.id().toString();
                    System.arraycopy(message.arguments(), 2, args, 1, args.length - 1);
                    machine.get().signal(name, args);
                }
                break;
            }
            default:
                break;
        }
    }

}
