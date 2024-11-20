package li.cil.ocreloaded.core.machine.architecture.component.base;

public interface ComponentCallArguments {

    String checkString(int index);

    String optionalString(int index, String defaultValue);

    int checkInteger(int index);

    int optionalInteger(int index, int defaultValue);

}
