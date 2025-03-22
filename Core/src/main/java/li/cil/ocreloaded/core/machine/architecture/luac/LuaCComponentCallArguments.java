package li.cil.ocreloaded.core.machine.architecture.luac;

import li.cil.ocreloaded.core.machine.component.ComponentCallArguments;
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
    public int count() {
        return count;
    }

    @Override
    public String checkString(int index) {
        return luaState.checkString(this.index + index);
    }

    @Override
    public String optionalString(int index, String defaultValue) {
        if (index >= count || luaState.isNoneOrNil(this.index + index)) {
            return defaultValue;
        }

        return luaState.checkString(this.index + index);
    }

    @Override
    public boolean isString(int index) {
        return luaState.isString(this.index + index);
    }

    @Override
    public int checkInteger(int index) {
        double number = luaState.toNumber(this.index + index);
        return number > Integer.MAX_VALUE ? Integer.MAX_VALUE : number < Integer.MIN_VALUE ? Integer.MIN_VALUE : (int) number;
    }

    @Override
    public int optionalInteger(int index, int defaultValue) {
        if (index >= count || luaState.isNoneOrNil(this.index + index)) {
            return defaultValue;
        }

        return checkInteger(index);
    }

    @Override
    public double optionalDouble(int index, double defaultValue) {
        if (index >= count || luaState.isNoneOrNil(this.index + index)) {
            return defaultValue;
        }

        return luaState.toNumber(this.index + index);
    }

    @Override
    public boolean checkBoolean(int index) {
        return luaState.toBoolean(this.index + index);
    }

    @Override
    public boolean optionalBoolean(int index, boolean defaultValue) {
        if (index >= count || luaState.isNoneOrNil(this.index + index)) {
            return defaultValue;
        }

        return checkBoolean(index);
    }

    @Override
    public boolean isNil(int index) {
        return luaState.isNoneOrNil(this.index + index);
    }

}
