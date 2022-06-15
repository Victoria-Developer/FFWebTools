package optimalRoutes;

public class DoublePoint {
    private final double x;
    private final double y;

    public DoublePoint(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Coordinate is: " +
                "x=" + x +
                ", y=" + y;
    }
}
