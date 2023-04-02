import de.javagl.obj.FloatTuple;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;

import java.io.FileInputStream;

public class Main {
  public static void main(String[] args)throws Exception  {
    System.out.println("aaaaaaaaaaaaaaaa");

    Obj obj = ObjReader.read(Main.class.getClassLoader().getResourceAsStream("jkjkjkl.obj"));
    FloatTuple vertex = obj.getVertex(0);

  }
}
