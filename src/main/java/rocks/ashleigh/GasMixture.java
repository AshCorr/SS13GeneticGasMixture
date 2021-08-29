package rocks.ashleigh;

import lombok.Data;
import rocks.ashleigh.reactions.GasReactionHydrogenFire;
import rocks.ashleigh.reactions.GasReactionPlasmaFire;
import rocks.ashleigh.reactions.GasReactionTritiumFire;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class GasMixture {
    private double volume = 70;
    private double temperature;
    private Map<GasType, Double> moles;

    public GasMixture(double temperature, double volume, Map<GasType, Double> moles) {
        this.volume = volume;
        this.temperature = temperature;
        this.moles = moles;
    }

    public GasMixture(double temperature, double volume) {
        this.volume = volume;
        this.temperature = temperature;
        this.moles = new HashMap<>();
    }

    public GasMixture(double temperature, Map<GasType, Double> moles) {
        this.temperature = temperature;
        this.moles = moles;
    }

    public GasMixture(double temperature) {
        this.temperature = temperature;
        moles = new HashMap<>();
    }

    public Double getMoles(GasType gasType) {
        return moles.getOrDefault(gasType, 0.0);
    }

    public void setMoles(GasType gasType, double amount) {
        moles.put(gasType, amount);
    }

    public void adjustMoles(GasType gasType, double amount) {
        if (getMoles(gasType) + amount < 0)
            amount = 0;

        setMoles(gasType, getMoles(gasType) + amount);
    }

    public double getHeatCapacity() {
        return Math.max(moles.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getSpecificHeatCapacity() * entry.getValue())
                .sum(), 0);
    }

    public GasMixture merge(GasMixture other) {
        double newTemperature = temperature;

        if(Math.abs(temperature - other.getTemperature()) > Constants.MAXIMUM_TEMPERATURE_DELTA_TO_CONSIDER) {
            double thisHeatCapacity = getHeatCapacity();
            double otherHeatCapacity = other.getHeatCapacity();
            double combinedHeatCapacity = thisHeatCapacity + otherHeatCapacity;

            newTemperature = (other.getTemperature() * otherHeatCapacity + temperature * thisHeatCapacity) / combinedHeatCapacity;
        }

        Map<GasType, Double> newMoles = new HashMap<>(moles);

        for (Map.Entry<GasType, Double> entry : other.getMoles().entrySet()) {
            newMoles.put(entry.getKey(), newMoles.getOrDefault(entry.getKey(), 0.0) + entry.getValue());
        }

        return new GasMixture(newTemperature, volume, newMoles);
    }

    public double getTotalMoles() {
        return moles.values().stream().mapToDouble(value -> value).sum();
    }

    public double getPressure() {
        return getTotalMoles() * Constants.IDEAL_GAS_CONSTANT * temperature / volume;
    }

    @Override
    public String toString() {
        return String.format("{ temperature=%s, pressure=%s, volume=%s moles=%s }", temperature, getPressure(), volume, moles);
    }

    public double getThermalEnergy() {
        return temperature * getHeatCapacity();
    }

    public void react(List<GasReaction> reactions) {
        for (GasReaction gasReaction : reactions) {
            if (gasReaction.canReact(this))
                gasReaction.handle(this);
        }
    }
}
