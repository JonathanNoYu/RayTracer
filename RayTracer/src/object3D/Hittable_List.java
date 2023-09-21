package object3D;

import java.util.ArrayList;
import java.util.List;
import object3D.boundBox.AABB;

/**
 * List of hittable objects in 3D space.
 */
public class Hittable_List extends Hittable {

  private List<Hittable> objects;

  // Constructor
  public Hittable_List() {
    this.objects = new ArrayList<>();
  }

  /**
   * Adds an object to this list
   * @param object - hittable object to be added
   */
  public void add(Hittable object) {
    this.objects.add(object);
  }

  /**
   * Clears the list of objects if needed
   */
  public void clear() {
    this.objects = new ArrayList<>();
  }

  @Override
  public boolean hit(Ray ray, double tMin, double tMax, HitRecord record) {
    HitRecord tempRec = new HitRecord();
    boolean hit_anything = false;
    double closest_so_far = tMax;

    for (Hittable object : this.objects) {
      if (object.hit(ray, tMin, closest_so_far, tempRec)) {
        hit_anything = true;
        record.setRecord(tempRec);
        closest_so_far = record.getT();
      }
    }

    return hit_anything;
  }

  @Override
  public boolean bounding_box(double time0, double time1, AABB output_box) {
    if(this.objects.isEmpty()) return false;

    AABB temp_box = new AABB();
    boolean first_box = true;

    for (Hittable object : objects) {
      if (!object.bounding_box(time0, time1, temp_box)) return false;

      if (first_box) {
        output_box.setAABB(temp_box);
      } else {
        output_box.setAABB(AABB.surrounding_box(output_box, temp_box));
      }

      first_box = false;
    }

    return true;
  }

  /**
   * Gets the list of objects in this hittable list.
   *
   * @return list of objects
   */
  public List<Hittable> getObjects() {
    return objects;
  }
}
