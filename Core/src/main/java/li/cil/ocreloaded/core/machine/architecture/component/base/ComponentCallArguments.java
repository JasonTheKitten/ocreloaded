package li.cil.ocreloaded.core.machine.architecture.component.base;

public interface ComponentCallArguments {

    String optionalString(int index, String defaultValue);

    /*public String optionalString(int index, String defaultValue) {
        if (index < rawArguments.length && !(rawArguments[index] instanceof String)) {
            throw new IllegalArgumentException(
                "Expected string at index " + index + ", got " + rawArguments[index].getClass().getSimpleName());
        }

        return index < rawArguments.length ?
            (String) rawArguments[index] :
            defaultValue;
    }*/

}
