package app.dto;

public class Area {
    String name;
    Coordinate[] teleports;
    String imgSrc;

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

    public void setImgSrc(String fileName) {
        this.imgSrc = fileName;
    }

    public String getImgSrc() {
        return imgSrc;
    }
}
