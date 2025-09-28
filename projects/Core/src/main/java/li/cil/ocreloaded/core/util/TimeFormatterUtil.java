package li.cil.ocreloaded.core.util;

import java.util.AbstractMap.SimpleEntry;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.function.Function;

public final class TimeFormatterUtil {
    
    private TimeFormatterUtil() {}

    private static final String[] WEEKDAYS = new String[] {
        "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    };

    private static final String[] SHORT_WEEKDAYS = new String[] {
        "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
    };

    private static final String[] MONTHS = new String[] {
        "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
    };

    private static final String[] SHORT_MONTHS = new String[] {
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private static final String[] AM_PM = new String[] { "AM", "PM" };

    private static final Map<Character, Function<DateTime, String>> SPECIFIERS = Map.ofEntries(
        entry('a', t -> SHORT_WEEKDAYS[t.weekDay() - 1]),
        entry('A', t -> WEEKDAYS[t.weekDay() - 1]),
        entry('b', t -> SHORT_MONTHS[t.month() - 1]),
        entry('B', t -> MONTHS[t.month() - 1]),
        entry('c', t -> format("%a %b %e %H:%M:%S %Y", t)),
        entry('C', t -> String.format("%02d", t.year() / 100)),
        entry('d', t -> String.format("%02d", t.day())),
        entry('e', t -> String.format("%2d", t.day())),
        entry('H', t -> String.format("%02d", t.hour())),
        entry('I', t -> String.format("%02d", (t.hour() + 11) % 12 + 1)),
        entry('j', t -> String.format("%03d", t.yearDay())),
        entry('m', t -> String.format("%02d", t.month())),
        entry('M', t -> String.format("%02d", t.minute())),
        entry('p', t -> AM_PM[t.hour() / 12]),
        entry('S', t -> String.format("%02d", t.second())),
        entry('w', t -> String.valueOf(t.weekDay() - 1)),
        entry('W', t -> String.format("%02d", (t.yearDay() + 6) / 7)),
        entry('x', t -> format("%m/%d/%y", t)),
        entry('X', t -> format("%H:%M:%S", t)),
        entry('y', t -> String.format("%02d", t.year() % 100)),
        entry('Y', t -> String.format("%04d", t.year()))
    );

    public static DateTime parse(long time) {
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(time * 1000);

        return new DateTime(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.DAY_OF_WEEK),
            calendar.get(Calendar.DAY_OF_YEAR),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND));
    }
    
    public static String format(String format, DateTime dateTime) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < format.length(); i++) {
            char c = format.charAt(i);
            if (c == '%') {
                if (++i == format.length()) {
                    break;
                }
                c = format.charAt(i);
                result.append(SPECIFIERS.getOrDefault(c, t -> "").apply(dateTime));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private static Entry<Character, Function<DateTime, String>> entry(char c, Function<DateTime, String> f) {
        return new SimpleEntry<>(c, f);
    }

    public static record DateTime(int year, int month, int day, int weekDay, int yearDay, int hour, int minute, int second) {}

}
