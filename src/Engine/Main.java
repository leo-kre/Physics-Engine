package Engine;

import Graphics.Canvas;
import Graphics.Window;
import LinearAlgebra.Rotation;
import LinearAlgebra.Vector2;
import PhysicsObjects.Cube;
import PhysicsObjects.Sphere;
import PhysicsObjects.Spring;

public class Main {

    public static Canvas canvas = new Canvas();
    public static Window window = new Window("Physics-Engine", canvas);
    public static Engine Engine = new Engine();

    public static long tickTime;
    public static long renderTime;

    public static void main(String[] args) {
        Engine.start(1000);
        canvas.startRender();

        Cube cube = new Cube(new Vector2(100, 100), new Rotation(0, 0), 100, 0.1);
        Engine.spawn(cube);

        Sphere sphere = new Sphere(new Vector2(400, 100), new Rotation(0, 0), 500, 10.1);
        Engine.spawn(sphere);

        Spring spring = new Spring(10, new Vector2(200, 200), new Vector2(400, 300));
        Engine.spawn(spring);
    }
}