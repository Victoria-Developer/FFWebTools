package app.controller;

import app.dto.Area;
import app.dto.Coordinate;
import app.dto.TspResponse;
import app.repository.AreaRepository;
import app.service.Converter;
import app.service.LogsParser;
import app.service.Tsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<TspResponse>> calculatePath(@RequestBody Map<String, List<Coordinate>> body) {
        // No logs found in the request body
        List<Coordinate> logs = body.get("logs");
        if (logs == null || logs.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Group logs by area name
        Map<String, List<Coordinate>> groupedLogs = logs.stream()
                .collect(Collectors.groupingBy(Coordinate::getAreaName));

        List<TspResponse> orderedLogs = new ArrayList<>();

        // Solve TSP for each area
        groupedLogs.forEach((areaName, coordinates) ->
            areaRepository.findByName(areaName).ifPresent(areaEntity -> {
                Area dto = converter.areaEntityToDto(areaEntity);
                LinkedList<Coordinate> tspSolution = tsp.solve(dto.getTeleports(), coordinates);
                orderedLogs.add(new TspResponse(dto, tspSolution));
            })
        );

        HttpStatus status = orderedLogs.isEmpty()? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.OK;
        return ResponseEntity
                .status(status)
                .body(orderedLogs);
    }

    @PostMapping(value = "route/parse", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<Coordinate>> parseLogs(@RequestBody Map<String, String> body) {
        // In-game chat logs input
        String userInput = body.get("userInput");
        if (userInput == null || userInput.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Parse logs to get only map coordinates
        List<Coordinate> coordinates = logsParser.parseLogs(userInput);
        HttpStatus status = coordinates.isEmpty()? HttpStatus.BAD_REQUEST : HttpStatus.OK;
        return ResponseEntity
                .status(status)
                .body(coordinates);
    }

}
