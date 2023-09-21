package util;

import java.awt.Color;
import object3D.Vector3D;

public class ColorUtil {

  public static int limitValue(double val) {
    return (int) Math.max(Math.min(val, 255),0);
  }

  public static Color vecToColor(Vector3D vec) {
    int x = limitValue(vec.getX() * 255);
    int y = limitValue(vec.getY() * 255);
    int z = limitValue(vec.getZ() * 255);
    return new Color(x,y,z);
  }
}
