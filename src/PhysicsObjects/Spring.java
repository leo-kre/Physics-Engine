package PhysicsObjects;

import LinearAlgebra.Vector2;

public class Spring {

    public double springConstant;
    public double restLength;

    public PhysicsObject pointA;
    public PhysicsObject pointB;

    public Spring(double _springConstant, PhysicsObject _pointA, PhysicsObject _pointB) {
        springConstant = _springConstant;
        pointA = _pointA;
        pointB = _pointB;

        restLength = _pointA.getPosition().distanceTo(_pointB.getPosition());
    }
}
