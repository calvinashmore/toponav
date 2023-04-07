import de.javagl.obj.*;
import game.Mesh;
import math.Vector3;

public class Main {
  public static void main(String[] args)throws Exception  {
    System.out.println("aaaaaaaaaaaaaaaa");

    Obj obj = ObjReader.read(Main.class.getClassLoader().getResourceAsStream("jkjkjkl.obj"));

    Mesh mesh = new Mesh(obj);

    System.out.println(mesh.mapToSurface(new Vector3(15.0,0,0)));
    System.out.println(mesh.mapToSurface(mesh.mapToSurface(new Vector3(15.0,0,0))));
    System.out.println(mesh.mapToSurface(new Vector3(15.5,0,0)));
    System.out.println(mesh.mapToSurface(new Vector3(15.0,1,0)));


  }

}
