package PhysicsObjects;

import LinearAlgebra.Rotation;
import LinearAlgebra.Vector2;

public class Cube implements PhysicsObject {

    public static Vector2 position;
    public Rotation rotation;

    public Vector2 force = new Vector2(0, 0);
    public Vector2 acceleration = new Vector2(0, 0);
    public Vector2 velocity = new Vector2(0, 0);

    public int size;
    public double mass;

    public Cube(Vector2 _position, Rotation _rotation, int _size, double _mass) {
        position = _position;
        rotation = _rotation;
        size = _size;
        mass = _mass;
    }

    public void addRotation(Rotation _rotation) {
        rotation.x += _rotation.x;
        rotation.y += _rotation.y;
    }

    public void addPositionVector(Vector2 _vector) {
        position.x += _vector.x;
        position.y += _vector.y;
    }

    public void addForceVector(Vector2 _vector) {
        force.x += _vector.x;
        force.y += _vector.y;
    }

    public void addAccelerationVector(Vector2 _vector) {
        acceleration.x += _vector.x;
        acceleration.y += _vector.y;
    }

    public void addVelocityVector(Vector2 _vector) {
        velocity.x += _vector.x;
        velocity.y += _vector.y;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public void setPosition(Vector2 _vector) {
        position = _vector;
    }

    @Override
    public void resetPhysics() {
        force = new Vector2(0, 0);
        acceleration = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
    }
}
