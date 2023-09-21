package object3D;

import object3D.boundBox.AABB;
import object3D.mats.Material;

/**
 * 3D Sphere class
 */
public class Sphere extends Hittable {

    private Point3D center;
    private double radius;
    private Material mat;

    public Sphere(Point3D center, double radius, Material mat) {
        this.center = center;
        this.radius = radius;
        this.mat = mat;
    }


    @Override
    public boolean hit(Ray ray, double tMin, double tMax, HitRecord record) {
        Vector3D oc = ray.origin().subtract(this.center);
        double a = ray.direction().lengthSquared();
        double halfB = Vector3D.dotProduct(oc, ray.direction());
        double c = oc.lengthSquared() - (this.radius * this.radius);

        double discriminant = (halfB * halfB) - (a * c);
        if (discriminant < 0) {
            return false;
        }
        double sqrtd = Math.sqrt(discriminant);

        // Find nearest root in range
        double root = (-halfB - sqrtd) / a;
        if (root < tMin || tMax < root) {
            root = (-halfB + sqrtd) / a;
            if (root < tMin || tMax < root) {
                return false;
            }
        }

        record.setT(root);
        record.setPoint(ray.at(record.getT()));
        Vector3D outwardNormal = (record.getPoint().subtract(this.center)).divide(this.radius);
        record.setFront(ray, outwardNormal);
        record.setMat(this.mat);
        return true;
    }

    @Override
    public boolean bounding_box(double time0, double time1, AABB output_box) {
        output_box.setAABB(new AABB(
            Point3D.vecToPoint(this.center.subtract(new Vector3D(radius, radius, radius))),
            Point3D.vecToPoint(this.center.add(new Vector3D(radius, radius, radius)))));
        return true;
    }
}
