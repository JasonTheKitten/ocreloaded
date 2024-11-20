package li.cil.ocreloaded.core.machine;

public interface Machine {
    
    boolean start();

    void stop();

    void runSync();

    void runAsync();

    boolean signal(String name, Object... args);

    Signal popSignal();
    
    long uptime();

    MachineParameters parameters();

    public static record Signal(String name, Object[] args) {}

}
