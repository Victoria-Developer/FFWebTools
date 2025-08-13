package app.controller;

import app.dto.Area;
import app.dto.Coordinate;
import app.dto.TspResponse;
import app.repository.AreaRepository;
import app.service.Converter;
import app.service.LogsParser;
import app.service.Tsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class PathFinderController {
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private Converter converter;
    @Autowired
    private LogsParser logsParser;
    @Autowired
    private Tsp tsp;

    @PostMapping(value = "route/calculate", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public List<TspResponse> calculatePath(@RequestBody Map<String, List<Coordinate>> body) {
        List<TspResponse> orderedLogs = new ArrayList<>();

        Map<String, List<Coordinate>> groupedLogs 
            = body.get("logs").stream().collect(Collectors.groupingBy(Coordinate::getAreaName));

        groupedLogs.forEach((areaName, coordinates) ->
            areaRepository.findByName(areaName).ifPresent(areaEntity -> {
                Area dto = converter.areaEntityToDto(areaEntity);
                    LinkedList<Coordinate> tspSolution = tsp.solve(dto.getTeleports(), coordinates);
                    orderedLogs.add(new TspResponse(dto, tspSolution));
            })
        );
        return orderedLogs;
    }

    @PostMapping(value = "route/parse", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public List<Coordinate> parseLogs(@RequestBody Map<String, String> body) {
        return logsParser.parseLogs(body.get("userInput"));
    }

}
