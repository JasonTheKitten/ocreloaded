package li.cil.ocreloaded.core.machine.architecture.luac.api;

import java.util.Map;

import li.cil.ocreloaded.core.machine.Machine;
import li.cil.ocreloaded.core.util.ExtendedUnicodeUtil;
import li.cil.ocreloaded.core.util.TextUtil;
import li.cil.repack.com.naef.jnlua.LuaState;

public final class UnicodeAPI {

    private UnicodeAPI() {}

    public static void register(LuaState luaState, Machine machine) {
        APIRegistrationUtil.register(luaState, machine, "unicode", Map.of(
            "lower", UnicodeAPI::lower,
            "upper", UnicodeAPI::upper,
            "char", UnicodeAPI::uchar,
            "len", UnicodeAPI::len,
            "reverse", UnicodeAPI::reverse,
            "sub", UnicodeAPI::sub,
            "isWide", UnicodeAPI::isWide,
            "charWidth", UnicodeAPI::charWidth,
            "wlen", UnicodeAPI::wlen,
            "wtrunc", UnicodeAPI::wtrunc
        ));
    }

    private static int lower(LuaState luaState, Machine machine) {
        String str = luaState.checkString(1);
        luaState.pushString(str.toLowerCase());
        return 1;
    }

    private static int upper(LuaState luaState, Machine machine) {
        String str = luaState.checkString(1);
        luaState.pushString(str.toUpperCase());
        return 1;
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
        luaState.pushInteger(str.codePointCount(0, str.length()));
        return 1;
    }

    private static int reverse(LuaState luaState, Machine machine) {
        String str = luaState.checkString(1);
        luaState.pushString(ExtendedUnicodeUtil.reverse(str));
        return 1;
    }

    private static int sub(LuaState luaState, Machine machine) {
        int numArgs = luaState.getTop();
        String str = luaState.checkString(1);
        int start = luaState.checkInteger(2);
        int end = numArgs > 2 ? luaState.checkInteger(3) : str.length();

        int sLength = ExtendedUnicodeUtil.length(str);
        if (start < 0) {
            start = str.offsetByCodePoints(str.length(), Math.max(start, -sLength));
        } else if (start > 0) {
            start = str.offsetByCodePoints(0, Math.min(start - 1, sLength));
        }

        if (end < 0) {
            end = str.offsetByCodePoints(str.length(), Math.max(end + 1, -sLength));
        } else if (end > 0) {
            end = str.offsetByCodePoints(0, Math.min(end, sLength));
        }

        if (end <= start) {
            luaState.pushString("");
        } else {
            luaState.pushString(str.substring(start, end));
        }

        return 1;
    }

    private static int isWide(LuaState luaState, Machine machine) {
        String str = luaState.checkString(1);
        luaState.pushBoolean(TextUtil.wcwidth(str.codePointAt(0)) > 1);
        return 1;
    }

    private static int charWidth(LuaState luaState, Machine machine) {
        String str = luaState.checkString(1);
        luaState.pushInteger(TextUtil.wcwidth(str.codePointAt(0)));
        return 1;
    }

    private static int wlen(LuaState luaState, Machine machine) {
        String str = luaState.checkString(1);
        int totalLength = str.codePoints().map(TextUtil::wcwidth).sum();
        luaState.pushInteger(totalLength);
        return 1;
    }

    private static int wtrunc(LuaState luaState, Machine machine) {
        String str = luaState.checkString(1);
        int count = luaState.checkInteger(2);
        int width = 0;
        int end = 0;
        while (width < count && end < str.length()) {
            width += Math.max(1, TextUtil.wcwidth(str.codePointAt(end)));
            end = str.offsetByCodePoints(end, 1);
        }
        if (end > 1) {
            luaState.pushString(str.substring(0, end - 1));
        } else {
            luaState.pushString("");
        }

        return 1;
    }

}
