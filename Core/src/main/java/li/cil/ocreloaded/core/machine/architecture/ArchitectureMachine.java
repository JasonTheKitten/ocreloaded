package li.cil.ocreloaded.core.machine.architecture;

import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.LoggerFactory;

import li.cil.ocreloaded.core.machine.Component;
import li.cil.ocreloaded.core.machine.Machine;
import li.cil.ocreloaded.core.machine.MachineParameters;
import li.cil.ocreloaded.core.machine.MachineResult;

public class ArchitectureMachine implements Machine {

    private final Architecture architecture;
    private final MachineParameters parameters;

    private final Deque<State> state = new ArrayDeque<>(List.of(State.STOPPED));

    private int waitTicks = 0;

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
        }

        if (state.peek() == State.WAIT) {
            waitTicks--;
            // TODO: Event queue
            if (waitTicks <= 0) {
                state.pop();
                state.push(State.ASYNC);
            }
        }

        if (state.peek() != State.SYNC) return;
        state.pop();
        handleMachineResult(architecture.resume());
    }

    @Override
    public void runAsync() {
        if (state.peek() != State.ASYNC) return;
        state.pop();

        handleMachineResult(architecture.resume());
    }

    private boolean startInternal() {
        Optional<InputStream> codeStream = parameters.codeStreamSupplier().get();
        if (codeStream.isEmpty()) return false;

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
            stop();
            setState(State.STOPPED);
            if (stopResult.restart()) {
                startInternal();
            }
        } else if (result instanceof MachineResult.Error error) {
            stop();
            setState(State.STOPPED);
            // TODO: Proper error handling
            LoggerFactory.getLogger(getClass()).error(error.message());
        }
    }

    private void setState(State state) {
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
    }

    public List<Component> getComponents() {
        return parameters.componentScanner().get();
    }

    private static enum State {
        STOPPED, STARTING, SYNC, ASYNC, WAIT, PAUSED
    }
    
}
