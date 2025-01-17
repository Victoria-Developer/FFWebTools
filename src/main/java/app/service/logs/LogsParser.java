package app.service.logs;

import app.dto.Area;
import app.dto.Coordinate;
import app.dto.Point;
import app.repository.FFDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class LogsParser {
    private static final String playerCordPattern = "([\uE090-\uE097])(.*)(\uE0BB)([a-zA-Z][a-zA-Z ]+)(.*)";
    @Autowired
    private FFDataRepository ffDataRepository;

    public Map<Area, LinkedList<Coordinate>> parseLogs(String input) {
        List<String> validSentences = findValidSentences(input);
        List<Area> areas = ffDataRepository.fetchAreasData();

        Map<Area, LinkedList<Coordinate>> groupedCoordinates = validSentences.stream()
                .map(sentence -> parseLog(sentence, areas))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                Map.Entry::getValue,
                                Collectors.toCollection(LinkedList::new)
                        )
                ));

        groupedCoordinates.forEach((area, coordinates) -> mergeSimilarCoordinates(coordinates));
        return groupedCoordinates;
    }

    private Map.Entry<Area, Coordinate> parseLog(String validSentence, List<Area> areas) {
        Pattern fullInfoPattern = Pattern.compile(playerCordPattern);
        Matcher fullInfoMatcher = fullInfoPattern.matcher(validSentence);

        if (fullInfoMatcher.find()) {
            // Parse player's coordinate
            String name = parsePlayerName(fullInfoMatcher.group(2)); // Player's name
            String coordinates = fullInfoMatcher.group(5); // Player's coordinates
            Point coordinate = parsePoint(coordinates);
            Coordinate playerCoordinate = new Coordinate(
                    name, coordinate, "x_mark.png", false
            );

            // Parse Area
            String areaName = fullInfoMatcher.group(4).trim();
            Area area = areas.stream()
                    .filter(a -> a.getName().equals(areaName))
                    .findFirst()
                    .orElse(null); // Location's name

            return area != null ? Map.entry(area, playerCoordinate) : null;
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

    private void mergeSimilarCoordinates(List<Coordinate> list) {
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
    }

    private boolean isSimilarPoint(Coordinate playerCord1, Coordinate playerCord2) {
        double margin = 1.5;

        Point point1 = playerCord1.getPoint();
        double x1 = point1.x();
        double y1 = point1.y();

        Point point2 = playerCord2.getPoint();
        double x2 = point2.x();
        double y2 = point2.y();

        return (x1 == x2 && y1 == y2) || (Math.abs(x1 - x2) < margin && Math.abs(y1 - y2) < margin);
    }

}
