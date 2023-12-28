package LinearAlgebra;

public class Vector2 {
    public double x;
    public double y;

    public Vector2(double _x, double _y) {
        x = _x;
        y = _y;
    }

    public double distanceTo(Vector2 other) {
        double dx = other.x - this.x;
        double dy = other.y - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

}