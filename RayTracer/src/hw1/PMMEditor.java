package hw1;

import java.awt.*;
import java.nio.file.Path;

/**
 * Basic Interface for any editor for PPMs
 */
public interface PMMEditor {

  /**
   * Loads the filepath given if the editor supports it.
   *
   * @param filePath - full path to a file (i.e. /dir/dir2/fileName.ppm, not necessarily absolute path).
   * @throws IllegalArgumentException if the path to the file is not supported.
   */
  void loadFile(Path filePath) throws IllegalArgumentException;

  /**
   * Loads a PPM in the form of a 2D array. Mainly used for when creating custom PPM not from an existing file.
   *
   * @param image - 2D array of colors that represents a PPM image.
   * @param imgName - name of the PPM.
   * @throws IllegalArgumentException if the path to the file is not supported.
   */
  void loadPPM(Color[][] image, String imgName) throws IllegalArgumentException;

  /**
   * Saves a PPM in P3 format. (ASCII)
   *
   * @param dirPath - full path to a directory.
   * @param fileName - name of the file to be saved.
   * @throws IllegalArgumentException if any errors happens during writing the file into the directory.
   */
  void saveFileP3(Path dirPath, String imageName, String fileName) throws IllegalArgumentException;

  /**
   * Saves a PPM in P6 format. (Binary)
   *
   * @param dirPath - full path to a directory
   * @param fileName - name of the file to be saved.
   * @throws IllegalArgumentException if any errors happens during writing the file into the directory.
   */
  void saveFileP6(Path dirPath, String imageName, String fileName) throws IllegalArgumentException;

  /**
   * Brightens an image by a certain value.
   * *Not currently used but kept since it may be useful later on*
   *
   * @param imageName - the name of the image we want to brighten.
   * @param val - the int we want to brighten the value by.
   * @throws IllegalArgumentException if the image does not exist or value is not 0-255.
   */
  void brightenPPMBy(String imageName, int val) throws IllegalArgumentException;

  /**
   * Darkens an image by a certain value.
   * *Not currently used but kept since it may be useful later on*
   *
   * @param imageName - the name of the image we want to darken.
   * @param val - the int we want to brighten the value by.
   * @throws IllegalArgumentException if the image does not exist or value is not 0-255.
   */
  void darkenPPMBy(String imageName, int val) throws IllegalArgumentException;

  /**
   * Brightens or darkens a image based on a factor
   *
   * @param imageName - the name of the image we want change its brightness.
   * @param factor - the double we want to multiple each RGB component to brighten the value by.
   * @throws IllegalArgumentException if the image does not exist.
   */
  void changePixelsByFactor(String imageName, double factor) throws IllegalArgumentException;
}