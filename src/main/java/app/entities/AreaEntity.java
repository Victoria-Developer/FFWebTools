package app.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "areas")
public class AreaEntity {
    @Id
    private Integer id;
    private String name;
    private String teleports;
    private String fileName;

    public AreaEntity() {}

    public AreaEntity(Integer id, String name, String teleports, String fileName) {
        this.id = id;
        this.name = name;
        this.teleports = teleports;
        this.fileName = fileName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTeleports(String teleports) {
        this.teleports = teleports;
    }

    public String getTeleports() {
        return teleports;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
