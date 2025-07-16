package app.dto;

public class Area {
    String name;
    Coordinate[] teleports;
    String fileName;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTeleports(Coordinate[] teleports) {
        this.teleports = teleports;
    }

    public Coordinate[] getTeleports() {
        return teleports;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
