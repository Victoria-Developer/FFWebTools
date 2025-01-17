package app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Area {
    String name;
    Coordinate[] teleports;
    String fileName;

    public Area(){}

    public Area(String name, Coordinate[] teleports, String fileName) {
        this.name = name;
        this.teleports = teleports;
        this.fileName = fileName;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("teleports")
    public Coordinate[] getTeleports() {
        return teleports;
    }

    @JsonProperty("fileName")
    public String getFileName() {
        return fileName;
    }
}
