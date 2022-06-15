package optimalRoutes;

public class LogInfo {
    private DoublePoint doublePoint;
    private DoublePoint scaledGamePoint;
    private String name;
    private LOCATION location;
    private boolean isTeleport;

    public LogInfo() {
    }

    public LogInfo(String name,
                   DoublePoint doublePoint,
                   boolean isTeleport) {
        this.name = name;
        this.doublePoint = doublePoint;
        this.isTeleport = isTeleport;
    }

    public void setDoublePoint(DoublePoint doublePoint) {
        this.doublePoint = doublePoint;
    }

    public DoublePoint getDoublePoint() {
        return doublePoint;
    }

    public void setScaledGamePoint(DoublePoint scaledGamePoint) {
        this.scaledGamePoint = scaledGamePoint;
    }

    public DoublePoint getScaledGamePoint() {
        return scaledGamePoint;
    }

    public void setName(String playerName) {
        this.name = playerName;
    }

    public String getName() {
        return name;
    }

    public void setLocation(LOCATION location) {
        this.location = location;
    }

    public LOCATION getLocation() {
        return location;
    }

    public void setTeleport(boolean teleport) {
        isTeleport = teleport;
    }

    public boolean isTeleport() {
        return isTeleport;
    }
}
