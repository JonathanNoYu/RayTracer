import java.util.Random;
import java.util.concurrent.CountDownLatch;
import object3D.Hittable_List;
import object3D.Ray;
import object3D.Vector3D;
import object3D.boundBox.bvh_node;

public class RayThread implements Runnable {

  private final bvh_node world;
  private Vector3D newColor;
  private PPMBuilder3D builder3D;
  private Camera cam;
  private CountDownLatch latch;
  private int row;
  private int col;

  public RayThread(PPMBuilder3D builder, Camera cam, bvh_node world, CountDownLatch cdl, int x, int y, Vector3D newColor) {
    this.builder3D = builder;
    this.cam = cam;
    this.latch = cdl;
    this.row = x;
    this.col =  y;
    this.newColor = newColor;
    this.world =  world;
  }

  @Override
  public void run() {
    Random rand = new Random();
    for (int sample = 0; sample < builder3D.getSamplePerPixel(); sample++) {
      double u = (col + rand.nextDouble()) / (builder3D.getWidth() - 1);
      double v = (row + rand.nextDouble()) / (builder3D.getHeight() -1);
      Ray r = cam.getRay(u, v);
      Vector3D newColor = builder3D.rayColor(r, world, builder3D.getMaxDepth());
      this.newColor.add(newColor);
    }
    latch.countDown();
  }
}
