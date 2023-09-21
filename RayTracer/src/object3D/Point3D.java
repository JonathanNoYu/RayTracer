package object3D;

/**
 * A position in 3D is going to be treated as the same as a Vector.
 * Similar to the code written by Shirley's c++ code.
 */
public class Point3D extends Vector3D {

    public Point3D(double x, double y, double z) {
        super(x, y, z);
    }

    public static Point3D vecToPoint(Vector3D v) {
        return new Point3D(v.getX(), v.getY(), v.getZ());
    }
}
