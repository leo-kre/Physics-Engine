package Engine;

import Graphics.Canvas;
import Graphics.Window;
import LinearAlgebra.Rotation;
import LinearAlgebra.Vector2;
import PhysicsObjects.Cube;

public class Main {

    public static Canvas canvas = new Canvas();
    public static Window window = new Window("Physics-Engine", canvas);
    public static Engine Engine = new Engine();

    public static long tickTime;
    public static long renderTime;

    public static void main(String[] args) {
        Engine.start(40);
        canvas.startRender();

        int count = 150;

        for(int x = 0; x < count; x++) {
            for(int y = 0; y < count; y++) {
                Engine.spawn(new Cube(new Vector2(10 + x * 3, 10 + y * 3), new Rotation(0, 0), 50, 0.1));
            }
        }

        Engine.physicsObjectCount = Engine.anchorArray.size() + Engine.cubeArray.size() + Engine.sphereArray.size() + Engine.springArray.size();

    }
}