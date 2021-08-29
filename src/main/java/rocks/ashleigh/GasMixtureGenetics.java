package rocks.ashleigh;

public class GasMixtureGenetics {
    public static void main(String[] args) {
        GasBombSimulator gasBombSimulator = new GasBombSimulator();

        GasMixture gasA = new GasMixture(28);
        gasA.setMoles(GasType.OXYGEN, 466);
        gasA.setMoles(GasType.HYDROGEN, 185.3);

        GasMixture gasB = new GasMixture(70_000);
        gasB.setMoles(GasType.PLASMA, 0.3);

        System.out.println("Left Tank:" + gasA);
        System.out.println("Right Tank:" + gasB);

        GasMixture bombMix = gasBombSimulator.simulate(gasA, gasB);
        System.out.println("Bomb mix: " + bombMix);
        System.out.println("Devastation Range: " + gasBombSimulator.getDevastationRange(bombMix));
        System.out.println("Heavy Range: " + gasBombSimulator.getHeavyRange(bombMix));
        System.out.println("Light Range: " + gasBombSimulator.getLightRange(bombMix));
    }
}
