package app.controller;

import app.dto.Area;
import app.dto.Coordinate;
import app.service.logs.LogsParser;
import app.service.tsp.Tsp;
import app.util.JsonConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class PathFinderController {
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
        Map<Area, LinkedList<Coordinate>> parsedLogs = JsonConverter.fromJsonToMap(logs);
        Map<Area, LinkedList<Coordinate>> orderedLogs = parsedLogs.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> tsp.solve(entry.getKey().getTeleports(), entry.getValue())
                ));

        return JsonConverter.fromMapToJson(orderedLogs);
    }

    @PostMapping(value = "/optimalRoute/parse", params = {"inputLogs"}, produces = {"application/json"})
    public @ResponseBody String parseLogs(
            @Validated @RequestParam(name = "inputLogs") String logs
    ) throws JsonProcessingException {
        return JsonConverter.fromMapToJson(logsParser.parseLogs(logs));
    }

}
