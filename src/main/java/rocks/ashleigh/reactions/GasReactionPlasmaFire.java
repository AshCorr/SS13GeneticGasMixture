package rocks.ashleigh.reactions;

import rocks.ashleigh.*;

import static rocks.ashleigh.Constants.*;

public class GasReactionPlasmaFire extends GasReaction {
    @Override
    public boolean canReact(GasMixture mixture) {
        return mixture.getTemperature() >= FIRE_MINIMUM_TEMPERATURE_TO_EXIST
                && mixture.getMoles(GasType.OXYGEN) >= MINIMUM_MOLE_COUNT
                && mixture.getMoles(GasType.PLASMA) >= MINIMUM_MOLE_COUNT;
    }

    @Override
    public void handle(GasMixture air) {
        double energyReleased = 0;
        double oldHeatCapacity = air.getHeatCapacity();
        double temperature = air.getTemperature();

        double plasmaBurnRate = 0;
        double oxygenBurnRate = 0;
        double temperatureScale = 0;

        double plasmaMoles = air.getMoles(GasType.PLASMA);
        double oxygenMoles = air.getMoles(GasType.OXYGEN);

        boolean superSaturation = false;

        if (temperature > PLASMA_UPPER_TEMPERATURE)
            temperatureScale = 1;
        else
            temperatureScale = (temperature-PLASMA_MINIMUM_BURN_TEMPERATURE)/(PLASMA_UPPER_TEMPERATURE-PLASMA_MINIMUM_BURN_TEMPERATURE);

        if (temperatureScale > 0) {
            oxygenBurnRate = OXYGEN_BURN_RATE_BASE - temperatureScale;

            superSaturation = air.getMoles(GasType.OXYGEN) / air.getMoles(GasType.PLASMA) > SUPER_SATURATION_THRESHOLD;

            if (air.getMoles(GasType.OXYGEN) > air.getMoles(GasType.PLASMA) * PLASMA_OXYGEN_FULLBURN)
                plasmaBurnRate = (air.getMoles(GasType.PLASMA)*temperatureScale)/PLASMA_BURN_RATE_DELTA;
            else
                plasmaBurnRate = (temperatureScale * (air.getMoles(GasType.OXYGEN)/PLASMA_OXYGEN_FULLBURN))/PLASMA_BURN_RATE_DELTA;

            if (plasmaBurnRate> MINIMUM_HEAT_CAPACITY) {
                plasmaBurnRate = Math.min(Math.min(plasmaBurnRate, plasmaMoles), oxygenMoles);
                air.setMoles(GasType.PLASMA, Helpers.quantize(plasmaMoles - plasmaBurnRate));
                air.setMoles(GasType.OXYGEN, Helpers.quantize(plasmaMoles - (plasmaBurnRate * oxygenBurnRate)));

                if (superSaturation)
                    air.adjustMoles(GasType.TRITIUM, plasmaBurnRate);
                else
                    air.adjustMoles(GasType.CARBON_DIOXIDE, plasmaBurnRate);

                energyReleased = FIRE_PLASMA_ENERGY_RELEASED * plasmaBurnRate;
            }
        }

        if (energyReleased > 0) {
            double newHeatCapacity = air.getHeatCapacity();
            if (newHeatCapacity > MINIMUM_HEAT_CAPACITY)
                air.setTemperature((temperature * oldHeatCapacity + energyReleased) / newHeatCapacity);
        }


    }
}
