package webapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fcParsing.Character;
import fcParsing.*;
import optimalRoutes.Calculator;
import optimalRoutes.ChatLogParser;
import optimalRoutes.LOCATION;
import optimalRoutes.LogInfo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@SessionAttributes({"jobEnums", "parseEnums", "characters", "sortingMethods", "formData"})

public class App {
    Map<FreeCompany, FormData> fcForms = new HashMap<>();
    LinkedList<SORTING> sortings = new LinkedList<>(Arrays.asList(SORTING.values()));
    LinkedList<PARSE_KEY> parseKeys = new LinkedList<>(Arrays.asList(PARSE_KEY.values()));
    public static final int maxLvl = 90;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/contact")
    public String contacts() {
        return "contacts";
    }

    @GetMapping("/optimalRoute")
    public String optimalRoute() {
        return "optimalRoute";
    }

    @PostMapping(value = "/optimalRoute", params = {"inputLogs"},
            produces = {"application/json"})
    public @ResponseBody
    List<LogInfo>
    parseInput(@Validated @RequestParam(name = "inputLogs") String inputLogs) {
        return ChatLogParser.parseCoordinate(inputLogs);
    }

    @PostMapping(value = "/optimalRoute", produces = {"application/json"})
    public @ResponseBody
    String
    calculateRoute(@Validated @RequestParam(name = "editedLogs")
                           String editedCoordinates) throws JsonProcessingException {
        //sort by enums every time
        //remove all teleports
        ObjectMapper mapper = new ObjectMapper();
        List<LogInfo> editedCoordinatesList =
                mapper.readValue(editedCoordinates, new TypeReference<>() {
                });
        JSONArray json = new JSONArray();

        Map<LOCATION, LinkedList<LogInfo>> sortedMap =
                Calculator.sortByEnums(editedCoordinatesList);

        sortedMap.forEach((location, logInfos) -> {
            ClassLoader cl = this.getClass().getClassLoader();
            InputStream inputStream = cl.getResourceAsStream("static" + location.getFileName());
            BufferedImage map = null;
            try {
                map = ImageIO.read(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            LinkedList<LogInfo> copy = new LinkedList<>(logInfos);
            Calculator.checkSimilarPoints(copy);
            List<LinkedList<LogInfo>> sortedData = Calculator.getShortestRoute(location, copy);
            Calculator.calculateInGamePoints(map, sortedData);

            JSONObject o = new JSONObject()
                    .put("logs", JSONObject.valueToString(sortedData))
                    .put("imageSrc", location.getFileName());
            json.put(o);
        });

        return json.toString();
    }

    /*@PostMapping(value = "/optimalRoute", produces = {"application/json"})
    public @ResponseBody
    String
    calculateOptimalRoute(@Validated @RequestParam(name = "inputLogs") String inputLogs) {
        Map<LOCATION, LinkedList<LogInfo>> coordinatesByEnums =
                ChatLogParser.getLocationsMap(inputLogs);
        JSONArray json = new JSONArray();

        coordinatesByEnums.forEach((location, logInfos) -> {
            ClassLoader cl = this.getClass().getClassLoader();
            InputStream inputStream = cl.getResourceAsStream("static" + location.getFileName());
            BufferedImage map = null;
            try {
                map = ImageIO.read(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            LinkedList<LogInfo> copy = new LinkedList<>(logInfos);
            Calculator.checkSimilarPoints(copy);
            List<LinkedList<LogInfo>> sortedData = Calculator.getShortestRoute(location, copy);
            Calculator.calculateInGamePoints(map, sortedData);

            JSONObject o = new JSONObject()
                    .put("logs", JSONObject.valueToString(sortedData))
                    .put("imageSrc", location.getFileName());
            json.put(o);
        });

        return json.toString();
    }*/

    @GetMapping("/fcSearch")
    public String fcNameForm() {
        return "fcInput";
    }

    @ModelAttribute("sortingMethods")
    public LinkedList<SORTING> getSortMethods() {
        return sortings;
    }

    @ModelAttribute("parseEnums")
    public LinkedList<PARSE_KEY> getParseKeys() {
        return parseKeys;
    }

    @ModelAttribute("jobEnums")
    public JOB[] getJobs() {
        return JOB.values();
    }

    @PostMapping(value = "/fcMembers")
    public String fcSubmit(@ModelAttribute(name = "fcId") String fcId,
                           Model model) {
        FreeCompany freeCompany;
        try {
            freeCompany = HtmlParser.parseChosenFC(fcId);
        } catch (IOException e) {
            return "fcError";
        }
        FormData formData = new FormData();
        fcForms.put(freeCompany, formData);

        model.addAttribute("characters", freeCompany.getCharacters());
        model.addAttribute("formData", formData);

        return "fcMembers";
    }

    @RequestMapping(value = "/fcMembers", method = RequestMethod.GET, params = {"selectedMethod"})
    public @ResponseBody
    LinkedList<Character> sortCharactersList(@RequestParam(name = "selectedMethod") Integer sortId,
                                             @Validated @ModelAttribute(name = "characters")
                                                     LinkedList<Character> charactersList,
                                             @Validated @ModelAttribute(name = "formData")
                                                     FormData formData,
                                             Model model) {

        SORTING selectedSortingMethod = SORTING.values()[sortId];
        LinkedList<Character> sortedList = selectedSortingMethod
                .getListUnaryOperator().apply(charactersList);
        formData.setSorting(selectedSortingMethod);
        model.addAttribute("characters", sortedList);
        return sortedList;
    }

    @RequestMapping(value = "/fcMembers", method = RequestMethod.GET, params = {"parseId"})
    public @ResponseBody
    Map<String, Object> parseCharacters(@RequestParam("parseId") Integer parseId,
                                        @Validated @ModelAttribute(name = "characters")
                                                LinkedList<Character> charactersList,
                                        @Validated @ModelAttribute(name = "formData")
                                                FormData formData,
                                        Model model) {
        Map<String, Object> map = new HashMap<>();
        try {
            PARSE_KEY parseKey = PARSE_KEY.values()[parseId];
            formData.getParsedByKeys().replace(parseKey, true);
            HtmlParser.parseCharacters(charactersList, parseKey);
            SORTING relatedSorting = Arrays.stream(SORTING.values())
                    .filter(sorting -> sorting.getParseKey() == parseKey).findFirst().get();
            map.put("charactersList", JSONObject.valueToString(charactersList));
            map.put("relatedSortingId", sortings.indexOf(relatedSorting));
        } catch (Exception ignored) {
        }

        model.addAttribute("characters", charactersList);
        return map;
    }

}
