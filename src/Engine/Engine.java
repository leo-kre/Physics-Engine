package Engine;

import Graphics.Window;
import LinearAlgebra.Vector2;
import PhysicsObjects.Cube;
import PhysicsObjects.PhysicsObject;
import PhysicsObjects.Sphere;
import PhysicsObjects.Spring;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Engine {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static long lastTickTimeInNS;
    private static double tpsInNs;

    public static final double ENGINE_SPEED = 1;

    // Constants
    public static final double GRAVITY = 9.81;

    public static ArrayList<Cube> cubeArray = new ArrayList<>();
    public static ArrayList<Sphere> sphereArray = new ArrayList<>();
    public static ArrayList<Spring> springArray = new ArrayList<>();

    public static PhysicsObject currentDragObject;

    public void start(int _tps) {
        tpsInNs = 1_000_000_000.0 / _tps;
        scheduler.scheduleAtFixedRate(this::loop, 0, 1, TimeUnit.MILLISECONDS);
    }

    private void loop() {
        processTick();

        long currentTime = System.nanoTime();
        Main.tickTime = TimeUnit.NANOSECONDS.toMillis(currentTime - lastTickTimeInNS);
        lastTickTimeInNS = currentTime;

        long sleepTime = lastTickTimeInNS + (long) tpsInNs - System.nanoTime();
        if (sleepTime > 0) {
            try {
                TimeUnit.NANOSECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void spawn(Cube _cube) {
        cubeArray.add(_cube);
    }

    public void spawn(Sphere _sphere) {
        sphereArray.add(_sphere);
    }

    public void spawn(Spring _spring) {
        springArray.add(_spring);
    }

    public static void processTick() {
        long timeInSeconds = (System.nanoTime() - lastTickTimeInNS) / 1_000_000;

        for (Cube cube : cubeArray) {

            if(cube == currentDragObject) {

                cube.resetPhysics();
                cube.setPosition(new Vector2(Window.lastDragX - cube.size / 2, Window.lastDragY - cube.size / 2));
            } else {
                cube.addAccelerationVector(new Vector2(0, GRAVITY));

                cube.addAccelerationVector(new Vector2(cube.force.x / cube.mass, cube.force.y / cube.mass));

                cube.addVelocityVector(new Vector2(cube.acceleration.x * timeInSeconds, cube.acceleration.y * timeInSeconds));

                cube.addPositionVector(new Vector2(cube.velocity.x * timeInSeconds * ENGINE_SPEED / 20_000, cube.velocity.y * timeInSeconds * ENGINE_SPEED / 20_000));

                cube.force = new Vector2(0, 0);
            }

            keepPhysicsObjectInScreen(cube);
        }


        for (Sphere sphere : sphereArray) {
            sphere.addAccelerationVector(new Vector2(0, GRAVITY));

            // Update acceleration using applied forces
            sphere.acceleration.x += sphere.force.x / sphere.mass;
            sphere.acceleration.y += sphere.force.y / sphere.mass;

            sphere.velocity.x += sphere.acceleration.x * timeInSeconds;
            sphere.velocity.y += sphere.acceleration.y * timeInSeconds;

            // Update position using kinematic equation
            sphere.position.x += sphere.velocity.x * timeInSeconds * ENGINE_SPEED / 10_000;
            sphere.position.y += sphere.velocity.y * timeInSeconds * ENGINE_SPEED / 10_000;

            sphere.force = new Vector2(0, 0);

            keepPhysicsObjectInScreen(sphere);
        }
    }

    private static void keepPhysicsObjectInScreen(Cube _cube) {
        Vector2 position = _cube.getPosition();
        if(position.x < 0) position.x = 0;
        if(position.y < 0) position.y = 0;
        if(position.x + _cube.size > Main.window.getWidth()) position.x = Main.window.getWidth() - _cube.size;
        if(position.y + _cube.size > Main.window.getHeight()) position.y = Main.window.getHeight() - _cube.size;
    }

    private static void keepPhysicsObjectInScreen(Sphere _sphere) {
        Vector2 position = _sphere.getPosition();
        if(position.x < 0) position.x = 0;
        if(position.y < 0) position.y = 0;
        if(position.x + _sphere.radius > Main.window.getWidth()) position.x = Main.window.getWidth() - _sphere.radius;
        if(position.y + _sphere.radius > Main.window.getHeight()) position.y = Main.window.getHeight() - _sphere.radius;
    }

    public void findPhysicsObject(Vector2 _vector) {
        for(Cube cube : cubeArray) {
            Vector2 position = cube.getPosition();
            if(_vector.x > position.x && _vector.y > position.y && _vector.x < position.x + cube.size && _vector.y < position.y + cube.size) {
                currentDragObject = cube;
                break;
            }
        }
    }
}

