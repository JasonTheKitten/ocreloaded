package li.cil.ocreloaded.core.machine.architecture.luac;

import li.cil.ocreloaded.core.machine.architecture.component.base.ComponentCallArguments;
import li.cil.repack.com.naef.jnlua.LuaState;

public class LuaCComponentCallArguments implements ComponentCallArguments {
    
    private final LuaState luaState;
    private final int index;
    private final int count;
        
    public LuaCComponentCallArguments(LuaState luaState, int index, int count) {
        this.luaState = luaState;
        this.index = index;
        this.count = count;
    }

    @Override
    public String optionalString(int index, String defaultValue) {
        if (index < count && !luaState.isString(this.index + index)) {
            throw new IllegalArgumentException(
                "Expected string at index " + index + ", got " + luaState.typeName(this.index + index));
        }

        return index < count ?
            luaState.toString(this.index + index) :
            defaultValue;
    }

}
