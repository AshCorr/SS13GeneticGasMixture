package rocks.ashleigh;

import rocks.ashleigh.reactions.GasReactionHydrogenFire;
import rocks.ashleigh.reactions.GasReactionPlasmaFire;
import rocks.ashleigh.reactions.GasReactionTritiumFire;

import java.util.Arrays;
import java.util.List;

import static rocks.ashleigh.Constants.ONE_ATMOSPHERE;

public class GasBombSimulator {

    private static final double TANK_FRAGMENT_PRESSURE = (40.0*ONE_ATMOSPHERE);
    private static final double TANK_FRAGMENT_SCALE = (6.0*ONE_ATMOSPHERE);

    private List<GasReaction> reactions = Arrays.asList(
            new GasReactionTritiumFire(),
            new GasReactionHydrogenFire(),
            new GasReactionPlasmaFire()
    );

    public GasMixture simulate(GasMixture gasA, GasMixture gasB) {
        GasMixture bombMix = new GasMixture(0, gasA.getVolume() + gasB.getVolume())
                .merge(gasA)
                .merge(gasB);

        return simulate(bombMix);
    }

    public GasMixture simulate(GasMixture gasMixture) {
        for (int i = 0; i < 10; i++) {
            gasMixture.react(reactions);
            if (gasMixture.getPressure() >= TANK_FRAGMENT_PRESSURE) {
                gasMixture.react(reactions);
                gasMixture.react(reactions);
                gasMixture.react(reactions);
                break;
            }
        }

        return gasMixture;
    }

    public double getLightRange(GasMixture gasMixture) {
        return (gasMixture.getPressure() - TANK_FRAGMENT_PRESSURE) / TANK_FRAGMENT_SCALE;
    }

    public double getHeavyRange(GasMixture gasMixture) {
        return getLightRange(gasMixture) * 0.50;
    }

    public double getDevastationRange(GasMixture gasMixture) {
        return getLightRange(gasMixture) * 0.25;
    }
}
