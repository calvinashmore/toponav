package math;

// Making this class immutable. hopefully this isn't a terrible idea
public class Bearing {
  private Vector3 forward;
  private Vector3 normal;
  private Vector3 right;
  private Vector3 position;

  public Bearing(Vector3 position, Vector3 forward, Vector3 normal) {
    this.position = position;
//    this.forward = forward.unit();

    this.right = Vector3.cross(forward, normal).unit();
    this.forward = Vector3.cross(right,normal).unit();
    this.normal = Vector3.cross(this.forward, right).unit(); // is the sign correct?

  }

//  public Bearing adjust(Vector3 adjustedPosition, Vector3 adjustedNormal) {
//    return new Bearing(adjustedPosition, forward, adjustedNormal);
//  }

//  public Bearing turn(double theta) {
//    double c = Math.cos(theta);
//    double s = Math.sin(theta);
//    Vector3 adjustedForward = forward.mult(c).add(right.mult(s)).unit();
//    Vector3 adjustedRight = forward.mult(-s).add(right.mult(c)).unit();
//    Vector3 adjustedNormal = Vector3.cross(adjustedForward, adjustedRight).unit();
//    return new Bearing(position, adjustedForward, adjustedNormal);
//  }

  public Vector3 getPosition() {
    return position;
  }

  public Vector3 getForward() {
    return forward;
  }

  public Vector3 getRight() {
    return right;
  }

  public Vector3 getNormal() {
    return normal;
  }

  @Override
  public String toString() {
    return "Bearing{" +
        "forward=" +
        forward +
        ", normal=" +
        normal +
        ", right=" +
        right +
        ", position=" +
        position +
        '}';
  }
}
