package hw1;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Scanner;

public class PPMUtil {

  /**
   * Reads a file as a PPM and returns the 2D array of colors representing a PPM.
   *
   * @param filePath - file path to the file we want to read.
   * @return 2D array of colors that represents a PPM.
   * @throws IllegalArgumentException if the file is not a PPM file or errors occur during reading.
   */
  public static Color[][] readP3PPM(Path filePath) throws IllegalArgumentException {
    Scanner sc;
    try {
      sc = new Scanner(filePath.toFile());
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File " + filePath + " not found!");
    }
    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s + System.lineSeparator());
      }
    }

    //now set up the scanner to read from the string we just built
    sc = new Scanner(builder.toString());

    String token = sc.next();
    if (!token.equals("P3")) {
      throw new IllegalArgumentException("Invalid PPM file: plain RAW file should begin with P3");
    }

    int width = sc.nextInt(); // # Columns
    int height = sc.nextInt(); // # Rows
    int maxValue = sc.nextInt(); // Not used since the 2D array can be used to find the maximum val.

    Color[][] arr = new Color[height][width];
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        int r = sc.nextInt(); // gets the red component value
        int g = sc.nextInt(); // gets the green component value
        int b = sc.nextInt(); // gets the blue component value
        arr[row][col] = new Color(r, g, b);
      }
    }
    return arr;
  }

  /**
   * Dims a whole PPM by a value.
   *
   * @param img - PPM to be dimmed.
   * @param dimVal - Value to dim by.
   * @return modified PPM.
   */
  public static Color[][] dimBy(Color[][] img, int dimVal) {
    return changePixelBy(img, dimVal, false);
  }

  /**
   * Brightens a whole PPM by a value.
   *
   * @param img - PPM to be brightened.
   * @param briVal - Value to light up by.
   * @return modified PPM.
   */
  public static Color[][] brightBy(Color[][] img, int briVal) {
    return changePixelBy(img, briVal, true);
  }

  /**
   * Changes (add or subtract) each pixel of a PPM by a certain value.
   *
   * @param img - PPM to change. (2D array of Colors)
   * @param val - value to add or subtract by.
   * @param add - Boolean to determine if add or subtract. (true for add, false for subtract)
   * @return modified PPM.
   */
  public static Color[][] changePixelBy(Color[][] img, int val, boolean add) {
    checkInputs(img, val);
    int height = img.length;
    int width = img[0].length;
    Color[][] newImg = new Color[height][width];
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        Color currPixel = img[row][col];
        if (add) {
          newImg[row][col] = bright(currPixel, val);
        } else {
          newImg[row][col] = dim(currPixel, val);
        }
      }
    }
    return newImg;
  }

  /**
   * Changes a PPM by a factor (multiples the factor with each pixel's component's value).
   *
   * @param img - PPM to modify.
   * @param factor - the factor/double to multiply by.
   * @return modified PPM.
   */
  public static Color[][] factorChange(Color[][] img, double factor) {
    int height = img.length;
    int width = img[0].length;
    Color[][] newImg = new Color[height][width];
    for(int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        Color c = img[row][col];
        // New color is created by making the doubles into ints and multiplying by the factor
        newImg[row][col] = new Color(capVal(c.getRed() * factor),
            capVal(c.getGreen() * factor),
            capVal(c.getBlue() * factor));
      }
    }
    return newImg;
  }

  /**
   * Dims a Color by a value.
   *
   * @param c - Color to be dimmed
   * @param dimVal - value to dim by.
   * @return new Color that is dimmed.
   */
  private static Color dim(Color c, int dimVal) {
    if (dimVal < 0 || dimVal > 255) {
      throw new IllegalArgumentException("Invalid dimming value must be (0, 255) inclusive");
    }
    return new Color(
        Math.max(c.getRed() - dimVal, 0),
        Math.max(c.getGreen() - dimVal, 0),
        Math.max(c.getBlue() - dimVal, 0));
  }

  /**
   * Lights up a Color by a value.
   *
   * @param c - Color to be dimmed
   * @param lightVal - value to lighten up by.
   * @return new Color that is brighter.
   */
  private static Color bright(Color c, int lightVal) {
    if (lightVal < 0 || lightVal > 255) {
      throw new IllegalArgumentException("Invalid dimming value must be (0, 255) inclusive");
    }
    return new Color(
        Math.min(c.getRed() + lightVal, 255),
        Math.min(c.getGreen() + lightVal, 255),
        Math.min(c.getBlue() + lightVal, 255));
  }

  /**
   * Caps a value (double) to within 0 to 255 and returns it as an int.
   *
   * @param val - double to be capped.
   * @return int that is within 0 to 255.
   */
  private static int capVal(double val) {
    return (int) Math.max(Math.min(val,255),0);
  }

  /**
   * Finds the maximum value of every pixel's components in a PPM.
   *
   * @param img - PPM to find the max value of.
   * @return int value that is the maximum value of the PPM.
   */
  public static int maxValueOf(Color[][] img) {
    int height = img.length;
    int width = img[0].length;
    int max = 0;
    Color currColor;
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        currColor = img[row][col];
        // Finds the max of the three color values
        int maxValue =
            Math.max(
                Math.max(currColor.getRed(), currColor.getGreen()),
                currColor.getBlue());
        // Sets new max if it is greater than previous max
        if (maxValue > max) {
          max = maxValue;
        }
      }
    }
    return max;
  }

  /**
   * Returns the file name from a path.
   *
   * @param path - Path to find file name of.
   * @return file name as a string.
   */
  public static String fileNameFromPath(Path path) {
    String[] pathNames = path.getFileName().toString().split("//");
    String[] fileName = pathNames[0].split("\\.");
    return fileName[0];
  }

  /**
   * Checks if the PPM given and val is valid.
   *
   * @param img - PPM to check. (2D array exists)
   * @param val - int to check. (within 0 to 255)
   */
  private static void checkInputs(Color[][] img, int val) {
    if (val < 0 || val > 255) {
      throw new IllegalArgumentException("Invalid dimming value must be (0, 255) inclusive");
    }
    if (img == null) {
      throw new IllegalArgumentException("The Image cannot be null");
    }
  }
}
