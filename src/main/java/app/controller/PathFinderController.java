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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.LinkedList;
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

    @GetMapping("/optimalRoute")
    public String optimalRoute() {
        return "optimalRoute";
    }

    @PostMapping(value = "/optimalRoute/calculate")
    public @ResponseBody String calculatePath(
            @RequestParam(name = "logs") String logs
    ) throws JsonProcessingException {

        Map<Area, LinkedList<Coordinate>> orderedLogs = new HashMap<>();
        jsonConverter.jsonToLogs(logs).forEach((areaName, coordinates) ->
                areaRepository.findByName(areaName).ifPresent(areaEntity -> {
                    Area dto = jsonConverter.areaEntityToDto(areaEntity);
                    LinkedList<Coordinate> tspSolution = tsp.solve(dto.getTeleports(), coordinates);
                    orderedLogs.put(dto, tspSolution);
                })
        );

        return jsonConverter.tspSolutionToJson(orderedLogs);
    }

    @PostMapping(value = "/optimalRoute/parse", params = {"inputLogs"}, produces = {"application/json"})
    public @ResponseBody String parseLogs(
            @Validated @RequestParam(name = "inputLogs") String logs
    ) throws JsonProcessingException {
        return jsonConverter.parsedLogsToJson(logsParser.parseLogs(logs));
    }

}
