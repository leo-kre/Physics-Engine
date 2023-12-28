package PhysicsObjects;

import LinearAlgebra.Vector2;

public interface PhysicsObject {
    Vector2 getPosition();

    int getSize();

    void setPosition(Vector2 _position);

    void resetPhysics();
}
