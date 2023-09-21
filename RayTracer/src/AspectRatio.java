/**
 * Enumerations of supported aspect ratios
 */
public enum AspectRatio {
    sixteen_to_nine, four_to_three;

    public static double aspectRatio(AspectRatio ar) {
        switch (ar) {
            case sixteen_to_nine -> {
                return (double) 16 / 9;
            }
            case four_to_three -> {
                return (double) 4 / 3;
            }
            default -> throw new IllegalArgumentException("Invalid Aspect Ratio");
        }
    }

    public static AspectRatio aspectRatio(double d) {
        if (d == (double) 16 / 9) {
            return sixteen_to_nine;
        } else if (d == (double) 4 / 3) {
            return four_to_three;
        }
        throw new IllegalArgumentException("Invalid Aspect Ratio");
    }
}
