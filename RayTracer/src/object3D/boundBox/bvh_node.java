package object3D.boundBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import object3D.HitRecord;
import object3D.Hittable;
import object3D.Hittable_List;
import object3D.Ray;

public class bvh_node extends Hittable {
  private Hittable left;
  private Hittable right;
  private AABB box;
  private int objectHits = 0;

  public bvh_node(Hittable_List list, double time0, double time1) {
    this(list.getObjects(), 0, list.getObjects().size(), time0, time1);
  }

  public bvh_node(List<Hittable> src_objects, int start, int end, double time0, double time1) {
    List<Hittable> objects = new ArrayList<>(src_objects);
    int axis = new Random().nextInt(0,2);

    int object_span = end - start;

    switch (object_span) {
      case 1 -> this.left = this.right = objects.get(start);
      case 2 -> {
        if (this.box_compare(objects.get(start), objects.get(start+1), axis)) {
          this.left = objects.get(start);
          this.right = objects.get(start + 1);
        } else {
          this.left = objects.get(start + 1);
          this.right = objects.get(start);
        }
      }
      default -> {
        this.sortListHittable(objects, axis);

        int mid = start + (object_span / 2);
        this.left = new bvh_node(objects, start, mid, time0, time1);
        this.right = new bvh_node(objects, mid, end, time0, time1);
      }

    }

    AABB box_left = new AABB();
    AABB box_right = new AABB();
    if (!left.bounding_box(time0, time1, box_left)
      || !right.bounding_box(time0, time1, box_right)) {
      throw new RuntimeException("No bounding box in bvh_node constructor.");
    }
    this.box = AABB.surrounding_box(box_left, box_right);
  }


  @Override
  public boolean hit(Ray ray, double tMin, double tMax, HitRecord record) {
    if (!this.box.hit(ray, tMin, tMax)) return false;

    boolean hit_left = this.left.hit(ray, tMin, tMax, record);
    boolean hit_right;
    if (hit_left) {
      hit_right = this.right.hit(ray, tMin, record.getT(), record);
    } else {
      hit_right = this.right.hit(ray, tMin, tMax, record);
    }
    boolean anyHit = hit_left || hit_right;
    if (anyHit) this.objectHits += 1;
    return anyHit;
  }

  @Override
  public boolean bounding_box(double time0, double time1, AABB output_box) {
    output_box.setAABB(this.box);
    return true;
  }

  public boolean box_compare(Hittable obj1, Hittable obj2, int axis) {
    AABB box_a = new AABB();
    AABB box_b = new AABB();

    if (!obj1.bounding_box(0,0,box_a) || !obj2.bounding_box(0,0,box_b)) {
      throw new IllegalArgumentException("No Bounding Box in bvh_node construct");
    }

    switch (axis) {
      case 0: return box_a.getMinimum().getX() < box_b.getMinimum().getX();
      case 1: return box_a.getMinimum().getY() < box_b.getMinimum().getY();
      default: return box_a.getMinimum().getZ() < box_b.getMinimum().getZ();
    }
  }

  /**
   * Bubble sort list of objects
   *
   * @param objects is the list of objects
   * @param compareInt is the diminsion we are comparing
   */
  public void sortListHittable(List<Hittable> objects, int compareInt) {
    int n = objects.size();
    for(int i = 0; i < n; i++){
      for(int j=0; j < n - 1; j++){
        if (!this.box_compare(objects.get(j), objects.get(j+1), compareInt)) {
          Hittable tempObj = objects.get(j);
          objects.set(j, objects.get(j+1));
          objects.set(j+1, tempObj);
        }
      }
    }
  }

  public int getObjectHits() {
    return objectHits;
  }

  public int getBoxHit() {
    return this.box.getBoxHit();
  }
}
