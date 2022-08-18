package webapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import optimalRoutes.util.Coordinations;
import optimalRoutes.util.RouteCalculator;
import optimalRoutes.ChatLogParser;
import optimalRoutes.LOCATION;
import optimalRoutes.LogInfo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
public class MapRouteController {

    @GetMapping("/optimalRoute")
    public String optimalRoute() {
        return "optimalRoute";
    }

    @PostMapping(value = "/optimalRoute", params = {"logs"},
            produces = {"application/json"})
    public @ResponseBody
    String calculatePath(@Validated @RequestParam(name = "logs") String changedLogs)
            throws JsonProcessingException {
        return calculateRouteAndConvert(ChatLogParser.parseCoordinates(changedLogs));
    }

    @PostMapping(value = "/optimalRoute", params = {"inputLogs"},
            produces = {"application/json"})
    public @ResponseBody
    List<LogInfo>
    parseInput(@Validated @RequestParam(name = "inputLogs") String inputLogs) {
        return ChatLogParser.parseCoordinates(inputLogs);
    }

    @PostMapping(value = "/optimalRoute", params = {"editedLogs"},
            produces = {"application/json"})
    public @ResponseBody
    String
    adjustRoute(@Validated @RequestParam(name = "editedLogs")
                        String editedCoordinates) throws JsonProcessingException {
        return calculateRouteAndConvert(convertStringToList(editedCoordinates));
    }

    private List<LogInfo> convertStringToList(String editedCoordinates) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(editedCoordinates, new TypeReference<>() {
        });
    }

    private Map<LOCATION, List<LogInfo>> convertToLocationsMap(List<LogInfo> coordinatesList) {
        Map<LOCATION, List<LogInfo>> map = new HashMap<>();
        coordinatesList.forEach(parsedLog -> {
            LOCATION location = parsedLog.getLocation();
            if (location != null) {
                if (map.containsKey(location))
                    map.get(location).add(parsedLog);
                else
                    map.put(location, new LinkedList<>(List.of(parsedLog)));
            }
        });
        return map;
    }

    private String calculateRouteAndConvert(List<LogInfo> editedCoordinatesList) {
        JSONArray json = new JSONArray();

        convertToLocationsMap(editedCoordinatesList)
                .forEach((location, logInfos) -> {
            ClassLoader cl = this.getClass().getClassLoader();
            InputStream inputStream = cl.getResourceAsStream("static" + location.getFileName());
            BufferedImage map = null;
            try {
                map = ImageIO.read(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            LinkedList<LogInfo> copy = new LinkedList<>(logInfos);
            Coordinations.checkSimilarPoints(copy);
            List<LinkedList<LogInfo>> sortedData = RouteCalculator.getShortestRoute(location, copy);
            Coordinations.calculateInGamePoints(map, sortedData);

            JSONObject o = new JSONObject()
                    .put("logs", JSONObject.valueToString(sortedData))
                    .put("imageSrc", location.getFileName());
            json.put(o);
        });

        return json.toString();
    }
}
