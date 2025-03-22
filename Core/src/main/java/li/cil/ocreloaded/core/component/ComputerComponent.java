package li.cil.ocreloaded.core.component;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import li.cil.ocreloaded.core.machine.Machine;
import li.cil.ocreloaded.core.machine.component.AnnotatedComponent;
import li.cil.ocreloaded.core.machine.component.Component;
import li.cil.ocreloaded.core.machine.component.ComponentCall.ComponentCallResult;
import li.cil.ocreloaded.core.machine.component.ComponentCallArguments;
import li.cil.ocreloaded.core.machine.component.ComponentCallContext;
import li.cil.ocreloaded.core.machine.component.ComponentMethod;
import li.cil.ocreloaded.core.network.NetworkMessage;
import li.cil.ocreloaded.core.network.NetworkNode;

public class ComputerComponent extends AnnotatedComponent {
    
    private final Supplier<Optional<Machine>> machineSupplier;

    public ComputerComponent(NetworkNode networkNode, Supplier<Optional<Machine>> machineSupplier) {
        super("computer", networkNode);
        this.machineSupplier = machineSupplier;
    }

    @ComponentMethod(doc = "function([frequency:string or number[, duration:number]]) -- Plays a tone, useful to alert users via audible feedback.")
    public ComponentCallResult beep(ComponentCallContext context, ComponentCallArguments arguments) {
        if (arguments.count() == 1 && arguments.isString(0)) {
            // TODO: Implement named beep
        } else {
            int frequency = arguments.optionalInteger(0, 440);
            if (frequency < 20 || frequency > 2000) {
                return ComponentCallResult.failure("invalid frequency, must be in [20, 2000]");
            }
            double duration = arguments.optionalDouble(1, 0.1);
            int durationInMs = Math.max(50, Math.min(5000, (int) (duration * 1000)));
            context.pause(durationInMs / 1000.0);
            machineSupplier.get().ifPresent(machine -> machine.parameters().actions()
                .beep((short) frequency, (short) durationInMs));
        }
        return ComponentCallResult.success();
    }

    @ComponentMethod(doc = "function():table -- Returns a map of program name to disk label for known programs.")
    public ComponentCallResult getProgramLocations(ComponentCallContext context, ComponentCallArguments arguments) {
        // TODO: Implement
        return ComponentCallResult.success(List.of());
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

    @Override
    public void onConnect(NetworkNode node) {
        if (node.component().isEmpty()) return;
        Component component = node.component().get();
        Optional<Machine> machine = machineSupplier.get();
        machine.ifPresent(m -> m.signal(
            "component_added", node.id().toString(), component.getType()));
    }

    @Override
    public void onDisconnect(NetworkNode node) {
        if (node.component().isEmpty()) return;
        Component component = node.component().get();
        Optional<Machine> machine = machineSupplier.get();
        machine.ifPresent(m -> m.signal(
            "component_removed", node.id().toString(), component.getType()));
    }

}
