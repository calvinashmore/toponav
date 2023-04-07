package game;

public class Player extends Entity {

  public void moveForward(double amount) {
    velocity = velocity.add(bearing.getForward().mult(amount));
  }

  public void turn(double amount) {
    angularVelocity += amount;
  }

  public void fire() {

    Bullet b = new Bullet();
    b.bearing = bearing;
    b.velocity = bearing.getForward().mult(1);
    World.INSTANCE.spawn(b);
  }
}
