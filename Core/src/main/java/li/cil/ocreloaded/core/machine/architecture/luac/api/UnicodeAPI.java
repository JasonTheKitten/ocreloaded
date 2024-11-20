package li.cil.ocreloaded.core.machine.architecture.luac.api;

import java.util.Map;

import li.cil.ocreloaded.core.machine.Machine;
import li.cil.repack.com.naef.jnlua.LuaState;

public final class UnicodeAPI {

    private UnicodeAPI() {}

    public static void register(LuaState luaState, Machine machine) {
        APIRegistrationUtil.register(luaState, machine, "unicode", Map.of(
            "len", UnicodeAPI::len,
            "sub", UnicodeAPI::sub,
            "char", UnicodeAPI::uchar
        ));
    }

    private static int uchar(LuaState luaState, Machine machine) {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= luaState.getTop(); i++) {
            builder.appendCodePoint(luaState.checkInteger(i));
        }
        luaState.pushString(builder.toString());

        return 1;
    }

    private static int len(LuaState luaState, Machine machine) {
        String str = luaState.checkString(1);
        luaState.pushInteger(str.length());
        return 1;
    }

    private static int sub(LuaState luaState, Machine machine) {
        int numArgs = luaState.getTop();
        String str = luaState.checkString(1);
        int start = luaState.checkInteger(2);
        int end = numArgs > 2 ? luaState.checkInteger(3) : 0;

        start = Math.max(0, start < 0 ? str.length() + start + 1 : start - 1);
        end = Math.min(str.length(), end < 0 ? str.length() + end + 1 : end);
        String subStr = end <= start ? "" : str.substring(start, end);
        luaState.pushString(subStr);

        return 1;
    }

}
