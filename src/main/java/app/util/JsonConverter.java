package app.util;

import app.dto.Coordinate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.util.List;

public class JsonConverter {

    public static JSONObject mapEntryToJsonObject(
            String areaName, String areaFileName, List<Coordinate> logs
    ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JSONObject o = new JSONObject()
                .put("logs", mapper.writeValueAsString(logs))
                .put("areaName", areaName);
        if (areaFileName != null) o.put("areaFileName", areaFileName);
        return o;
    }

}
