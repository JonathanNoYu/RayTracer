package object3D.boundBox;

import object3D.Point3D;
import object3D.Ray;

public class AABB {
  private Point3D minimum;
  private Point3D maximum;
  private double currMin;
  private double currMax;
  private int boxHit = 0;

  public AABB() {
    this(new Point3D(0,0,0), new Point3D(0,0,0));
  }
  public AABB(Point3D a, Point3D b) {
    this.minimum = a;
    this.maximum = b;
  }

  public Point3D getMaximum() {
    return this.maximum;
  }

  public Point3D getMinimum() {
    return this.minimum;
  }

  public void setMaximum(Point3D maximum) {
    this.maximum = maximum;
  }

  public void setMinimum(Point3D minimum) {
    this.minimum = minimum;
  }

  public boolean hit(Ray r, double tMin, double tMax) {
    this.currMin = tMin;
    this.currMax = tMax;
    if (this.hitHelper(minimum.getX(), maximum.getX(), r.origin().getX(), r.direction().getX())
    || this.hitHelper(minimum.getY(), maximum.getY(), r.origin().getY(), r.direction().getY())
    || this.hitHelper(minimum.getZ(), maximum.getZ(), r.origin().getZ(), r.direction().getZ())) {
      return false;
    }
    this.boxHit += 1;
    return true;
  }

  private boolean hitHelper(double min, double max, double rayOrigin, double rayDir) {
    double t0 = Math.min(((min - rayOrigin) / rayDir),
                          ((max - rayOrigin) / rayDir));
    double t1 = Math.max(((min - rayOrigin) / rayDir),
                          ((max - rayOrigin) / rayDir));

    this.currMin = Math.max(t0, this.currMin);
    this.currMax = Math.min(t1, this.currMax);
    return this.currMax  <= this.currMin;
  }

  public void setAABB(AABB aabb) {
    this.setMaximum(aabb.getMaximum());
    this.setMinimum(aabb.getMinimum());
  }

  public static AABB surrounding_box(AABB box0, AABB box1) {
    Point3D small = new Point3D(Math.min(box0.getMinimum().getX(), box1.getMinimum().getX()),
        Math.min(box0.getMinimum().getY(), box1.getMinimum().getY()),
        Math.min(box0.getMinimum().getZ(), box1.getMinimum().getZ()));
    Point3D big = new Point3D(Math.max(box0.getMaximum().getX(), box1.getMaximum().getX()),
        Math.max(box0.getMaximum().getY(), box1.getMaximum().getY()),
        Math.max(box0.getMaximum().getZ(), box1.getMaximum().getZ()));
    return new AABB(small, big);
  }

  public int getBoxHit() {
    return this.boxHit;
  }
}
