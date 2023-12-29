package PhysicsObjects;

public class Rod {

    public PhysicsObject pointA;
    public PhysicsObject pointB;

    public double distance;

    public Rod(PhysicsObject _pointA, PhysicsObject _pointB) {
        pointA = _pointA;
        pointB = _pointB;
        distance = _pointA.getPosition().distanceTo(_pointB.getPosition());
    }
}
