import object3D.Point3D;
import object3D.Ray;
import object3D.Vector3D;

import static object3D.Point3D.vecToPoint;
import static object3D.Vector3D.cross;
import static object3D.Vector3D.random_in_unit_disk;

/**
 * Camera class that holds the position and orientation of the camera in the world
 */
public class Camera {
    private Point3D origin;
    private Vector3D horizontal;
    private Vector3D vertical;
    private Point3D lowerLeftCorner;
    private Vector3D u,v,w;
    private double lensRadius;

    public Camera(Point3D lookFrom, Point3D lookAt, Vector3D vup, double vfov, AspectRatio ar, double aperture, double focus_dist) {
        double theta = degreesToRadians(vfov);
        double h = Math.tan(theta / 2);
        double camHeight = 2.0 * h;
        double camWidth = AspectRatio.aspectRatio(ar) * camHeight;

        // Some issue here.
        this.w = (lookFrom.subtract(lookAt)).normalize();
        this.u = (cross(vup,w)).normalize();
        this.v = cross(w,u);

        this.origin = lookFrom;
        this.horizontal = u.multiply(camWidth * focus_dist);
        this.vertical = v.multiply(camWidth * focus_dist);
        Vector3D lowLeft = origin.subtract(horizontal.divide(2))
            .subtract(vertical.divide(2))
            .subtract(w.multiply(focus_dist));
        this.lowerLeftCorner = vecToPoint(lowLeft);

        this.lensRadius = aperture / 2;
    }

    /**
     * Gets a ray from the camera position and orientation but varies by s (horizontal) and t (vertical).
     *
     * @param s - double within (-1,1)
     * @param t - double within (-1,1)
     * @return Ray that is offset and fuzzed
     */
    public Ray getRay(double s, double t) {
        Vector3D rd = random_in_unit_disk().multiply(this.lensRadius);
        Vector3D offset = u.multiply(rd.getX()).add(v.multiply(rd.getY()));

        return new Ray(vecToPoint(origin.add(offset)),
                lowerLeftCorner.add(horizontal.multiply(s))
                        .add(vertical.multiply(t))
                        .subtract(origin)
                        .subtract(offset));
    }

    public static double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180.0;
    }
}
