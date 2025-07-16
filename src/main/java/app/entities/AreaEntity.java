package app.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "areas")
public class AreaEntity {
    @Id
    private Integer id;
    private String name;
    private String teleports;
    private String fileName;

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
