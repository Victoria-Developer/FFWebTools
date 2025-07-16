package app.service;

import app.dto.Area;
import app.dto.Coordinate;
import app.entities.AreaEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class JsonConverter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Area areaEntityToDto(AreaEntity areaEntity){
        Area dto = new Area();
        dto.setName(areaEntity.getName());
        try {
            dto.setTeleports(objectMapper.readValue(areaEntity.getTeleports(), Coordinate[].class));
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
        dto.setFileName(areaEntity.getFileName());
        return dto;
    }

    public Map<String, List<Coordinate>> s(String logs) throws JsonProcessingException {
        return objectMapper.readValue(logs, new TypeReference<>() {
        });
    }

    public String tspSolutionToJson(Map<Area, LinkedList<Coordinate>> orderedLogs) throws JsonProcessingException {
        JSONArray json = new JSONArray();
        for (Map.Entry<Area, LinkedList<Coordinate>> entry : orderedLogs.entrySet()) {
            Area area = entry.getKey();
            JSONObject o = app.util.JsonConverter.mapEntryToJsonObject(
                    area.getName(), area.getFileName(), entry.getValue()
            );
            json.put(o);
        }
        return json.toString();
    }

    public String parsedLogsToJson(Map<String, List<Coordinate>> parsedLogs) throws JsonProcessingException {
        JSONArray json = new JSONArray();
        for (Map.Entry<String, List<Coordinate>> entry : parsedLogs.entrySet()) {
            JSONObject o = app.util.JsonConverter.mapEntryToJsonObject(
                    entry.getKey(), null, entry.getValue()
            );
            json.put(o);
        }
        return json.toString();
    }

}
