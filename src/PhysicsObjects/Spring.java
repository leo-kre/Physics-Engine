package PhysicsObjects;

import LinearAlgebra.Vector2;

public class Spring {

    public double springConstant;

    public Vector2 pointA;
    public Vector2 pointB;

    public Spring(double _springConstant, Vector2 _pointA, Vector2 _pointB) {
        springConstant = _springConstant;
        pointA = _pointA;
        pointB = _pointB;
    }
}
