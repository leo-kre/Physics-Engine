package Engine;

import Graphics.Window;
import LinearAlgebra.Vector2;
import PhysicsObjects.*;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Engine {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static int physicsObjectCount = 0;

    private static long lastTickTimeInNS;
    private static double tpsInNs;

    public static final double ENGINE_SPEED = 1;
    public static final double DAMPNIG_FACTOR = 0.4;

    // Constants
    public static final double GRAVITY = 9.81;

    public static ArrayList<Anchor> anchorArray = new ArrayList<>();
    public static ArrayList<Cube> cubeArray = new ArrayList<>();
    public static ArrayList<Sphere> sphereArray = new ArrayList<>();
    public static ArrayList<Spring> springArray = new ArrayList<>();

    public static PhysicsObject currentDragObject;
    public static PhysicsObject currentHoverObject;

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

    public static void processTick() {
        long timeInSeconds = (System.nanoTime() - lastTickTimeInNS) / 1_000_000;

        for (Cube cube : cubeArray) {
            if(cube == currentDragObject) {
                cube.resetPhysics();

                if (Window.lastDragX != -1 && Window.lastDragY != -1) {
                    // Calculate displacement based on the difference between current and last drag positions
                    double displacementX = (Window.lastDragX - (double) cube.getSize() / 2) - cube.getPosition().x;
                    double displacementY = (Window.lastDragY - (double) cube.getSize() / 2) - cube.getPosition().y;

                    // Update position gradually for a smoother effect
                    cube.addPositionVector(new Vector2(displacementX * DAMPNIG_FACTOR, displacementY * DAMPNIG_FACTOR));
                }

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
            if(sphere == currentDragObject) {
                sphere.resetPhysics();

                if (Window.lastDragX != -1 && Window.lastDragY != -1) {
                    // Calculate displacement based on the difference between current and last drag positions
                    double displacementX = (Window.lastDragX - (double) sphere.getSize() / 2) - sphere.getPosition().x;
                    double displacementY = (Window.lastDragY - (double) sphere.getSize() / 2) - sphere.getPosition().y;

                    // Update position gradually for a smoother effect
                    sphere.addPositionVector(new Vector2(displacementX * DAMPNIG_FACTOR, displacementY * DAMPNIG_FACTOR));
                }

            } else {
                sphere.addAccelerationVector(new Vector2(0, GRAVITY));

                sphere.addAccelerationVector(new Vector2(sphere.force.x / sphere.mass, sphere.force.y / sphere.mass));

                sphere.addVelocityVector(new Vector2(sphere.acceleration.x * timeInSeconds, sphere.acceleration.y * timeInSeconds));

                sphere.addPositionVector(new Vector2(sphere.velocity.x * timeInSeconds * ENGINE_SPEED / 20_000, sphere.velocity.y * timeInSeconds * ENGINE_SPEED / 20_000));

                sphere.force = new Vector2(0, 0);
            }

            keepPhysicsObjectInScreen(sphere);
        }
    }

    public void spawn(Anchor _anchor) {
        anchorArray.add(_anchor);
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

    private static void keepPhysicsObjectInScreen(PhysicsObject _obj) {
        Vector2 position = _obj.getPosition();
        if(position.x < 0) position.x = 0;
        if(position.y < 0) position.y = 0;
        if(position.x + _obj.getSize() > Main.window.getWidth()) position.x = Main.window.getWidth() - _obj.getSize();
        if(position.y + _obj.getSize() > Main.window.getHeight()) position.y = Main.window.getHeight() - _obj.getSize();
    }

    public PhysicsObject findPhysicsObject(Vector2 _vector) {
        // Bounding box size for click check
        final int boundingBoxSize = 10;

        PhysicsObject result = null;
        double minDistance = Double.MAX_VALUE;

        // Iterate through anchors and perform bounding box check
        for (Anchor anchor : anchorArray) {
            Vector2 position = anchor.getPosition();
            int size = anchor.getSize();

            // Bounding box check
            if (_vector.x >= position.x - boundingBoxSize && _vector.y >= position.y - boundingBoxSize &&
                    _vector.x < position.x + size + boundingBoxSize && _vector.y < position.y + size + boundingBoxSize) {

                // Accurate check
                double distance = _vector.distanceTo(position);
                if (distance < minDistance) {
                    result = anchor;
                    minDistance = distance;
                }
            }
        }

        // Iterate through cubes and perform bounding box check
        for (Cube cube : cubeArray) {
            Vector2 position = cube.getPosition();
            int size = cube.getSize();

            // Bounding box check
            if (_vector.x >= position.x - boundingBoxSize && _vector.y >= position.y - boundingBoxSize &&
                    _vector.x < position.x + size + boundingBoxSize && _vector.y < position.y + size + boundingBoxSize) {

                // Accurate check
                double distance = _vector.distanceTo(position);
                if (distance < minDistance) {
                    result = cube;
                    minDistance = distance;
                }
            }
        }

        // Iterate through spheres and perform bounding box check
        for (Sphere sphere : sphereArray) {
            Vector2 position = sphere.getPosition();
            int radius = sphere.getSize();

            // Bounding box check
            if (_vector.x >= position.x - boundingBoxSize && _vector.y >= position.y - boundingBoxSize &&
                    _vector.x < position.x + radius + boundingBoxSize && _vector.y < position.y + radius + boundingBoxSize) {

                // Accurate check
                double distance = _vector.distanceTo(position);
                if (distance < minDistance) {
                    result = sphere;
                    minDistance = distance;
                }
            }
        }

        // Additional checks for other object types

        return result;
    }

}

