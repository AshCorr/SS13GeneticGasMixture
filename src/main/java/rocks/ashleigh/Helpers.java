package rocks.ashleigh;

public class Helpers {
    public static double quantize(double amount) {

        // TODO: Replace this garbage rounding
        return Math.round(amount * 1000000) / 1000000.0;
    }
}
