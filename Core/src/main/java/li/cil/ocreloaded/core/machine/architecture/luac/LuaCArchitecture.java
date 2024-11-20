package li.cil.ocreloaded.core.machine.architecture.luac;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import li.cil.ocreloaded.core.machine.Machine;
import li.cil.ocreloaded.core.machine.MachineResult;
import li.cil.ocreloaded.core.machine.architecture.Architecture;
import li.cil.ocreloaded.core.machine.architecture.luac.api.ComponentAPI;
import li.cil.ocreloaded.core.machine.architecture.luac.api.ComputerAPI;
import li.cil.ocreloaded.core.machine.architecture.luac.api.OSAPI;
import li.cil.ocreloaded.core.machine.architecture.luac.api.SystemAPI;
import li.cil.repack.com.naef.jnlua.LuaState;
import li.cil.repack.com.naef.jnlua.LuaState.Library;
import li.cil.repack.com.naef.jnlua.LuaType;

public class LuaCArchitecture implements Architecture {

    private static final Logger LOGGER = LoggerFactory.getLogger(LuaCArchitecture.class);

    private static final List<BiConsumer<LuaState, Machine>> API_REGISTRATIONS = List.of(
        ComputerAPI::register, OSAPI::register, ComponentAPI::register, SystemAPI::register
    );

    private static final List<Library> LIBRARIES = List.of(
        Library.BASE, Library.BIT32, Library.COROUTINE, Library.DEBUG, Library.ERIS, Library.MATH, Library.STRING, Library.TABLE
    );

    private final LuaCStateFactory factory;
    private final Machine machine;
    
    private Optional<LuaState> luaState = Optional.empty();
    private ArchState state = ArchState.STOPPED;
    
    public LuaCArchitecture(LuaCStateFactory factory, Machine machine) {
        this.factory = factory;
        this.machine = machine;
    }

    @Override
    public MachineResult start(InputStream codeStream) {
        if (luaState.isPresent()) {
            stop();
        }

        LuaState luaState = factory.create();
        this.luaState = Optional.of(luaState);

        LIBRARIES.forEach(luaState::openLib);
        luaState.pop(LIBRARIES.size());

        API_REGISTRATIONS.forEach(consumer -> consumer.accept(luaState, machine));

        try {
            luaState.load(codeStream, "=machine", "t");
            luaState.newThread();
        } catch (IOException e) {
            return new MachineResult.Error("Failed to read startup code!");
        }
        int result = luaState.resume(1, 0);
        // TODO: What if something *was* returned?

        assert result == 0;

        state = ArchState.YIELD;
        return new MachineResult.ExecAsync();
    }

    @Override
    public MachineResult resume() {
        if (luaState.isEmpty()) {
            return new MachineResult.Error("Machine not running");
        }
        LuaState l = luaState.get();
        LoggerFactory.getLogger(LuaCArchitecture.class).info("Resuming machine with state: " + state);
        return switch (state) {
            case STOPPED -> new MachineResult.Error("Cannot resume");
            case SYNC_CALL -> shield(() -> handleSyncCall(l));
            case SYNC_RESULT -> shield(() -> handleSyncCallResult(l));
            case YIELD -> shield(() -> handleNormal(l));
            default -> new MachineResult.Error("Invalid state");
        };
    }

    @Override
    public void stop() {
        luaState.ifPresent(LuaState::close);
        luaState = Optional.empty();
    }

    private MachineResult handleSyncCall(LuaState l) {
        // Stack: Function, Thread
        if (!(l.getTop() == 2 && l.isThread(1) && l.isFunction(2))) {
            return new MachineResult.Error("Invalid stack state");
        }

        l.call(0, 1);
        l.checkType(2, LuaType.TABLE);

        state = ArchState.SYNC_RESULT;

        // Returning table result
        return new MachineResult.ExecAsync();
    }

    private MachineResult handleSyncCallResult(LuaState l) {
        // Stack: Table, Thread
        if (!(l.getTop() == 2 && l.isThread(1) && l.isTable(2))) {
            return new MachineResult.Error("Invalid stack state");
        }

        int results = l.resume(1, 1);

        return determineMachineResult(l, results);
    }

    private MachineResult handleNormal(LuaState l) {
        // TODO: Push the event queue
        int results = l.resume(1, 0);

        return determineMachineResult(l, results);
    }

    private MachineResult shield(Supplier<MachineResult> runnable) {
        try {
            return runnable.get();
        } catch (Exception e) {
            // TODO: More versatile error handling
            return new MachineResult.Error(e.getMessage());
        }
    }

    private MachineResult determineMachineResult(LuaState l, int results) {
        if (!l.isThread(1)) {
            return new MachineResult.Error("Invalid stack state");
        }
        if (l.status(1) != LuaState.YIELD) {
            return handleDeadVM(l);
        }

        int waitTicks = Integer.MAX_VALUE;
        if (results == 1 && l.isFunction(2)) {
            // Sync Call (run a method on render thread, and it returns a table result)
            state = ArchState.SYNC_CALL;
            return new MachineResult.ExecSync();
        } else if (results == 1 && l.isBoolean(2)) {
            // Power state
            state = ArchState.STOPPED;
            return new MachineResult.Stop(l.toBoolean(2));
        } else if (results == 1 && l.isNumber(2)) {
            // Bounded Wait
            waitTicks = l.toInteger(2) * 20;
            // No Ret
        }
        l.pop(results);
        return new MachineResult.Wait(waitTicks);
    }

    private MachineResult handleDeadVM(LuaState l) {
        if (!l.isThread(1)) {
            return badExit("Invalid stack state");
        }

        // Machine.lua runs a pcall, so if we don't get (bool, message?) something went very wrong
        if (!l.isBoolean(2) && !(l.isString(3) || l.isNoneOrNil(3))) {
            LOGGER.error("Kernel returned unexpected results.");
        }

        // pcall is designed to not exit
        if (l.toBoolean(2)) {
            return badExit("Kernel stopped unexpectedly");
        }

        // TODO: Handle memory
        String error = l.isJavaObjectRaw(3) ? l.toJavaObjectRaw(3).toString() : l.toString(3);
        error = error != null ? error : "unknown error";

        return new MachineResult.Error(error);
    }

    private MachineResult badExit(String message) {
        LOGGER.error(message);
        return new MachineResult.Error(message);
    }

    private static enum ArchState {
        STOPPED, SYNC_CALL, SYNC_RESULT, YIELD
    }

}
