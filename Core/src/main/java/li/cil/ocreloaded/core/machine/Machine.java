package li.cil.ocreloaded.core.machine;

public interface Machine {
    
    boolean start();

    void stop();

    void runSync();

    void runAsync();

}
