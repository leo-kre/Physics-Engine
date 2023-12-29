package PhysicsObjects;

import LinearAlgebra.Vector2;

public class Anchor implements PhysicsObject{

    public Vector2 position;

    public Anchor(Vector2 _position) {
        position = _position;
    }

    public void addPositionVector(Vector2 _vector) {
        position.x += _vector.x;
        position.y += _vector.y;
    }

    @Override
    public Vector2 getForce() {
        return null;
    }

    @Override
    public void setForce(Vector2 _force) {
    }

    @Override
    public Vector2 getAcceleration() {
        return null;
    }

    @Override
    public double getMass() {
        return 0;
    }

    @Override
    public void setAcceleration(Vector2 _acceleration) {

    }

    @Override
    public void addVelocityVector(Vector2 _velocity) {

    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public int getSize() {
        return 30;
    }

    @Override
    public void setPosition(Vector2 _position) {
        position = _position;
    }

    @Override
    public void resetPhysics() {
    }

    @Override
    public void addForceVector(Vector2 _force) {

    }

    @Override
    public Vector2 getVelocity() {
        return null;
    }
}
