import hw1.PPMEditorImpl;
import texture.MyNoise1D;

import java.awt.*;
import java.nio.file.Path;
import java.util.Random;

public class NoiseDriver {
    public static void main(String[] args) {
        int height = 400;
        int width = 400;
        int seed = 100;
        Color[][] noiseArr = new Color[height][width];
        MyNoise1D noise = new MyNoise1D(new Random(seed));
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                noiseArr[row][col] = noise.newColor(row, col);
            }
        }

        // Editor save settings
        PPMEditorImpl editor = new PPMEditorImpl();
        editor.loadPPM(noiseArr, "noise");
        editor.saveFileP3(Path.of("Assignment3_Camera/part1/res"),"noise","noise.ppm");
    }
}
