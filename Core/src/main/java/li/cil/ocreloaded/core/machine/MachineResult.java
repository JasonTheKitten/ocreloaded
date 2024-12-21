package li.cil.ocreloaded.core.machine;

public sealed interface MachineResult {
    
    public record ExecAsync() implements MachineResult {}

    public record ExecSync() implements MachineResult {}

    public record Wait(int ticks) implements MachineResult {}

    public record Stop(boolean restart) implements MachineResult {}

    public record Error(String message, Throwable exception) implements MachineResult {
        public Error(String message) {
            this(message, null);
        }
    }

}
