package app.service;

import app.dto.Coordinate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class LogsParser {
    private static final String playerCordPattern = "([\uE090-\uE097])(.*)(\uE0BB)([a-zA-Z][a-zA-Z ]+)(.*)";

    private record Point(double x, double y) {
    }

    public Map<String, List<Coordinate>> parseLogs(String input) {
        return findValidSentences(input)
                .stream()
                .map(this::parseLog)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Coordinate::getAreaName));
    }

    private Coordinate parseLog(String validSentence) {
        Pattern fullInfoPattern = Pattern.compile(playerCordPattern);
        Matcher fullInfoMatcher = fullInfoPattern.matcher(validSentence);

        if (fullInfoMatcher.find()) {
            Coordinate playerCoordinate = new Coordinate();

            String name = parsePlayerName(fullInfoMatcher.group(2)); // Player's name
            playerCoordinate.setName(name);

            Point point = parsePoint(fullInfoMatcher.group(5)); // Player's coordinates
            playerCoordinate.setX(point.x);
            playerCoordinate.setY(point.y);

            String areaName = fullInfoMatcher.group(4).trim();
            playerCoordinate.setAreaName(areaName);

            return playerCoordinate;
        }
        return null;
    }

    private String parsePlayerName(String nameSource) {
        Pattern namePattern = Pattern.compile("^[a-zA-Z -']+(?!\\)$)");
        Matcher nameMatcher = namePattern.matcher(nameSource);
        String name = null;
        while (nameMatcher.find()) {
            name = nameMatcher.group();
        }
        return name;
    }

    private Point parsePoint(String pointSource) {
        Pattern coordinatesPattern = Pattern.compile("(\\d*\\.?\\d+)");
        Matcher coordinatesMatcher = coordinatesPattern.matcher(pointSource);

        Double[] coordinatesS = coordinatesMatcher
                .results()
                .map(r -> Double.parseDouble(r.group()))
                .toArray(Double[]::new);
        return new Point(
                coordinatesS[0], // x
                coordinatesS[1]); // y
    }

    private List<String> findValidSentences(String inputSource) {
        String[] sSentence = inputSource.split("(?=\\([\uE090-\uE097])");
        Pattern validLogsPattern = Pattern.compile("\\([\uE090-\uE097].*\uE0BB.*(.*)\\)");

        ArrayList<String> validSentences = new ArrayList<>();
        for (String s : sSentence) {
            Matcher validLogsMatcher = validLogsPattern.matcher(s);
            while (validLogsMatcher.find()) {
                validSentences.add(validLogsMatcher.group());
            }

        }
        return validSentences;
    }

}
