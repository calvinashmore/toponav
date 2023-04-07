package math;

public class Vector2 {
  private final double x, y;

  public Vector2(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public Vector2 add(Vector2 that) {
    return new Vector2(this.x + that.x, this.y + that.y);
  }

  public Vector2 mult(double c) {
    return new Vector2(c * x, c * y);
  }

  public double normSquared() {
    return x * x + y * y;
  }

  public double dot(Vector2 that) {
    return this.x * that.x + this.y * that.y;
  }
}
