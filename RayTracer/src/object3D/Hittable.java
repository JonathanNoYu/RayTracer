package object3D;

import object3D.boundBox.AABB;

/**
 * Hittable objects in 3D space
 */
public abstract class Hittable {

    /**
     * Calculates if the given Ray hits this object(s)
     *
     * @param ray - ray being shot into the world any maybe at this object
     * @param tMin - Minimum time the ray has traveled
     * @param tMax - Maximum time the ray can travel to hit (or not hit) this object
     * @param record - Record that stores the hit data
     * @return boolean if the ray hit or not
     */
    public abstract boolean hit(Ray ray, double tMin, double tMax, HitRecord record);

    public abstract boolean bounding_box(double time0, double time1, AABB output_box);

}
