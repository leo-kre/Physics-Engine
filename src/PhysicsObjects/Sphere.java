package PhysicsObjects;

import LinearAlgebra.Rotation;
import LinearAlgebra.Vector2;

public class Sphere implements PhysicsObject {

    public Vector2 position;
    public Rotation rotation;

    public Vector2 force = new Vector2(0, 0);
    public Vector2 acceleration = new Vector2(0, 0);
    public Vector2 velocity = new Vector2(0, 0);

    public int size;
    public double mass;

    public Sphere(Vector2 _position, Rotation _rotation, int _radius, double _mass) {
        position = _position;
        rotation = _rotation;
        size = _radius;
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

    @Override
    public Vector2 getForce() {
        return force;
    }

    @Override
    public void setForce(Vector2 _force) {
        force = _force;
    }

    @Override
    public Vector2 getAcceleration() {
        return acceleration;
    }

    @Override
    public double getMass() {
        return mass;
    }

    @Override
    public void setAcceleration(Vector2 _acceleration) {
        acceleration = _acceleration;
    }

    public void addForceVector(Vector2 _vector) {
        force.x += _vector.x;
        force.y += _vector.y;
    }

    @Override
    public Vector2 getVelocity() {
        return velocity;
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
    public int getSize() {
        return size;
    }

    @Override
    public void setPosition(Vector2 _position) {
        position = _position;
    }

    @Override
    public void resetPhysics() {
        force = new Vector2(0, 0);
        acceleration = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
    }
}
