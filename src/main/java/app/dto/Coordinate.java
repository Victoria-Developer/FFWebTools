package app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Coordinate {
    String name;
    Point point;
    String filePath;
    boolean isTeleport;

    public Coordinate(){}

    public Coordinate(String name, Point point, String filePath, boolean isTeleport) {
        this.name = name;
        this.point = point;
        this.filePath = filePath;
        this.isTeleport = isTeleport;
    }

    public Coordinate(String name, Point point) {
        this.name = name;
        this.point = point;
    }

    public void setName(String playerName) {
        this.name = playerName;
    }

    public String getName() {
        return name;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    @JsonProperty("point")
    public Point getPoint() {
        return point;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setTeleport(boolean teleport) {
        isTeleport = teleport;
    }

    public boolean isTeleport() {
        return isTeleport;
    }

    @Override
    public String toString() {
        return name + " " + point.toString();
    }
}
