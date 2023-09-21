package object3D;

import java.util.Random;

import static java.lang.Math.sqrt;

/**
 * 3D Vector with general vector operations
 */
public class Vector3D {
    private double xMag;
    private double yMag;
    private double zMag;

    public Vector3D(double x, double y, double z) {
        this.xMag = x;
        this.yMag = y;
        this.zMag = z;
    }

    public double getX() {
        return this.xMag;
    }

    public double getY() {
        return this.yMag;
    }

    public double getZ() {
        return this.zMag;
    }

    public void setXMag(double xMag) {
        this.xMag = xMag;
    }

    public void setYMag(double yMag) {
        this.yMag = yMag;
    }

    public void setZMag(double zMag) {
        this.zMag = zMag;
    }

    public Vector3D add(double d) {
        this.xMag += d;
        this.yMag += d;
        this.zMag += d;
        return this;
    }

    public Vector3D multiply(double d) {
        this.xMag *= d;
        this.yMag *= d;
        this.zMag *= d;
        return this;
    }

    public Vector3D multiply(Vector3D v) {
        this.xMag *= v.getX();
        this.yMag *= v.getY();
        this.zMag *= v.getZ();
        return this;
    }

    public Vector3D divide(double d) {
        this.xMag /= d;
        this.yMag /= d;
        this.zMag /= d;
        return this;
    }

    public Vector3D add(Vector3D v) {
        this.xMag += v.getX();
        this.yMag += v.getY();
        this.zMag += v.getZ();
        return this;
    }

    public Vector3D subtract(Vector3D v) {
        this.xMag -= v.getX();
        this.yMag -= v.getY();
        this.zMag -= v.getZ();
        return this;
    }

    public Vector3D invert() {
        this.xMag += -1;
        this.yMag += -1;
        this.zMag += -1;
        return this;
    }

    public double length() {
        return sqrt(this.lengthSquared());
    }

    public double lengthSquared() {
        return (this.xMag * this.xMag) +
                (this.yMag * this.yMag) +
                (this.zMag * this.zMag);
    }


    public double dotProduct(Vector3D v) {
        return (v.getX() * this.xMag) + (
                v.getY() * this.yMag) +
                (v.getZ() * this.zMag);
    }

    public static double dotProduct(Vector3D v1, Vector3D v2) {
        return (v1.getX() * v2.getX()) +
                (v1.getY() * v2.getY()) +
                (v1.getZ() * v2.getZ());
    }

    public Vector3D crossProduct(Vector3D v) {
        return cross(this, v);
    }

    public static Vector3D cross(Vector3D u, Vector3D v) {
        double newX = (u.getY() * v.getZ()) - (u.getZ() * v.getY());
        double newY = (u.getZ() * v.getX()) - (u.getX() * v.getZ());
        double newZ = (u.getX() * v.getY()) - (u.getY() * v.getX());
        return new Vector3D(newX, newY, newZ);
    }

    public Vector3D normalize() {
        return Vector3D.normalize(this);
    }

    public static Vector3D normalize(Vector3D v) {
        return v.divide(v.length());
    }

    public static Vector3D random(double min, double max) {
        Random rand = new Random();
        return new Vector3D(rand.nextDouble(min, max), rand.nextDouble(min, max), rand.nextDouble(min, max));
    }

    public static Vector3D random_in_unit_sphere() {
        while (true) {
            Vector3D p = random(-1,1);
            if(p.lengthSquared() < 1) {
                return p;
            }
        }
    }

    public static Vector3D random_unit_vector() {
        return random_in_unit_sphere().normalize();
    }

    public static Vector3D random_in_hemisphere(Vector3D normal) {
        Vector3D in_unit_sphere = random_in_unit_sphere();
        if (Vector3D.dotProduct(in_unit_sphere,normal) > 0.0) {
            return in_unit_sphere;
        } else {
            return in_unit_sphere.invert();
        }
    }

    public static Vector3D random_in_unit_disk() {
        Random rand = new Random();
        while (true) {
            Vector3D p = new Vector3D(rand.nextDouble(-1,1), rand.nextDouble(-1,0),0);
            if (p.lengthSquared() < 1) {
                return p;
            }
        }
    }

    public boolean nearZero() {
        double s = 1e-8;
        return (Math.abs(this.xMag) < s) && (Math.abs(this.yMag) < s) && (Math.abs(this.zMag) < s);
    }

    public static Vector3D reflect(Vector3D v, Vector3D n) {
        double d = 2 * Vector3D.dotProduct(v,n);
        return v.subtract(n.multiply(d));
    }

    public void setVec(Vector3D v) {
        this.xMag = v.getX();
        this.yMag = v.getY();
        this.zMag = v.getZ();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector3D v) {
            return equals(this, v);
        }
        return false;
    }

    /**
     * Refracts two vectors
     *
     * @param uv - first vector
     * @param n - second vector
     * @param etai_over_etat - theta
     * @return
     */
    public static Vector3D refract(Vector3D uv, Vector3D n, double etai_over_etat) {
        double cos_theta = Math.min(dotProduct(uv.invert(), n), 1.0);
        Vector3D r_out_perp = (uv.add(n.multiply(cos_theta))).multiply(etai_over_etat);
        Vector3D r_out_parallel =  n.multiply(-sqrt(Math.abs(1.0 - r_out_perp.lengthSquared())));
        return r_out_perp.add(r_out_parallel);
    }

    public static Boolean equals(Vector3D v, Vector3D u) {
        double epsilon = 0.000001d;
        return Math.abs(v.getX() - u.getX()) < epsilon &&
        Math.abs(v.getY() - u.getY()) < epsilon &&
        Math.abs(v.getZ() - u.getZ()) < epsilon;
    }
}
