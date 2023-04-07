package game;

public class Bullet extends Entity {

  private double ttl = 10;

  public Bullet() {
    velocityDecay = 1;
  }

  @Override
  public void update(double dt) {
    super.update(dt);
    ttl -= dt;

    if (ttl <= 0) {
      alive = false;
    }
  }
}
