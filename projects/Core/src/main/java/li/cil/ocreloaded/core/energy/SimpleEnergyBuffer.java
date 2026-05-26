package li.cil.ocreloaded.core.energy;

public class SimpleEnergyBuffer implements EnergyBuffer {

    private final double capacity;
    private double energy;

    public SimpleEnergyBuffer(double capacity) {
        this.capacity = capacity;
        this.energy = 0;
    }

    @Override
    public double getEnergy() {
        return energy;
    }

    @Override
    public double getCapacity() {
        return capacity;
    }

    @Override
    public double insert(double amount) {
        double inserted = Math.min(Math.max(0, amount), capacity - energy);
        energy += inserted;
        return inserted;
    }

    @Override
    public double extract(double amount) {
        double extracted = Math.min(Math.max(0, amount), energy);
        energy -= extracted;
        return extracted;
    }

    public void setEnergy(double amount) {
        this.energy = Math.max(0, Math.min(amount, capacity));
    }

}
