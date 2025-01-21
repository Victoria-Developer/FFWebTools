package app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Area {
    String name;
    Coordinate[] teleports;
    String fileName;

    public Area(){}

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setTeleports(Coordinate[] teleports) {
        this.teleports = teleports;
    }

    @JsonProperty("teleports")
    public Coordinate[] getTeleports() {
        return teleports;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @JsonProperty("fileName")
    public String getFileName() {
        return fileName;
    }
}
