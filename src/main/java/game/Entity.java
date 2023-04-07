package game;

import math.Bearing;
import math.Vector3;

// This represents an object on a mesh/2d manifold that can move on that surface
// this has a settable velocity and angular velocity.
public class Entity {
  protected Bearing bearing;
  protected Vector3 velocity = Vector3.ZERO;
  protected double angularVelocity = 0;

  protected double velocityDecay = .9;

  boolean alive = true;

  public void update(double dt) {
    updatePosition(dt);

    velocity = velocity.mult(velocityDecay);
    angularVelocity *= velocityDecay*.5;
  }

  protected void updatePosition(double dt) {
    Vector3 newPosition = bearing.getPosition().add(velocity.mult(dt));
    Vector3
        newForward =
        bearing.getForward()
            .mult(Math.cos(angularVelocity * dt))
            .add(bearing.getRight().mult(Math.sin(angularVelocity * dt)));
//    Vector3
//        newRight =
//        bearing.getRight()
//            .mult(Math.cos(angularVelocity * dt))
//            .add(bearing.getForward().mult(-Math.sin(angularVelocity * dt)));
    bearing = new Bearing(newPosition, newForward, bearing.getNormal());
  }

  public Bearing getBearing() {
    return bearing;
  }
}
