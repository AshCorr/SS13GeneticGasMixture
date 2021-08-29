package rocks.ashleigh;

public enum GasType {
    PLASMA(200),
    OXYGEN(20),
    HYDROGEN(15),
    TRITIUM(10),
    CARBON_DIOXIDE(30),
    WATER_VAPOUR(40);


    GasType(double specificHeatCapacity) {
        this.specificHeatCapacity = specificHeatCapacity;
    }

    double specificHeatCapacity;

    public double getSpecificHeatCapacity() {
        return specificHeatCapacity;
    }
}
