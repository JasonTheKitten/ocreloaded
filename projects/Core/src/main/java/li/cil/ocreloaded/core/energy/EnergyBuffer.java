package li.cil.ocreloaded.core.energy;

public interface EnergyBuffer {

    double getEnergy();

    double getCapacity();

    double insert(double amount);

    double extract(double amount);

    void setEnergy(double amount);

}
