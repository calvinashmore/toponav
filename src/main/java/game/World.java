package game;

import math.Bearing;
import math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class World {

  static World INSTANCE;

  private final List<Entity> allEntities = new ArrayList<>();
  private List<Entity> toAdd = new ArrayList<>();
  private Mesh worldMesh;

  public Player player;
  public OrthoCamera camera;

  public World(Mesh mesh) {
    this.worldMesh = mesh;

    this.player = new Player();
    player.bearing = new Bearing(
        new Vector3(15, 0, 0),
        new Vector3(0, 1, 0),
        new Vector3(1, 0, 0)
    );

    allEntities.add(player);

    this.camera = new OrthoCamera();

    INSTANCE = this;
  }

  public List<Entity> getAllEntities() {
    return allEntities;
  }

  public void spawn(Entity toAdd) {
    this.toAdd.add(toAdd);
  }

  // list entities
  // collisions // could use same octree??
  // mapping to mesh

  public void update(double dt) {
    // update position/bearing on all entities

    // map/attach all entities to mesh surface
    // may need to build triangle/face graph to deal with self-intersections, and potentially incorporate that into
    // bearing/entity state

    allEntities.addAll(toAdd);
    toAdd.clear();

    List<Entity> toRemove = new ArrayList<>();

    for (Entity entity : allEntities) {
      entity.update(dt);
      entity.bearing = worldMesh.mapToSurface(entity.bearing);

      if(!entity.alive) {
        toRemove.add(entity);
      }
    }

    allEntities.removeAll(toRemove);

    camera.bearing = player.bearing;
  }
}
