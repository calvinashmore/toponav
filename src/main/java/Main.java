import com.google.common.collect.ImmutableList;
import de.javagl.obj.*;
import math.Boundable;
import math.Octree;
import math.Vector3;

import java.io.FileInputStream;

public class Main {
  public static void main(String[] args)throws Exception  {
    System.out.println("aaaaaaaaaaaaaaaa");

    Obj obj = ObjReader.read(Main.class.getClassLoader().getResourceAsStream("jkjkjkl.obj"));

    class FaceWrapper implements Boundable {
      ObjFace face;
      Vector3 min, max;

      FaceWrapper(ObjFace face) {
        this.face = face;
        min = Vector3.MAX_VALUE;
        max = Vector3.MIN_VALUE;

        for (int k=0;k<face.getNumVertices();k++) {
          FloatTuple vertex = obj.getVertex(face.getVertexIndex(k));
          Vector3 v = new Vector3(vertex.getX(), vertex.getY(), vertex.getZ());
          min = Vector3.min(min, v);
          max = Vector3.max(max, v);
        }
      }

      @Override
      public Vector3 getMinimumBound() {
        return min;
      }

      @Override
      public Vector3 getMaximumBound() {
        return max;
      }

      @Override
      public String toString() {
        return "FaceWrapper{" +
            "min=" + min +
            ", max=" + max +
            '}';
      }
    }

    ImmutableList.Builder<FaceWrapper> facesBuilder = ImmutableList.builder();


    for (int i=0;i< obj.getNumFaces();i++) {
      ObjFace face = obj.getFace(i);
      facesBuilder.add(new FaceWrapper(face));
    }

    Octree<FaceWrapper> octree = new Octree<>(8, facesBuilder.build());

    System.out.println(
    octree.getContents(new Vector3(15,0,0), 1.5));
  }
}
