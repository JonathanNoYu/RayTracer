package hw1;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * First version of my PPMEditor for Assignment 1
 */
public class PPMEditorImpl implements PMMEditor {
  // In case we need to know the latest loaded PPM in the future
  private Color[][] currImg;
  // Name of the latest loaded PPM, same as above.
  private String currImgName;
  // Map of all the images we have loaded
  private final Map<String, Color[][]> Images;

  public PPMEditorImpl() {
    this.Images = new HashMap<>();
  }

  @Override
  public void loadFile(Path filePath) throws IllegalArgumentException {
    this.currImg = PPMUtil.readP3PPM(filePath);
    this.currImgName = PPMUtil.fileNameFromPath(filePath);
    this.Images.put(this.currImgName, this.currImg);
  }

  @Override
  public void loadPPM(Color[][] image, String imgName) throws IllegalArgumentException {
    this.currImg = image;
    this.currImgName = imgName;
    this.Images.put(this.currImgName, this.currImg);
  }

  @Override
  public void saveFileP3(Path dirPath, String imageName, String fileName) throws IllegalArgumentException {
    this.saveFile(dirPath, imageName, fileName, "P3");
  }

  @Override
  public void saveFileP6(Path dirPath, String imageName, String fileName) throws IllegalArgumentException {
    this.saveFile(dirPath, imageName, fileName, "P6");
  }

  @Override
  public void brightenPPMBy(String imageName, int val) throws IllegalArgumentException {
    Color[][] img = this.Images.get(imageName);
    Color[][] newImg = PPMUtil.brightBy(img, val);
    this.Images.put(imageName + "_brighten_" + val, newImg);
  }

  @Override
  public void darkenPPMBy(String imageName, int val) throws IllegalArgumentException {
    Color[][] img = this.Images.get(imageName);
    Color[][] newImg = PPMUtil.dimBy(img, val);
    this.Images.put(imageName + "_darken_" + val, newImg);
  }

  @Override
  public void changePixelsByFactor(String imageName, double factor)
      throws IllegalArgumentException {
    Color[][] img = this.Images.get(imageName);
    Color[][] newImg = PPMUtil.factorChange(img, factor);
    this.Images.put(imageName + "_" + factor, newImg);
  }

  /**
   * Generalize saving file that can be extended to support more formats
   *
   * @param dirPath - director path where the ppm should be saved.
   * @param imageName - name of the PPM to save.
   * @param fileName - name of the file (including extensions i.e. ".ppm").
   * @param Token - Format token i.e. P3, P6.
   * @throws IllegalArgumentException - error to be thrown if something goes wrong.
   */
  private void saveFile(Path dirPath, String imageName, String fileName, String Token) throws IllegalArgumentException {
    long startTime = System.currentTimeMillis();
    Color[][] imgFromStorage = this.Images.get(imageName);
    if (imgFromStorage == null) {
      throw new IllegalArgumentException("Image not found, could not find: " + fileName);
    }
    int width = imgFromStorage[0].length;
    int height = imgFromStorage.length;
    String fullPath = dirPath.toString() + "//" + fileName;
    try {
      FileOutputStream output = new FileOutputStream(fullPath);
      // Formatting Header
      String header = Token + "\n"                            // P3 or P6
              + width + " " + height + "\n"                    // # #
              + "255" + "\n" ;

      //Writing Header in ASCII
      output.write(header.getBytes(StandardCharsets.UTF_8));

      // Writes rest of the image in their formats
      switch (Token) {
        case "P3" -> {
          StringBuilder imageAsString = this.P3SFormat(imgFromStorage);
          output.write(imageAsString.toString().getBytes(StandardCharsets.UTF_8)); // ASCII format
        }
        case "P6" -> {
          List<Byte> img = this.P6Format(imgFromStorage);
          for (byte b : img) {
            output.write(b);
          }
        }
        default ->
                throw new IllegalArgumentException("Token was not P3 or P6, it was: " + Token);
      }
      output.flush();
      output.close();
      long endtime = System.currentTimeMillis();
      long timeTaken = endtime - startTime;
      String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeTaken),
          TimeUnit.MILLISECONDS.toMinutes(timeTaken) % TimeUnit.HOURS.toMinutes(1),
          TimeUnit.MILLISECONDS.toSeconds(timeTaken) % TimeUnit.MINUTES.toSeconds(1));
      System.out.println("Time Taken to Save Image:        " + hms + " Hr:Min:Sec" + System.lineSeparator()
      + "Saved Image " + imageName + System.lineSeparator());
    } catch (IOException e) {
      throw new IllegalArgumentException("File failed to save, Path or Image does not exist \n" + e);
    }
  }

  /**
   * Formats the header for the P3 PPM files. (ASCII).
   *
   * @param img - PPM data/2D array of the colors in the PPM
   * @return a String Builder to reduce time taken in building the long string to be written
   */
  private StringBuilder P3SFormat(Color[][] img) {
    StringBuilder imageAsString = new StringBuilder();
    for (Color[] colors : img) {
      for (Color c : colors) { // while there is stuff in the file
        imageAsString.append(c.getRed()).append(" ")
                .append(c.getGreen()).append(" ")
                .append(c.getBlue()).append(" ");
      }
      imageAsString.append(System.lineSeparator());
    }
    return imageAsString;
  }

  /**
   * Formats the header for the P6 PPM files. (Binary)
   *
   * @param img - PPM data/2D array of the colors in the PPM
   * @return List of bytes to be written for a P6 format PPM.
   */
  private List<Byte> P6Format(Color[][] img){
    List<Byte> list = new ArrayList<>();
    for (Color[] colors : img) {
      for (Color c : colors) {
        list.add((byte) c.getRed());
        list.add((byte) c.getGreen());
        list.add((byte) c.getBlue());
      }
    }
    return list;
  }
}