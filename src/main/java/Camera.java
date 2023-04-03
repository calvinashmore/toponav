import math.Vector2;
import math.Vector3;

public interface Camera {

  Vector2 project (Vector3 p);

  // project 3d point to 2d screen coordinates (possibly unitized)
  // can have ortho camera & other kinds

  // will need position
  // subclass of entity??
}
