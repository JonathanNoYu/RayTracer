package object3D.mats;

import object3D.HitRecord;
import object3D.Ray;
import object3D.Vector3D;

import java.util.Random;

import static object3D.Vector3D.reflect;


public class Dielectric implements Material {

    private double ir;

    public Dielectric(double indexOfRefraction) {
        this.ir = indexOfRefraction;
    }

    @Override
    public boolean scatter(Ray rayIn, HitRecord rec, Vector3D attenuation, Ray scattered) {
        attenuation.setVec(new Vector3D(1,1,1));
        double refractionRatio;
        if (rec.getFront()) {
            refractionRatio = (1.0/this.ir);
        } else {
            refractionRatio = this.ir;
        }
        Vector3D unitDir = rayIn.direction().normalize();
        double cosTheta = Math.min(Vector3D.dotProduct(unitDir.invert(), rec.getNormal()), 1.0);
        double sinTheta = Math.sqrt(1.0 - (cosTheta*cosTheta));

        boolean cannot_refract = (refractionRatio * sinTheta) > 1.0;
        Vector3D direction;
        if (cannot_refract || reflectance(cosTheta, refractionRatio) > new Random().nextDouble(0,1)) {
            direction = reflect(unitDir, rec.getNormal());
        } else {
            direction = Vector3D.refract(unitDir, rec.getNormal(), refractionRatio);
        }
        scattered.setRay(new Ray(rec.getPoint(), direction));
        return true;
    }

    private static double reflectance(double cosine, double ref_idx) {
        // Use Schlick's approximation for reflectance
        double r0 = (1-ref_idx) / (1+ref_idx);
        r0 = r0 * r0;
        return r0 + (1-r0)*Math.pow(1-cosine,5);
    }
}
