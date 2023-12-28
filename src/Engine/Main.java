package Engine;

import Graphics.Canvas;
import Graphics.Window;
import LinearAlgebra.Rotation;
import LinearAlgebra.Vector2;
import PhysicsObjects.Anchor;
import PhysicsObjects.Cube;
import PhysicsObjects.Rod;
import PhysicsObjects.Sphere;

public class Main {

    public static Canvas canvas = new Canvas();
    public static Window window = new Window("Physics-Engine", canvas);
    public static Engine Engine = new Engine();

    public static long tickTime;
    public static long renderTime;

    public static void main(String[] args) {
        Engine.start(144);
        canvas.startRender();

        Anchor anchor = new Anchor(new Vector2(250, 250));
        Engine.spawn(anchor);

        Sphere sphere = new Sphere(new Vector2(100, 100), new Rotation(0, 0), 100, 0.1);
        Engine.spawn(sphere);

        Rod rod = new Rod(anchor, sphere);
        Engine.spawn(rod);

        //Engine.spawn(new Cube(new Vector2(300, 300), new Rotation(0, 0), 50, 0.1));
    }
}