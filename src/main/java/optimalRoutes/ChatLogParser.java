package optimalRoutes;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ChatLogParser {

    /*public static Map<LOCATION, LinkedList<LogInfo>> getLocationsMap(String s) {
        ArrayList<LogInfo> commonList = ChatLogParser.parseCoordinate(s); //get common list

        Map<LOCATION, LinkedList<LogInfo>> coordinatesByEnums = new HashMap<>();
        Arrays.stream(LOCATION.values()).forEach(location -> { //sort by location enum
            LinkedList<LogInfo> logs = commonList.stream()
                    .filter(logInfo -> logInfo.getLocation() == location)
                    .collect(Collectors.filtering(Objects::nonNull,
                            Collectors.toCollection(LinkedList::new)));
            if(logs != null && !logs.isEmpty())
                coordinatesByEnums.put(location, logs);
        });
        return coordinatesByEnums;
    }*/

    public static ArrayList<LogInfo> parseCoordinate(String input) {
        ArrayList<String> results = findValidSentences(input);
        ArrayList<LogInfo> logsInfo = new ArrayList<>();
        results.forEach(result -> {
            LogInfo log = parseLogInfo(result);
            if(log.getLocation()!= null) logsInfo.add(log);
        });
        return logsInfo;
    }

    private static LogInfo parseLogInfo(String validSentence) {
        Pattern fullInfoPattern =
                Pattern.compile("([\uE090-\uE097])(.*)(\uE0BB)([a-zA-Z][a-zA-Z ]+)(.*)");
        Matcher fullInfoMatcher = fullInfoPattern.matcher(validSentence);
        LogInfo logInfo = new LogInfo();
        while (fullInfoMatcher.find()) {
            logInfo.setName(parsePlayerName(fullInfoMatcher.group(2)));//player's name
            logInfo.setLocation(LOCATION.getByKey(fullInfoMatcher.group(4).trim()));//location's name
            String coordinates = fullInfoMatcher.group(5);//coordinates
            logInfo.setTeleport(false);
            logInfo.setDoublePoint(parsePoint(coordinates));
        }
        return logInfo;
    }

    private static String parsePlayerName(String nameSource) {
        Pattern namePattern = Pattern.compile("^[a-zA-Z -']+(?!\\)$)");
        Matcher nameMatcher = namePattern.matcher(nameSource);
        String name = null;
        while (nameMatcher.find()) {
            name = nameMatcher.group();
        }
        return name;
    }

    private static DoublePoint parsePoint(String pointSource) {
        Pattern coordinatesPattern = Pattern.compile("(\\d*\\.?\\d+)");
        Matcher coordinatesMatcher = coordinatesPattern.matcher(pointSource);

        Double[] coordinatesS = coordinatesMatcher
                .results()
                .map(r -> Double.parseDouble(r.group()))
                .toArray(Double[]::new);
        return new DoublePoint(
                coordinatesS[0],//x
                coordinatesS[1]);//y
    }

    private static ArrayList<String> findValidSentences(String inputSource) {
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
