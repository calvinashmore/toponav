package game;

import math.Bearing;
import math.Vector2;
import math.Vector3;

public class OrthoCamera implements  Camera{

  Bearing bearing;


  @Override
  public Vector2 project(Vector3 p) {
    Vector3 pAdjust = p.sub(bearing.getPosition());

    // assume unitized?
    //Vector3 right = Vector3.cross(bearing.getForward(),bearing.getNormal()).unit();

    // this is all there is to it, but will need to account for occlusion
    return new Vector2(bearing.getRight().dot(pAdjust), bearing.getForward().dot(pAdjust));
  }
}
