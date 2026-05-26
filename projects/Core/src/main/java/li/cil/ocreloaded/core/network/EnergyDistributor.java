package li.cil.ocreloaded.core.network;

public interface EnergyDistributor {

    double getEnergyStored();

    double getEnergyCapacity();

    double changeEnergy(double delta);

}
