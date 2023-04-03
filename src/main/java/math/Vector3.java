package math;

public class Vector3 {

  public static final Vector3 ZERO = new Vector3(0, 0, 0);
  public static final Vector3 MIN_VALUE = new Vector3(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
  public static final Vector3 MAX_VALUE = new Vector3(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

  private final double x, y, z;

  public Vector3(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getZ() {
    return z;
  }

  public Vector3 add(Vector3 that) {
    return new Vector3(this.x + that.x, this.y + that.y, this.z + that.z);
  }

  public Vector3 mult(double c) {
    return new Vector3(c * x, c * y, c * z);
  }

  public double normSquared() {
    return x * x + y * y + z * z;
  }

  public double magnitude() {
    return Math.sqrt(normSquared());
  }

  public Vector3 unit() {
    double magnitude = magnitude();
    return new Vector3(x / magnitude, y / magnitude, z / magnitude);
  }

  public double dot(Vector3 that) {
    return this.x * that.x + this.y * that.y + this.z + that.z;
  }

  static public Vector3 cross(Vector3 a, Vector3 b) {
    return new Vector3(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
  }

  public static Vector3 min(Vector3 a, Vector3 b) {
    return new Vector3(Math.min(a.x, b.x), Math.min(a.y, b.y), Math.min(a.z, b.z));
  }

  public static Vector3 max(Vector3 a, Vector3 b) {
    return new Vector3(Math.max(a.x, b.x), Math.max(a.y, b.y), Math.max(a.z, b.z));
  }

  @Override
  public String toString() {
    return "Vector3{" +
        "x=" + x +
        ", y=" + y +
        ", z=" + z +
        '}';
  }
}
