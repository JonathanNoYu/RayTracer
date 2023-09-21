package object3D;

import java.util.ArrayList;
import java.util.List;
import object3D.boundBox.AABB;
import object3D.mats.Lambertian;

public class TriangleMesh extends Hittable {
  private Vector3D origin;
  private List<Vector3D> vertices;
  private List<Vector3D> normVec;
  private List<List<Vector3D>> faces;
  private AABB boundingBox;

  public TriangleMesh(Vector3D origin, List<Vector3D> vertices, List<Vector3D> normVec, List<List<Vector3D>> faces) {
    this.origin = origin;
    this.vertices = vertices;
    this.normVec = normVec;
    this.faces = faces;
  }

  @Override
  public boolean hit(Ray ray, double tMin, double tMax, HitRecord record) {
    HitRecord tempRecord = new HitRecord();
    double closetSoFar = tMax;
    boolean hasHit = false;
    for (List<Vector3D> face : this.faces) {
      // Check this triangle
      List<Vector3D> faceVertices = new ArrayList<>();
      List<Vector3D> faceNormals = new ArrayList<>();
      for (int i = 0; i < face.size(); i += 2) {
        faceVertices.add(face.get(i));
        faceNormals.add(face.get(i + 1));
      }

      if (triangleIntersection(ray, tMin, closetSoFar,
          faceVertices.get(0), faceVertices.get(1), faceVertices.get(2), tempRecord)) {
        record.setRecord(tempRecord);
        closetSoFar = record.getT();
        hasHit = true;
      }
    }

    return hasHit;
  }

  @Override
  public boolean bounding_box(double time0, double time1, AABB output_box) {
    if (boundingBox == null) {
      Vector3D min = new Vector3D(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
      Vector3D max = new Vector3D(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
      for (Vector3D vec : vertices) {
        min.setXMag(Math.min(min.getX(), vec.getX()));
        min.setYMag(Math.min(min.getY(), vec.getY()));
        min.setZMag(Math.min(min.getZ(), vec.getZ()));

        max.setXMag(Math.max(max.getX(), vec.getX()));
        max.setYMag(Math.max(max.getY(), vec.getY()));
        max.setZMag(Math.max(max.getZ(), vec.getZ()));
      }
      this.boundingBox = new AABB(Point3D.vecToPoint(min), Point3D.vecToPoint(max));
    }
    output_box.setAABB(this.boundingBox);
    return true;
  }

  public static boolean triangleIntersection(Ray ray, double tMin, double tMax, Vector3D v0, Vector3D v1, Vector3D v2, HitRecord record) {
    Vector3D edge0 = v1.subtract(v0);
    Vector3D edgeV2V0 = v2.subtract(v0);
    Vector3D normal = Vector3D.cross(edge0, edgeV2V0);
    normal = normal.normalize();

    double normalDotRayDir = normal.dotProduct(ray.direction());
    //if (Math.abs(normalDotRayDir) < 0.01) return false; // parallel

    double d = -normal.dotProduct(v0);
    double t = -(normal.dotProduct(ray.origin()) + d) / normalDotRayDir;
    if (t < 0) return false; // ray is beind the triangle


    if (t < tMin || t > tMax) return false; // not within the distance

    Vector3D C;
    Vector3D P = ray.at(t);

    // Inside-Outside test
    Vector3D vp0 = P.subtract(v1);
    C = edge0.crossProduct(vp0);
    if (normal.dotProduct(C) < 0) return false;

    Vector3D edge1 = v2.subtract(v1);
    Vector3D vp1 = P.subtract(v1);
    C = edge1.crossProduct(vp1);
    if (normal.dotProduct(C) < 0) return false;

    Vector3D edge2 = v0.subtract(v2);
    Vector3D vp2 = P.subtract(v2);
    C = edge2.crossProduct(vp2);
    if (normal.dotProduct(C) < 0) return false;

    record.setT(t);
    record.setPoint(Point3D.vecToPoint(P));
    record.setFront(ray, normal);
    record.setMat(new Lambertian(new Vector3D(0.5, 0.5, 0.5)));
    return true;
  }
}
