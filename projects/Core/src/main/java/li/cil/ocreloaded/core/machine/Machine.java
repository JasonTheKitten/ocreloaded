package li.cil.ocreloaded.core.machine;

public interface Machine {
    
    boolean start();

    void stop();

    void runSync();

    void runAsync();

    boolean signal(String name, Object... args);

    Signal popSignal();

    double cpuTime();
    
    double uptime();

    MachineParameters parameters();

    boolean pause(double duration);

    public static record Signal(String name, Object[] args) {}

}
