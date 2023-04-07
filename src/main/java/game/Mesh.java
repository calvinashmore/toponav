package game;

import com.google.common.collect.ImmutableList;
import de.javagl.obj.FloatTuple;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjFace;
import math.Bearing;
import math.Boundable;
import math.Octree;
import math.Vector3;

import java.util.Comparator;
import java.util.List;

public class Mesh {
  private static final double MAPPING_RADIUS = 4.0;

  private final Obj obj;
  private final Octree<FaceWrapper> octree;

  public Mesh(Obj obj) {
    this.obj = obj;

    ImmutableList.Builder<FaceWrapper> faces = ImmutableList.builder();
    for (int i = 0; i < obj.getNumFaces(); i++) {
      faces.add(new FaceWrapper(obj.getFace(i)));
    }
    this.octree = new Octree<>(8, faces.build());
  }

  /**
   * Find the point on the surface closes to the input.
   */
  public Vector3 mapToSurface(Vector3 v) {

    // first, get the faces that are near v
    ImmutableList<FaceWrapper> faces = octree.getContents(v, MAPPING_RADIUS);

    // get the closest face given centroid
    return faces.stream()
        .min(Comparator.comparingDouble(face -> v.sub(face.centroid).normSquared()))
        .map(face -> face.projectToPlane(v))
        .orElseThrow(() -> new IllegalStateException("Can't find faces near " + v));
  }

  public Bearing mapToSurface(Bearing v) {

    // **************
    // we will also need to confine to edges

    // first, get the faces that are near v
    ImmutableList<FaceWrapper> faces = octree.getContents(v.getPosition(), MAPPING_RADIUS);

    // get the closest face given centroid
    return faces.stream()
        .min(Comparator.comparingDouble(face -> v.getPosition().sub(face.centroid).normSquared()))
        .map(face -> face.projectToPlane(v))
        .orElseThrow(() -> new IllegalStateException("Can't find faces near " + v));
  }

  public ImmutableList<FaceWrapper> getFacesNear(Vector3 v, double radius) {
    return octree.getContents(v, radius);
  }

  //  private List<ObjFace> getNearFaces(Vector3 v){
  //    // painfully dumb impl for now
  //    for(int i = 0;i<obj.getNumFaces();i++) {
  //
  //    }
  //  }

  // should we import LVect classes?
  // there is a quadtree in there somewhere as well
  // what can we do with an octree? <------ this needs to contain faces, not just points

  // could potentially bundle all important logic and put it into processing


  private static Vector3 from(FloatTuple v) {
    return new Vector3(v.getX(), v.getY(), v.getZ());
  }

  public class FaceWrapper implements Boundable {
    // assumptions: planar, non-degenerate
    final ObjFace face;

    // all of these are effectively final
    Vector3 min, max;
    Vector3 centroid;
    Vector3 normal;

    FaceWrapper(ObjFace face) {
      this.face = face;
      min = Vector3.MAX_VALUE;
      max = Vector3.MIN_VALUE;
      centroid = Vector3.ZERO;

      // this equation only works if the face is a triangle 
//      area = Math.abs(normal.dot(
//          Vector3.cross(
//              vertexAt(1).sub(vertexAt(0)),
//              vertexAt(2).sub(vertexAt(0)))));

      for (int k = 0; k < face.getNumVertices(); k++) {
        Vector3 v = vertexAt(k);
        min = Vector3.min(min, v);
        max = Vector3.max(max, v);
        centroid = centroid.add(v);
      }
      this.centroid = centroid.mult(1.0 / face.getNumVertices());

      Vector3 u = centroid.sub(vertexAt(0)).unit();
      Vector3 v = centroid.sub(vertexAt(1)).unit();
      normal = Vector3.cross(u, v).unit();
    }

    public Vector3 projectToPlane(Vector3 v) {
      Vector3 r = v.sub(centroid);
      return v.add(normal.mult(-r.dot(normal)));
    }

    public Vector3 vertexAt(int i) {
      return from(obj.getVertex(face.getVertexIndex(i)));
    }

    public Bearing projectToPlane(Bearing v) {
      // ********* should interpolate the normal
      // new bearing should also have either forward or reversed normal

      // need to get barycentric coordinates for weighting
      double area0 = normal.dot(
          Vector3.cross(vertexAt(1),)
      );


      obj.getNormal(face.getNormalIndex());

      return new Bearing(projectToPlane(v.getPosition()), v.getForward(), normal);
    }

    @Override
    public Vector3 getMinimumBound() {
      return min;
    }

    @Override
    public Vector3 getMaximumBound() {
      return max;
    }

    public ObjFace getFace() {
      return face;
    }

    @Override
    public String toString() {
      return "FaceWrapper{" + "min=" + min + ", max=" + max + '}';
    }
  }
}
