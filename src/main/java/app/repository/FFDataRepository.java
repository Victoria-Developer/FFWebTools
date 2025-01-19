package app.repository;


import app.dto.Area;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Repository
public class FFDataRepository {

    public Area findAreaByName(String areaName){
        return fetchAreasData().stream()
                .filter(a -> a.getName().equals(areaName))
                .findFirst()
                .orElse(null);
    }

    public List<Area> fetchAreasData() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream("json/areas.json");
            List<Area> areas = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
            areas.stream()
                    .flatMap(area -> Arrays.stream(area.getTeleports()))
                    .forEach(t -> t.setTeleport(true));
            return areas;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
