package app.controller;

import app.dto.Area;
import app.dto.Coordinate;
import app.repository.AreaRepository;
import app.service.JsonConverter;
import app.service.LogsParser;
import app.service.Tsp;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
public class PathFinderController {
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private JsonConverter jsonConverter;
    @Autowired
    private LogsParser logsParser;
    @Autowired
    private Tsp tsp;

    @GetMapping("/ff")
    public String home() {
        return "ff_about";
    }

    @GetMapping("/ff/optimalRoute")
    public String optimalRoute() {
        return "optimalRoute";
    }

    @PostMapping(value = "ff/optimalRoute/calculate", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String calculatePath(@RequestBody Map<String, List<Coordinate>> logs) throws JsonProcessingException {
        Map<Area, LinkedList<Coordinate>> orderedLogs = new HashMap<>();

        logs.forEach((areaName, coordinates) ->
            areaRepository.findByName(areaName).ifPresent(areaEntity -> {
                Area dto = jsonConverter.areaEntityToDto(areaEntity);
                    LinkedList<Coordinate> tspSolution = tsp.solve(dto.getTeleports(), coordinates);
                    orderedLogs.put(dto, tspSolution);
            })
        );

        return jsonConverter.tspSolutionToJson(orderedLogs);
    }

    @PostMapping(value = "/ff/optimalRoute/parse", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String parseLogs(@RequestBody Map<String, String> body) throws JsonProcessingException {
        Map<String, List<Coordinate>> logs = logsParser.parseLogs(body.get("inputLogs"));
        return jsonConverter.parsedLogsToJson(logs);
    }

}
