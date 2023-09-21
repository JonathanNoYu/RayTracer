package object3D.mats;

import object3D.HitRecord;
import object3D.Ray;
import object3D.Vector3D;

/**
 * Material interface that needs a scatter to work. (Material Requirement)
 */
public interface Material {

    /**
     * Based on the material light/rays reflect off this object different. This class will encompass all of that.
     * @param rayIn - ray that is coming toward this object
     * @param rec - Hit Record of where the object is hit at
     * @param attenuation - Color we want to modify
     * @param scattered - the outward going ray.
     * @return - boolean
     */
    boolean scatter(Ray rayIn, HitRecord rec, Vector3D attenuation, Ray scattered);
}
