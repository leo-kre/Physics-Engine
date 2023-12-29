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

        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }

    public Vector2 subtract(Vector2 other) {
        return new Vector2(this.x - other.x, this.y - other.y);
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2 normalize() {
        double mag = magnitude();
        return new Vector2(x / mag, y / mag);
    }

    public Vector2 multiply(double scalar) {
        return new Vector2(x * scalar, y * scalar);
    }

    public Vector2 add(Vector2 other) {
        return new Vector2(this.x + other.x, this.y + other.y);
    }

}