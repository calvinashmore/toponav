import de.javagl.obj.Obj;
import de.javagl.obj.ObjFace;
import math.Vector3;

import java.util.List;

public class Mesh {

  private final Obj obj;

  Mesh(Obj obj) {
    this.obj = obj;
  }

  /**
   * Find the point on the surface closes to the input.
   */
//  public Vector3 mapToSurface(Vector3 v) {
//
//    // first, get the faces that are near v
//
//  }
//
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
}
