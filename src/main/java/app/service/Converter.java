package app.service;

import app.dto.Area;
import app.dto.Coordinate;
import app.entities.AreaEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Converter {

    @Autowired
    private ObjectMapper objectMapper;

    public Area areaEntityToDto(AreaEntity areaEntity) {
        Area dto = new Area();
        dto.setName(areaEntity.getName());
        try {
            Coordinate[] teleports = objectMapper.readValue(areaEntity.getTeleports(), Coordinate[].class);
            for (Coordinate coordinates : teleports) {
                coordinates.setTeleport(true);
            }
            dto.setTeleports(teleports);
        } catch (JsonProcessingException ignored) {}
        dto.setImgSrc(areaEntity.getFileName());
        return dto;
    }
}
