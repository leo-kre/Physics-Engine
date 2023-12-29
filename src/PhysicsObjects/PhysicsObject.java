package PhysicsObjects;

import LinearAlgebra.Vector2;

public interface PhysicsObject {
    Vector2 getPosition();

    int getSize();

    void setPosition(Vector2 _position);

    void resetPhysics();

    void addForceVector(Vector2 _force);

    Vector2 getVelocity();

    void addPositionVector(Vector2 _position);

    Vector2 getForce();

    void setForce(Vector2 _force);

    Vector2 getAcceleration();

    double getMass();

    void setAcceleration(Vector2 _acceleration);

    void addVelocityVector(Vector2 _velocity);
}
