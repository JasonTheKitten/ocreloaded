package li.cil.ocreloaded.core.machine.architecture.luac.api;

import java.util.Map;

import li.cil.ocreloaded.core.machine.Machine;
import li.cil.ocreloaded.core.util.TimeFormatterUtil;
import li.cil.ocreloaded.core.util.TimeFormatterUtil.DateTime;
import li.cil.repack.com.naef.jnlua.LuaState;

public final class OSAPI {
    
    private OSAPI() {}
    
    public static void register(LuaState luaState, Machine machine) {
        APIRegistrationUtil.register(luaState, machine, "os", Map.of(
            "clock", OSAPI::clock,
            "date", OSAPI::date
        ));
    }

    private static int clock(LuaState luaState, Machine machine) {
        // TODO: I forget, is uptime the same as CPU time?
        luaState.pushNumber(machine.uptime());
        return 1;
    }

    private static int date(LuaState luaState, Machine machine) {
        String format = luaState.getTop() > 0 && luaState.isString(1) ?
            luaState.toString(1) :
            "%d/%m/%Y %H:%M:%S";
        
        long time = luaState.getTop() > 1 && luaState.isNumber(2) ?
            (long) luaState.toNumber(2) :
            // TODO: Use world time
            System.currentTimeMillis() / 1000;

        DateTime dateTime = TimeFormatterUtil.parse(time);
        if (format.startsWith("!")) {
            format = format.substring(1);
        }

        if (format.equals("*t")) {
            luaState.newTable(0, 8);
            luaState.pushInteger(dateTime.year());
            luaState.setField(-2, "year");
            luaState.pushInteger(dateTime.month());
            luaState.setField(-2, "month");
            luaState.pushInteger(dateTime.day());
            luaState.setField(-2, "day");
            luaState.pushInteger(dateTime.hour());
            luaState.setField(-2, "hour");
            luaState.pushInteger(dateTime.minute());
            luaState.setField(-2, "min");
            luaState.pushInteger(dateTime.second());
            luaState.setField(-2, "sec");
            luaState.pushInteger(dateTime.weekDay());
            luaState.setField(-2, "wday");
            luaState.pushInteger(dateTime.yearDay());
            luaState.setField(-2, "yday");
        } else {
            luaState.pushString(TimeFormatterUtil.format(format, dateTime));
        }

        return 1;
    }

    // TODO: time

}
