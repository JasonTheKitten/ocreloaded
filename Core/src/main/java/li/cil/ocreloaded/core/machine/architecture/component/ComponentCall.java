package li.cil.ocreloaded.core.machine.architecture.component;

public interface ComponentCall {
    
    ComponentCallResult call();

    ComponentMethod getAnnotation();

    public static record ComponentCallResult(String error, Object[] result) {
        public static ComponentCallResult success(Object... result) {
            return new ComponentCallResult(null, result);
        }

        public static ComponentCallResult failure(String error) {
            return new ComponentCallResult(error, null);
        }
    }

}
