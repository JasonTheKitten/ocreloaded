package li.cil.ocreloaded.core.machine.component;

public interface ComponentCallArguments {

    String checkString(int index);

    String optionalString(int index, String defaultValue);

    int checkInteger(int index);

    int optionalInteger(int index, int defaultValue);

    boolean checkBoolean(int index);

    boolean optionalBoolean(int index, boolean defaultValue);

    boolean isNil(int index);

}
