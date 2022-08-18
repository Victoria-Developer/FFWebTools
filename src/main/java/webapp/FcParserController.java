package webapp;

import fcParsing.*;
import fcParsing.Character;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Controller
@SessionAttributes({"parseEnums", "characters", "sortingMethods"})
public class FcParserController {
    LinkedList<SORTING> sortings = new LinkedList<>(Arrays.asList(SORTING.values()));
    LinkedList<PARSE_KEY> parseKeys = new LinkedList<>(Arrays.asList(PARSE_KEY.values()));
    public static final int maxLvl = 90;

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

    @PostMapping(value = "/fcMembers")
    public String fcSubmit(@ModelAttribute(name = "fcId") String fcId,
                           Model model) throws IOException {
        FreeCompany freeCompany = HtmlParser.parseChosenFC(fcId);
        model.addAttribute("characters", freeCompany.getCharacters());
        return "fcMembers";
    }

    @RequestMapping(value = "/fcMembers", method = RequestMethod.GET, params = {"selectedMethod"})
    public @ResponseBody
    LinkedList<fcParsing.Character> sortCharactersList(@RequestParam(name = "selectedMethod") Integer sortId,
                                                       @Validated @ModelAttribute(name = "characters")
                                                               LinkedList<fcParsing.Character> charactersList,
                                                       Model model) {

        SORTING selectedSortingMethod = SORTING.values()[sortId];
        LinkedList<fcParsing.Character> sortedList = selectedSortingMethod
                .getListUnaryOperator().apply(charactersList);
        model.addAttribute("characters", sortedList);
        return sortedList;
    }

    @RequestMapping(value = "/fcMembers", method = RequestMethod.GET, params = {"parseId"})
    public @ResponseBody
    Map<String, Object> parseCharacters(@RequestParam("parseId") Integer parseId,
                                        @Validated @ModelAttribute(name = "characters")
                                                LinkedList<Character> charactersList,
                                        Model model) {
        Map<String, Object> map = new HashMap<>();
        try {
            PARSE_KEY parseKey = PARSE_KEY.values()[parseId];
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
