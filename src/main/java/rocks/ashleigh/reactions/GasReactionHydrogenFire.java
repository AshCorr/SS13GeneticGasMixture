package rocks.ashleigh.reactions;

import rocks.ashleigh.GasMixture;
import rocks.ashleigh.GasReaction;
import rocks.ashleigh.GasType;

import static rocks.ashleigh.Constants.*;

public class GasReactionHydrogenFire extends GasReaction {
    @Override
    public boolean canReact(GasMixture air) {
        return air.getTemperature() >= FIRE_MINIMUM_TEMPERATURE_TO_EXIST
                && air.getMoles(GasType.HYDROGEN) >= MINIMUM_MOLE_COUNT
                && air.getMoles(GasType.OXYGEN) >= MINIMUM_MOLE_COUNT;
    }

    @Override
    public void handle(GasMixture air) {
        double energyReleased = 0;
        double oldHeadCapacity = air.getHeatCapacity();
        double temperature = air.getTemperature();
        double burnedFuel;

        if (air.getMoles(GasType.OXYGEN) < air.getMoles(GasType.HYDROGEN) || MINIMUM_H2_OXYBURN_ENERGY > air.getThermalEnergy()) {
            burnedFuel = air.getMoles(GasType.OXYGEN) / HYDROGEN_BURN_OXY_FACTOR;
            air.adjustMoles(GasType.HYDROGEN, -burnedFuel);
        } else {
            burnedFuel = air.getMoles(GasType.HYDROGEN) * HYDROGEN_BURN_H2_FACTOR;
            air.adjustMoles(GasType.HYDROGEN, -(air.getMoles(GasType.HYDROGEN) / HYDROGEN_BURN_H2_FACTOR));
            air.adjustMoles(GasType.OXYGEN, -air.getMoles(GasType.HYDROGEN));
        }

        if (burnedFuel > 0) {
            energyReleased = FIRE_HYDROGEN_ENERGY_RELEASED * burnedFuel;
            air.adjustMoles(GasType.WATER_VAPOUR, burnedFuel / HYDROGEN_BURN_OXY_FACTOR);
        }

        if (energyReleased > 0) {
            double newHeatCapacity = air.getHeatCapacity();
            if(newHeatCapacity > MINIMUM_HEAT_CAPACITY)
                air.setTemperature((temperature * oldHeadCapacity + energyReleased) / newHeatCapacity);
        }
    }
}
