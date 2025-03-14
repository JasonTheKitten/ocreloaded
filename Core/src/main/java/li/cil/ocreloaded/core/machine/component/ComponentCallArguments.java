package li.cil.ocreloaded.core.machine.component;

public interface ComponentCallArguments {

    int count();

    String checkString(int index);

    String optionalString(int index, String defaultValue);

    boolean isString(int index);

    int checkInteger(int index);

    int optionalInteger(int index, int defaultValue);

    double optionalDouble(int index, double defaultValue);

    boolean checkBoolean(int index);

    boolean optionalBoolean(int index, boolean defaultValue);

    boolean isNil(int index);

}
