package app.dto;

public class Coordinate {
    String name;
    String areaName;
    double x;
    double y;
    boolean isTeleport;

    public Coordinate(){}

    public Coordinate(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public void setName(String playerName) {
        this.name = playerName;
    }

    public String getName() {
        return name;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public void setTeleport(boolean teleport) {
        isTeleport = teleport;
    }

    public boolean isTeleport() {
        return isTeleport;
    }

    @Override
    public String toString() {
        return name + " " + x + ", " + y;
    }

}
