package app.service.logs;

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

    private record Point(double x, double y) {}

    public Map<String, List<Coordinate>> parseLogs(String input) {
        List<String> validSentences = findValidSentences(input);

        Map<String, List<Coordinate>> logs = validSentences.stream()
                .map(this::parseLog)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Coordinate::getAreaName));
        logs.replaceAll((name, coordinates) -> mergeSimilarCoordinates(coordinates));
        return logs;
    }

    private Coordinate parseLog(String validSentence) {
        Pattern fullInfoPattern = Pattern.compile(playerCordPattern);
        Matcher fullInfoMatcher = fullInfoPattern.matcher(validSentence);

        if (fullInfoMatcher.find()) {
            Coordinate playerCoordinate = new Coordinate();

            String name = parsePlayerName(fullInfoMatcher.group(2)); // Player's name
            playerCoordinate.setName(name);

            Point point  = parsePoint(fullInfoMatcher.group(5)); // Player's coordinates
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

    private List<Coordinate> mergeSimilarCoordinates(List<Coordinate> list) {
        for (int i = 0; i < list.size(); i++) {
            Coordinate current = list.get(i);

            for (int j = i + 1; j < list.size(); j++) {
                Coordinate other = list.get(j);

                if (isSimilarPoint(current, other)) {
                    if (!current.getName().equals(other.getName()))
                        current.setName(current.getName() + " & " + other.getName());
                    list.remove(j);
                    j--;
                }
            }
        }
        return list;
    }

    private boolean isSimilarPoint(Coordinate coordinate1, Coordinate coordinate2) {
        double margin = 1.5;

        double x1 = coordinate1.getX();
        double y1 =  coordinate2.getY();

        double x2 = coordinate2.getX();
        double y2 = coordinate2.getY();

        return (x1 == x2 && y1 == y2) || (Math.abs(x1 - x2) < margin && Math.abs(y1 - y2) < margin);
    }

}
