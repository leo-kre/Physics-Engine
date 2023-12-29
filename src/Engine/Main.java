package Engine;

import Graphics.Canvas;
import Graphics.Window;
import LinearAlgebra.Rotation;
import LinearAlgebra.Vector2;
import PhysicsObjects.*;

public class Main {

    public static Canvas canvas = new Canvas();
    public static Window window = new Window("Physics-Engine", canvas);
    public static Engine Engine = new Engine();

    public static long tickTime;
    public static long renderTime;

    public static void main(String[] args) {
        Engine.start(144);
        canvas.startRender();

        //Anchor anchor = new Anchor(new Vector2(250, 250));
        //Engine.spawn(anchor);

        Sphere sphere = new Sphere(new Vector2(100, 100), new Rotation(0, 0), 100, 0.1);
        Engine.spawn(sphere);

        Sphere sphere2 = new Sphere(new Vector2(500, 600), new Rotation(0, 0), 100, 0.1);
        Engine.spawn(sphere2);

        //Spring spring = new Spring(0.01, sphere, sphere2);
        //Engine.spawn(spring);

        Rod rod = new Rod(sphere, sphere2);
        Engine.spawn(rod);


        Cube cube = new Cube(new Vector2(300, 300), new Rotation(0, 0), 50, 0.1);
        //Engine.spawn(cube);

        //Rod rod2 = new Rod(sphere2, cube);
        //Engine.spawn(rod2);
    }
}