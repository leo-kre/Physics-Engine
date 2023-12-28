package PhysicsObjects;

import LinearAlgebra.Vector2;

public class Anchor implements PhysicsObject{

    public Vector2 position;
    public int size;

    public Anchor(Vector2 _position, int _size) {
        position = _position;
        size = _size;
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
        //nothing
    }
}
