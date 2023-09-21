package object3D;

/**
 * 3D ray
 */
public class Ray {
    private Point3D origin;
    private Vector3D direction;

    public Ray(Point3D origin, Vector3D direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Point3D origin() {
        return this.origin;
    }

    public Vector3D direction() {
        return this.direction;
    }

    /**
     * Shoots the ray in it's direction by t time.
     *
     * @param t - time the ray travels
     * @return - new position the ray is in after t time
     */
    public Point3D at(double t) {
        return Point3D.vecToPoint(this.origin.add(this.direction.multiply(t)));
    }

    public void setRay(Ray r) {
        this.origin = r.origin();
        this.direction = r.direction();
    }
}
