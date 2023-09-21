package texture;

import java.awt.*;
import java.util.Random;

import static texture.ImprovedNoise.fade;
import static util.ColorUtil.limitValue;

public class MyNoise1D {

    private final Random rand;
    private final double factor;

    public MyNoise1D(Random rand) {
        this.rand = rand;
        this.factor = rand.nextDouble(0.4,0.78);
    }

    public double lerp(double x) {
        return (x / 3) * Math.sqrt(x) + 2 * x;
    }

    public double noise(int x, int y) {
        double X = lerp(x);
        double Y = lerp(y);

        x -= x * rand.nextDouble(0, 0.5);
        y -= y * rand.nextDouble(0, 0.5);

        if (X > 255) {
            X = rand.nextDouble(0, 219);
            return noise((int) X,y);
        }

        if (Y > 255) {
            Y = rand.nextDouble(0, 170);
            return noise(x, (int) Y);
        }

        x = (int) smoothStep(x);
        y = (int) smoothStep(y);

        return smoothStep(((x * Y) - (y * X)) * this.factor);
    }

    public Color newColor(int x, int y) {
        int newValue = limitValue(noise(x,y));
        return new Color(newValue,newValue,newValue);
    }

    public double smoothStep(double v) {
        return v * v * (3 - 2 * v);
    }
}
