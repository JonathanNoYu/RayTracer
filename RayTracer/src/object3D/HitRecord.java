package object3D;

import object3D.mats.Material;

/**
 * Record of where an object is hit, including time, point, outward going normal, if it is the front of the object.
 */
public class HitRecord {
    private Point3D point;
    private Vector3D normal;
    private Material mat;
    private double t;
    private boolean front;

    public void setPoint(Point3D point) {
        this.point = point;
    }
    public void setMat(Material mat) {
        this.mat = mat;
    }

    public void setT(double t) {
        this.t = t;
    }

    // Sets the normal vector going outward
    public void setNormal(Vector3D normal) {
        this.normal = normal;
    }

    /**
     * Determines if the ray is hitting the front of the object
     *
     * @param ray - Ray hitting the object
     * @param outNormal - Outward normal vector
     */
    public void setFront(Ray ray, Vector3D outNormal) {
        this.front = Vector3D.dotProduct(ray.direction(),outNormal) < 0;
        if (this.front) {
            this.normal = outNormal;
        } else {
            this.normal = outNormal.invert();
        }
    }

    public double getT() {
        return t;
    }

    public Point3D getPoint() {
        return point;
    }

    public boolean getFront() {
        return this.front;
    }

    public Vector3D getNormal() {
        return normal;
    }

    public Material getMat() {
        return this.mat;
    }

    // Used to mutate data
    public void setRecord(HitRecord rec) {
        this.setT(rec.getT());
        this.setNormal(rec.getNormal());
        this.setPoint(rec.getPoint());
        this.setMat(rec.getMat());
    }
}
