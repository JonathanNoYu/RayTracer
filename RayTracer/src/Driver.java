import hw1.PPMEditorImpl;
import object3D.ObjParser;
import object3D.Point3D;
import object3D.Sphere;
import object3D.Vector3D;
import object3D.mats.Dielectric;
import object3D.mats.Lambertian;
import object3D.mats.Material;
import object3D.mats.Metal;

import java.nio.file.Path;

public class Driver {
  public static void main(String[] args) {
    // Initializing PPM I/O
    PPMEditorImpl editor = new PPMEditorImpl();
    Path resPathInIDE = Path.of("Assignment5_BoundingVolume/part1/res");
    Path resPathFromDriver = Path.of("../res");

    //Image Settings
    AspectRatio ar = AspectRatio.four_to_three;
    int imageWidth = 600;
    int imageHeight = (int) (imageWidth / AspectRatio.aspectRatio(ar));

    // Camera1 Settings
    Point3D lookFrom = new Point3D(2,2,5);
    Point3D lookAt = new Point3D(0,0,0);
    Vector3D vup = new Vector3D(0,1,0);
    double vfov = 40;
    double dist_to_focus1 = (lookFrom.subtract(lookAt)).length();
    double apertureLow = 0.1;
    Camera cam1 = new Camera(lookFrom, lookAt, vup, vfov, ar, apertureLow, dist_to_focus1);

    // Render/Builder (Object Builder)
    int samplePerPixels = 150;
    int maxDepth = 50;
    double gamma = 1.5;
    PPMBuilder3D builder3D = new PPMBuilder3D(cam1, ar, imageWidth, imageHeight, samplePerPixels, maxDepth, gamma);

    // Parse Object (OBJ requirement)
    ObjParser objParser = new ObjParser("Assignment5_BoundingVolume/part1/res/cube.obj");
    builder3D.addObject(objParser.buildObject());

    // Using Cam1 as perspective
    editor.loadPPM(builder3D.build(), "output");

    // Saving images
    editor.saveFileP3(resPathInIDE,"output","output.ppm");

    Material groundMat = new Lambertian(new Vector3D(divBy255(96), divBy255(96), divBy255(96)));
    Material centerMat = new Dielectric(1.5);
    Material topLeftMat = new Lambertian(new Vector3D(0.1,0.2,0.5));
    Material topRightMat = new Metal(new Vector3D(divBy255(183),divBy255(75),divBy255(75)), 0.15);
    Material bottomLeftMat = new Metal(new Vector3D(divBy255(153),divBy255(153),0), 0.3);
    Material bottomRightMat = new Lambertian(new Vector3D(divBy255(12),divBy255(219),divBy255(219)));
    builder3D = new PPMBuilder3D(cam1, ar, imageWidth, imageHeight, samplePerPixels, maxDepth, gamma);
    builder3D.addObject(new Sphere(new Point3D(0,-1001,-1),1000, groundMat));
    builder3D.addObject(new Sphere(new Point3D(-2,0,-1), 1, bottomLeftMat));
    builder3D.addObject(new Sphere(new Point3D(-2,4,-1), 1, topLeftMat));
    builder3D.addObject(new Sphere(new Point3D(0,2,-1), 2, centerMat));
    builder3D.addObject(new Sphere(new Point3D(2,0,-1), 1, bottomRightMat));
    builder3D.addObject(new Sphere(new Point3D(2,4,-1), 1, topRightMat));

    // Using Cam1 as perspective
    editor.loadPPM(builder3D.build(), "output2");

    // Saving images
    editor.saveFileP3(resPathInIDE,"output2","output2.ppm");
  }

  public static double divBy255(double d) {
    return d / 255;
  }
}


