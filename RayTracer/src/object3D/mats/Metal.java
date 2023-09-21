package object3D.mats;

import object3D.HitRecord;
import object3D.Ray;
import object3D.Vector3D;

/**
 * Metal that reflects objects with Fuzz!! (Reflection Requirement)
 */
public class Metal implements Material {
    private Vector3D albedo;
    double fuzz;
    public Metal(Vector3D a, double fuzz) {
        this.albedo = a;
        if (fuzz < 1) {
            this.fuzz = fuzz;
        } else {
            this.fuzz = 1;
        }
    }
    @Override
    public boolean scatter(Ray rayIn, HitRecord rec, Vector3D attenuation, Ray scattered) {
        Vector3D reflect = Vector3D.reflect(rayIn.direction().normalize(), rec.getNormal());
        scattered.setRay(new Ray(rec.getPoint(), reflect.add(Vector3D.random_unit_vector().multiply(fuzz))));
        attenuation.setVec(this.albedo);
        return (Vector3D.dotProduct(scattered.direction(), rec.getNormal()) > 0);
    }
}
