import java.awt.desktop.SystemEventListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Timer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import object3D.*;
import object3D.boundBox.bvh_node;
import util.ColorUtil;

import java.awt.Color;
import java.util.Random;

import static util.ColorUtil.limitValue;

/**
 * Builder/Render for my ray tracing PPMs
 */
public class PPMBuilder3D {
    public static double INFINITY = Double.POSITIVE_INFINITY;
    public static Vector3D zeroVec = new Vector3D(0,0,0);
    private Camera cam;
    private final int samplePerPixel;
    private final int maxDepth;
    private final double aspectRatio;
    private final int width;
    private final int height;
    private final double gamma;
    private Hittable_List world;
    private int numberOfRaysFired;
    private int numberOfIntersections;
    // Constructor
    public PPMBuilder3D(Camera cam, AspectRatio ar, int width, int height, int samplePerPixel, int maxDepth, double gamma) {
        this.cam = cam;
        this.aspectRatio = AspectRatio.aspectRatio(ar);
        this.width = width;
        this.height = height;
        this.world = new Hittable_List();
        this.samplePerPixel = samplePerPixel;
        this.maxDepth = maxDepth;
        this.gamma = gamma;
    }

    /**
     * Ray tracing aspect where a ray is shot into the world and reflected/refracted realistic.
     *
     * @param r - Ray that is being shot in a direction.
     * @param world - Hittable object(s) we want to see if the ray hits
     * @param depth - How many more children of this ray
     * @return Vector that represents a Color (x = r, y = g, z = b)
     */
    public Vector3D rayColor(Ray r, Hittable world, int depth) {
        numberOfRaysFired += 1;
        HitRecord rec = new HitRecord();
        // Does not hit anything
        if(depth <= 0) {
            return zeroVec;
        }
        // Ray tracing here
        if(world.hit(r, 0.001, INFINITY, rec)) {
            numberOfIntersections += 1;
            Ray scattered = new Ray(new Point3D(0,0,0), zeroVec);
            Vector3D attenuation = zeroVec;
            if (rec.getMat().scatter(r, rec, attenuation, scattered)) {
                return attenuation.multiply(rayColor(scattered, world, depth - 1));
            }
            return zeroVec;
        }
        // Background color
        Vector3D unitDir = r.direction().normalize();
        double t = 0.5 * (unitDir.getY() + 1.0);
        Vector3D c1 = new Vector3D(1.0, 1.0, 1.0).multiply(1.0-t);
        Vector3D c2 = new Vector3D(0.5, 0.7, 1.0).multiply(t);
        return c1.add(c2);
    }

    /**
     * Adds objects to this image.
     *
     * @param object - Object that is able to be hit.
     */
    public void addObject(Hittable object) {
        this.world.add(object);
    }

    /**
     * Builds/Renders the image.
     *
     * @return 2D array of pixels meant to be saved as a PPM.
     */
    public Color[][] build() {
        long startTime = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(height * width);
        bvh_node bvh = new bvh_node(world, 0, 0);
        Color[][] img = new Color[height][width];
        Random rand = new Random();
        int realRow = 0;
        for (int row = height-1; row >= 0; --row) {
            for (int col = 0; col < width; ++col) {
                Vector3D pixel_color = new Vector3D(0, 0, 0);
                new Thread(new RayThread(this, cam, bvh, latch, row, col, pixel_color)).start();
                img[realRow][col] = scaleColor(pixel_color);
            }
            realRow++;
        }
        try{
            latch.await();
        }catch(InterruptedException e){System.out.println ("Exception");}
        long endTime = System.currentTimeMillis();
        long timeTakenToRender = endTime - startTime;
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeTakenToRender),
            TimeUnit.MILLISECONDS.toMinutes(timeTakenToRender) % TimeUnit.HOURS.toMinutes(1),
            TimeUnit.MILLISECONDS.toSeconds(timeTakenToRender) % TimeUnit.MINUTES.toSeconds(1));
        DecimalFormat formatter = new DecimalFormat("#,###");
        System.out.println(
                "Render Time:                     " + hms + " Hr:Min:Sec" + System.lineSeparator() +
                "Ray Fired:                       " + formatter.format(this.numberOfRaysFired) + System.lineSeparator()
        +       "Bounded Box Intersections:       " + formatter.format(bvh.getBoxHit()) + System.lineSeparator()
        +       "Successful Object Intersections: " + formatter.format(this.numberOfIntersections) + System.lineSeparator());
        return img;
    }

    /**
     * Sets a new camera to see a different perspective of the world.
     * @param cam - new Camera point of view
     */
    public void setNewCamera(Camera cam) {
        this.cam = cam;
    }

    /**
     * Scales the vectors and limits the vectors to 0-255 (Gamma Requirement)
     *
     * @param pixel_color - the final color of the pixel
     * @return - correct Color given a vector
     */

    public Color scaleColor(Vector3D pixel_color) {
        double r = pixel_color.getX();
        double g = pixel_color.getY();
        double b = pixel_color.getZ();

        double scale = 1.0 / samplePerPixel;
        // Gamma Correction by square the x,y,z/r,g,b values.
        r = Math.pow((r * scale), (1/gamma)) * 255.999;
        g = Math.pow((g * scale), (1/gamma)) * 255.999;
        b = Math.pow((b * scale), (1/gamma)) * 255.999;

        Color c = new Color(limitValue(r), limitValue(g), limitValue(b));
        return c;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public int getSamplePerPixel() {
        return samplePerPixel;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
