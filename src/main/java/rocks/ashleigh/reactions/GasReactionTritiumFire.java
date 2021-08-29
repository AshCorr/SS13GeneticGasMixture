package rocks.ashleigh.reactions;

import rocks.ashleigh.GasMixture;
import rocks.ashleigh.GasReaction;
import rocks.ashleigh.GasType;

import static rocks.ashleigh.Constants.*;

public class GasReactionTritiumFire extends GasReaction {
    @Override
    public boolean canReact(GasMixture air) {
        return air.getTemperature() >= FIRE_MINIMUM_TEMPERATURE_TO_EXIST
                && air.getMoles(GasType.TRITIUM) >= MINIMUM_MOLE_COUNT
                && air.getMoles(GasType.OXYGEN) >= MINIMUM_MOLE_COUNT;
    }

    @Override
    public void handle(GasMixture air) {
        double energyReleased = 0;
        double oldHeatCapacity = air.getHeatCapacity();
        double temperature = air.getTemperature();
        double burnedFuel = 0;
        double initialTritium = air.getMoles(GasType.TRITIUM);

        if (air.getMoles(GasType.OXYGEN) < initialTritium || MINIMUM_TRIT_OXYBURN_ENERGY > air.getThermalEnergy()) {
            burnedFuel = air.getMoles(GasType.OXYGEN) / TRITIUM_BURN_OXY_FACTOR;
            if (burnedFuel > initialTritium)
                burnedFuel = initialTritium;

            air.adjustMoles(GasType.TRITIUM, -burnedFuel);
        } else {
            burnedFuel = initialTritium;
            // lovely number gore
            air.setMoles(GasType.TRITIUM, initialTritium * (1 - 1 / TRITIUM_BURN_TRIT_FACTOR));
            air.adjustMoles(GasType.OXYGEN, -air.getMoles(GasType.TRITIUM));
            energyReleased = (FIRE_HYDROGEN_ENERGY_RELEASED * burnedFuel * (TRITIUM_BURN_TRIT_FACTOR - 1));
        }

        if (burnedFuel > 0) {
            energyReleased += FIRE_HYDROGEN_ENERGY_RELEASED * burnedFuel;

            // TODO: Minmax radiation?

            air.adjustMoles(GasType.WATER_VAPOUR, burnedFuel);
        }

        if (energyReleased > 0) {
            double newHeatCapacity = air.getHeatCapacity();
            if (newHeatCapacity > MINIMUM_HEAT_CAPACITY)
                air.setTemperature((temperature * oldHeatCapacity + energyReleased) / newHeatCapacity);
        }
    }
}
