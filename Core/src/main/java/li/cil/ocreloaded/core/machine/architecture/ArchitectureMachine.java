package li.cil.ocreloaded.core.machine.architecture;

import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import li.cil.ocreloaded.core.machine.Machine;
import li.cil.ocreloaded.core.machine.MachineParameters;
import li.cil.ocreloaded.core.machine.MachineResult;
import li.cil.ocreloaded.core.machine.component.Component;
import li.cil.ocreloaded.core.network.NetworkNode;

public class ArchitectureMachine implements Machine {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArchitectureMachine.class);

    private final Architecture architecture;
    private final MachineParameters parameters;

    private final Deque<State> state = new ArrayDeque<>(List.of(State.STOPPED));
    private final Queue<Signal> signals = new ArrayDeque<>();

    private int waitTicks = 0;
    private long startTime = 0;
    private long cpuTime = 0;
    private int uptime = 0;

    public ArchitectureMachine(Function<Machine, Architecture> architectureFactory, MachineParameters parameters) {
        this.architecture = architectureFactory.apply(this);
        this.parameters = parameters;
    }

    @Override
    public boolean start() {
        switch (state.peek()) {
            case STOPPED:
                return startInternal();
            default:
                return false;
        }
    }


    @Override
    public void stop() {
        switch (state.peek()) {
            case STOPPED:
                break;
            default:
                architecture.stop();
        }
        
    }

    @Override
    public void runSync() {
        if (state.peek() == State.STARTING) {
            state.pop();
            setState(State.ASYNC);
            return;
        } else if (state.peek() == State.RESTARTING) {
            startInternal();
            return;
        }

        if (state.peek() == State.STOPPED || state.peek() == State.STOPPING) return;
        uptime++;

        if (state.peek() == State.WAIT || state.peek() == State.PAUSED) {
            waitTicks--;
            // TODO: Event queue
            if (waitTicks <= 0 || (!signals.isEmpty() && state.peek() == State.WAIT)) {
                state.pop();
                setState(State.ASYNC);
            }
        }

        if (state.peek() != State.SYNC) return;
        state.pop();
        startTime = System.nanoTime();
        handleMachineResult(architecture.resume());
        cpuTime += System.nanoTime() - startTime;
    }

    @Override
    public void runAsync() {
        if (state.peek() != State.ASYNC) return;
        state.pop();

        startTime = System.nanoTime();
        handleMachineResult(architecture.resume());
        cpuTime += System.nanoTime() - startTime;
    }

    @Override
    public boolean signal(String name, Object... args) {
        signals.add(new Signal(name, args));

        return true;
    }

    @Override
    public Signal popSignal() {
        if (signals.isEmpty()) return null;
        return signals.poll();
    }

    @Override
    public double cpuTime() {
        return (cpuTime + (System.nanoTime() - startTime)) / 1_000_000.0;
    }

    @Override
    public double uptime() {
        return uptime / 20.0;
    }

    @Override
    public MachineParameters parameters() {
        return parameters;
    }

    @Override
    public boolean pause(double seconds) {
        int ticksToPause = Math.max(1, (int) (seconds * 20));
        boolean shouldPause;
        synchronized(state) {
            shouldPause = shouldPause(ticksToPause);
        }
        if (shouldPause) {
            synchronized(this) {
                synchronized(state) {
                    if (state.peek() != State.PAUSED) {
                        assert !state.contains(State.PAUSED);
                        state.push(State.PAUSED);
                    }
                    waitTicks = ticksToPause;
                    return true;
                }
            }
        }

        return false;
    }

    private boolean startInternal() {
        Optional<InputStream> codeStream = parameters.codeStreamSupplier().get();
        if (codeStream.isEmpty()) return false;

        this.state.clear();
        this.state.push(State.STARTING);
        uptime = 0;
        MachineResult result = architecture.start(codeStream.get());
        handleMachineResult(result);

        return !(result instanceof MachineResult.Error);
    }

    private void handleMachineResult(MachineResult result) {
        if (result instanceof MachineResult.ExecSync) {
            setState(State.SYNC);
        } else if (result instanceof MachineResult.ExecAsync) {
            setState(State.ASYNC);
        } else if (result instanceof MachineResult.Wait waitResult) {
            setState(State.WAIT);
            waitTicks = waitResult.ticks();
        } else if (result instanceof MachineResult.Stop stopResult) {
            setState(State.STOPPING);
            stop();
            setState(stopResult.restart() ? State.RESTARTING : State.STOPPED);
        } else if (result instanceof MachineResult.Error error) {
            setState(State.STOPPING);
            stop();
            setState(State.STOPPED);
            // TODO: Proper error handling
            LOGGER.error(error.message(), error.exception());
        }
    }

    private void setState(State state) {
        boolean isPaused = this.state.peek() == State.PAUSED;
        if (this.state.peek() == State.PAUSED) {
            this.state.pop();
        }
        switch (state) {
            case ASYNC:
                this.state.push(State.ASYNC);
                parameters.threadService().execute(this::runAsync);
                break;
            case STOPPED:
                this.state.clear();
                this.state.push(State.STOPPED);
                break;
            default:
                this.state.push(state);
                break;
        }
        if (isPaused) {
            this.state.push(State.PAUSED);
        }
    }

    private boolean shouldPause(int ticksToPause) {
        return switch (state.peek()) {
            case STOPPED -> false;
            case PAUSED -> ticksToPause > waitTicks;
            default -> true;
        };
    }

    public Map<UUID, NetworkedComponent> getComponents() {
        NetworkNode computerNetworkNode = parameters.networkNode();
        return computerNetworkNode.reachableNodes().stream()
            .filter(node -> node.component().isPresent())
            .collect(() -> new HashMap<>(Map.of(
                computerNetworkNode.id(), new NetworkedComponent(computerNetworkNode.component().get(), computerNetworkNode)
            )), (m, node) -> m.put(node.id(), new NetworkedComponent(node.component().get(), computerNetworkNode)), Map::putAll);
    }

    public static record NetworkedComponent(Component component, NetworkNode networkNode) {}

    private static enum State {
        STOPPED, STOPPING, STARTING, SYNC, ASYNC, WAIT, PAUSED, RESTARTING
    }
    
}
