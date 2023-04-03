import math.Vector2;
import math.Vector3;

public class OrthoCamera implements  Camera{

  private Vector3 direction;
  private Vector3 normal;

  @Override
  public Vector2 project(Vector3 p) {

    // assume unitized?
    Vector3 right = Vector3.cross(direction,normal);

    // this is all there is to it, but will need to account for occlusion
    return new Vector2(right.dot(p), normal.dot(p));
  }
}
