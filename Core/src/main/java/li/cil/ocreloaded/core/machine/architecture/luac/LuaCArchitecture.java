package li.cil.ocreloaded.core.machine.architecture.luac;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import li.cil.ocreloaded.core.machine.Machine;
import li.cil.ocreloaded.core.machine.architecture.Architecture;
import li.cil.repack.com.naef.jnlua.LuaState;
import li.cil.repack.com.naef.jnlua.LuaState.Library;

public class LuaCArchitecture implements Architecture {

    private final LuaCStateFactory factory;
    
    private Optional<LuaState> luaState = Optional.empty();
    
    public LuaCArchitecture(LuaCStateFactory factory, Machine machine) {
        this.factory = factory;
    }

    @Override
    public boolean start(InputStream codeStream) {
        if (luaState.isPresent()) {
            stop();
        }

        LuaState luaState = factory.create();
        this.luaState = Optional.of(luaState);

        luaState.openLib(Library.BASE);
        try {
            luaState.load(codeStream, "=machine", "t");
        } catch (IOException e) {
            return false;
        }
        luaState.call(0, 0);

        return true;
    }

    @Override
    public void stop() {
        luaState.ifPresent(LuaState::close);
        luaState = Optional.empty();
    }

}
