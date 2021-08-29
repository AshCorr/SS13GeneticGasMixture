package rocks.ashleigh;

public class Constants {
    public static final double ONE_ATMOSPHERE = 101.325;
    public static final double IDEAL_GAS_CONSTANT = 8.31;
    public static final double T0C = 273.15;

    public static final double MAXIMUM_TEMPERATURE_DELTA_TO_CONSIDER = 0;
    public static final double FIRE_MINIMUM_TEMPERATURE_TO_EXIST = 100 + T0C;
    public static final double MINIMUM_MOLE_COUNT = 0.01;
    public static final double MINIMUM_HEAT_CAPACITY = 0.0003;
    public static final double SUPER_SATURATION_THRESHOLD = 96;

    public static final double PLASMA_UPPER_TEMPERATURE = 1370 + T0C;
    public static final double PLASMA_MINIMUM_BURN_TEMPERATURE = 100+T0C;
    public static final double PLASMA_OXYGEN_FULLBURN = 10;
    public static final double PLASMA_BURN_RATE_DELTA = 9;
    public static final double FIRE_PLASMA_ENERGY_RELEASED = 3_000_000;

    public static final double OXYGEN_BURN_RATE_BASE = 1.4;

    public static final double MINIMUM_H2_OXYBURN_ENERGY = 2_000_000;
    public static final double HYDROGEN_BURN_OXY_FACTOR = 100;
    public static final double HYDROGEN_BURN_H2_FACTOR = 10;
    public static final double FIRE_HYDROGEN_ENERGY_RELEASED = 560_000;

    public static final double MINIMUM_TRIT_OXYBURN_ENERGY = 2_000_000;
    public static final double TRITIUM_BURN_OXY_FACTOR = 100;
    public static final double TRITIUM_BURN_TRIT_FACTOR = 10;

}
