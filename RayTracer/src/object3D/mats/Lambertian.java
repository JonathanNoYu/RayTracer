package object3D.mats;

import object3D.HitRecord;
import object3D.Ray;
import object3D.Vector3D;

/**
 * Material class
 */
public class Lambertian implements Material {
    Vector3D albedo;

    public Lambertian(Vector3D a) {
        this.albedo = a;
    }

    @Override
    public boolean scatter(Ray rayIn, HitRecord rec, Vector3D attenuation, Ray scattered) {
        Vector3D scatterDir = rec.getNormal().add(Vector3D.random_unit_vector());
        if (scatterDir.nearZero()) {
            scatterDir.setVec(rec.getNormal());
        }
        scattered.setRay(new Ray(rec.getPoint(), scatterDir));
        attenuation.setVec(this.albedo);
        return true;
    }
}
