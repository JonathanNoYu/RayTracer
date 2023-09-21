package object3D;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Parses object files,
 */
public class  ObjParser {
  List<Vector3D> vertices;
  List<Vector3D> normVec;
  List<List<Vector3D>> faces;
  String[] tokens;
  public ObjParser() {
    this.vertices = new ArrayList<>();
    this.normVec = new ArrayList<>();
    this.faces = new ArrayList<>();
    this.tokens = new String[]{"v", "vn", "f"};
  }

  public ObjParser(String filePath) {
    this();
    this.parseObj(Path.of(filePath).toFile());
  }

  public void parseObj(File filePath) {
    Scanner sc;
    try {
      sc = new Scanner(filePath);
    } catch (Exception e) {
      throw new IllegalArgumentException(e.getMessage());
    }
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      String token = s.substring(0,2);
      switch (token) {
        case "v " -> this.addVertex(s);
        case "vn" -> this.addNormal(s);
        case "f " -> this.addFace(s);
        default -> { }
      }
    }
  }

  private void addVertex(String s) {
    addVectorToList(s, this.vertices);
  }

  public static void addVectorToList(String line, List<Vector3D> list) {
    Scanner sc = new Scanner(line);
    String token = sc.next(); // Skip the token
    double d1 = sc.nextDouble();
    double d2 = sc.nextDouble();
    double d3 = sc.nextDouble();

    Vector3D vec = new Vector3D(d1, d2, d3);
    list.add(vec);
  }

  public void addNormal(String line) {
    addVectorToList(line, this.normVec);
  }

  public void addFace(String line) {
    String[] splitLine = line.split("//");
    StringBuilder spacedOutLine = new StringBuilder();
    for (String s : splitLine) {
      spacedOutLine.append(s).append(" ");
    }
    String spacedOut = spacedOutLine.toString();
    Scanner sc = new Scanner(spacedOut);
    List<Vector3D> newList = new ArrayList<>();
    String token = sc.next(); // Skips the token
    while (sc.hasNextInt()) {
      int vIndex = sc.nextInt();
      int vnIndex = sc.nextInt();
      Vector3D vertex = this.vertices.get(vIndex - 1);
      Vector3D normVec = this.normVec.get(vnIndex - 1);
      newList.add(vertex);
      newList.add(normVec);
    }
    this.faces.add(newList);
  }

  public Hittable buildObject() {
    return new TriangleMesh(new Vector3D(0,0,0), this.vertices, this.normVec, this.faces);
  }
}
