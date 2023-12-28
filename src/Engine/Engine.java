package Engine;

import Graphics.Window;
import LinearAlgebra.Vector2;
import PhysicsObjects.Cube;
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

    public static final double ENGINE_SPEED = 2;

    // Constants
    public static final double GRAVITY = 9.81;

    public static ArrayList<Cube> cubeArray = new ArrayList<>();
    public static ArrayList<Sphere> sphereArray = new ArrayList<>();
    public static ArrayList<Spring> springArray = new ArrayList<>();

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
            cube.addAccelerationVector(new Vector2(0, GRAVITY));

            // Update acceleration using applied forces
            cube.acceleration.x += cube.force.x / cube.mass;
            cube.acceleration.y += cube.force.y / cube.mass;

            cube.velocity.x += cube.acceleration.x * timeInSeconds;
            cube.velocity.y += cube.acceleration.y * timeInSeconds;

            // Update position using kinematic equation
            cube.position.x += cube.velocity.x * timeInSeconds * ENGINE_SPEED / 10_000;
            cube.position.y += cube.velocity.y * timeInSeconds * ENGINE_SPEED / 10_000;

            cube.force = new Vector2(0, 0);

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
        if(_cube.position.x < 0) _cube.position.x = 0;
        if(_cube.position.y < 0) _cube.position.y = 0;
        if(_cube.position.x + _cube.size > Main.window.getWidth()) _cube.position.x = Main.window.getWidth() - _cube.size;
        if(_cube.position.y + _cube.size > Main.window.getHeight()) _cube.position.y = Main.window.getHeight() - _cube.size;
    }

    private static void keepPhysicsObjectInScreen(Sphere _sphere) {
        if(_sphere.position.x < 0) _sphere.position.x = 0;
        if(_sphere.position.y < 0) _sphere.position.y = 0;
        if(_sphere.position.x + _sphere.radius > Main.window.getWidth()) _sphere.position.x = Main.window.getWidth() - _sphere.radius;
        if(_sphere.position.y + _sphere.radius > Main.window.getHeight()) _sphere.position.y = Main.window.getHeight() - _sphere.radius;
    }
}
