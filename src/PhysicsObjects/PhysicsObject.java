package PhysicsObjects;

import LinearAlgebra.Vector2;

public interface PhysicsObject {
    Vector2 getPosition();

    void setPosition(Vector2 position);

    void resetPhysics();
}
