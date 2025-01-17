package app.util;

import app.dto.Area;
import app.dto.Coordinate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Map;

public class JsonConverter {

    public static String fromMapToJson(Map<Area, LinkedList<Coordinate>> logs) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JSONArray json = new JSONArray();
        for (Map.Entry<Area, LinkedList<Coordinate>> entry : logs.entrySet()) {
            JSONObject o = new JSONObject()
                    .put("logs", mapper.writeValueAsString(entry.getValue()))
                    .put("area", mapper.writeValueAsString(entry.getKey()));
            json.put(o);
        }
        return json.toString();
    }

    public static Map<Area, LinkedList<Coordinate>> fromJsonToMap(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addKeyDeserializer(Area.class, new AreaKeyDeserializer());
        objectMapper.registerModule(module);
        return objectMapper.readValue(json, new TypeReference<>() {
        });
    }

}
