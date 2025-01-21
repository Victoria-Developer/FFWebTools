package app.controller;

import app.dto.Area;
import app.dto.Coordinate;
import app.repository.FFDataRepository;
import app.service.logs.LogsParser;
import app.service.tsp.Tsp;
import app.util.JsonConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
public class PathFinderController {
    @Autowired
    private FFDataRepository ffDataRepository;
    @Autowired
    private LogsParser logsParser;
    @Autowired
    private Tsp tsp;

    @GetMapping("/optimalRoute")
    public String optimalRoute() {
        return "optimalRoute";
    }

    @PostMapping(value = "/optimalRoute/calculate")
    public @ResponseBody String calculatePath(
            @RequestParam(name = "logs") String logs
    ) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, List<Coordinate>> parsedLogs = objectMapper.readValue(logs, new TypeReference<>() {
        });

        Map<Area, LinkedList<Coordinate>> orderedLogs = new HashMap<>();
        parsedLogs.forEach((areaName, coordinates) -> {
            Area area = ffDataRepository.findAreaByName(areaName);
            orderedLogs.put(area, tsp.solve(area.getTeleports(), coordinates));
        });

        JSONArray json = new JSONArray();
        for (Map.Entry<Area, LinkedList<Coordinate>> entry : orderedLogs.entrySet()) {
            Area area = entry.getKey();
            JSONObject o = JsonConverter.mapEntryToJsonObject(
                area.getName(), area.getFileName(), entry.getValue()
            );
            json.put(o);
        }
        return json.toString();
    }

    @PostMapping(value = "/optimalRoute/parse", params = {"inputLogs"}, produces = {"application/json"})
    public @ResponseBody String parseLogs(
            @Validated @RequestParam(name = "inputLogs") String logs
    ) throws JsonProcessingException {
        Map<String, List<Coordinate>> parsedLogs = logsParser.parseLogs(logs);

        JSONArray json = new JSONArray();
        for (Map.Entry<String, List<Coordinate>> entry : parsedLogs.entrySet()) {
            JSONObject o = JsonConverter.mapEntryToJsonObject(
                entry.getKey(), null, entry.getValue()
            );
            json.put(o);
        }
        return json.toString();
    }

}
