package app.service;

import app.dto.Area;
import app.dto.Coordinate;
import app.entities.AreaEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JsonConverter {

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
        dto.setFileName(areaEntity.getFileName());
        return dto;
    }

    public Map<String, List<Coordinate>> jsonToLogs(String logs) throws JsonProcessingException {
        return objectMapper.readValue(logs, new TypeReference<>() {});
    }

    public String tspSolutionToJson(Map<Area, LinkedList<Coordinate>> orderedLogs) throws JsonProcessingException {
        List<Object> result = new ArrayList<>();
        for (Map.Entry<Area, LinkedList<Coordinate>> entry : orderedLogs.entrySet()) {
            Map<String, Object> obj = new LinkedHashMap<>();
            obj.put("areaName", entry.getKey().getName());
            obj.put("areaFileName", entry.getKey().getFileName());
            obj.put("logs", entry.getValue());
            result.add(obj);
        }
        return objectMapper.writeValueAsString(result);
    }

    public String parsedLogsToJson(Map<String, List<Coordinate>> parsedLogs) throws JsonProcessingException {
        List<Object> result = new ArrayList<>();
        for (Map.Entry<String, List<Coordinate>> entry : parsedLogs.entrySet()) {
            Map<String, Object> obj = new LinkedHashMap<>();
            obj.put("areaName", entry.getKey());
            obj.put("logs", entry.getValue());
            result.add(obj);
        }
        return objectMapper.writeValueAsString(result);
    }
}
