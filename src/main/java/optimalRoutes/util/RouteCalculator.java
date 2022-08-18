package optimalRoutes.util;

import optimalRoutes.DoublePoint;
import optimalRoutes.LOCATION;
import optimalRoutes.LogInfo;

import java.util.*;
import java.util.stream.Collectors;

public final class RouteCalculator {

   /* public static LinkedList<LogInfo> getSingleCheapestRoute(LOCATION location,
                                                             List<LogInfo> logInfos) {
        LogInfo logInfo = new LogInfo();//some cheapest aetheryte
        LinkedList<LogInfo> sortedList =
                new LinkedList<>(Collections.singletonList(logInfo));
        for (LogInfo log : logInfos) {

        }
        return sortedList;
    }*/

    public static List<LinkedList<LogInfo>> getShortestRoute(LOCATION location,
                                                             List<LogInfo> logInfos) {
        List<LinkedList<LogInfo>> sortedData = new ArrayList<>();
        List<LogInfo> copy = new ArrayList<>(logInfos);

        //creating teleports' logs
        List<LogInfo> teleports = new ArrayList<>();
        Arrays.stream(location.getAetherytes())
                .forEach(a -> teleports.add(new LogInfo(a.getName(),
                        a.getCoordinates(), true)));
        copy.addAll(teleports);

        //map with pais of logs nearest to each other
        Map<LogInfo, LogInfo> otherMap = new HashMap<>();
        logInfos.forEach(log -> {
            LogInfo pair = findNearestLog(copy, log).getKey();
            if (pair.isTeleport())
                sortedData.add(new LinkedList<>(List.of(pair, log)));
            else
                otherMap.put(log, pair);
        });

        //populating lists with aetheryte as start point
        sortedData.forEach(list -> {
            //starting from the log next to aetheryte
            LogInfo compareLog = list.get(1);
            LogInfo matchLog;
            List<LogInfo> match = new ArrayList<>(getMatch(otherMap, compareLog));
            while (!match.isEmpty()) {
                if (match.size() > 1)
                    matchLog = findNearestLog(match, compareLog).getKey();
                else
                    matchLog = match.get(0);
                list.add(list.indexOf(compareLog) + 1, matchLog);
                otherMap.remove(matchLog, compareLog);
                match.remove(matchLog);
                compareLog = matchLog;
                match.addAll(getMatch(otherMap, compareLog));
            }
        });

        //if after populating there are still pairs outside any list
        if (!otherMap.isEmpty()) {
            List<LogInfo> mapCopy = new ArrayList<>(new HashSet<>(otherMap.values()));

            while (!mapCopy.isEmpty()) {
                //for each end point or aetherite find nearest point
                List<LogInfo> listForMatch = new ArrayList<>(teleports);
                sortedData.forEach(list -> listForMatch.add(list.getLast()));

                LinkedList<LogInfo> listToAdd;
                Map.Entry<LogInfo, LogInfo> match = findNearestPair(listForMatch, mapCopy);
                LogInfo pairLog1 = match.getKey();
                LogInfo pairLog2 = match.getValue();
                if (pairLog1.isTeleport()) {
                    listToAdd = new LinkedList<>(List.of(pairLog1));
                    sortedData.add(listToAdd);
                } else listToAdd = getMatchedList(sortedData, pairLog1);

                listToAdd.add(pairLog2);
                mapCopy.remove(pairLog2);
                LogInfo pairLog = otherMap.get(pairLog2);
                if (!listToAdd.contains(pairLog)) {
                    listToAdd.add(pairLog);
                    mapCopy.remove(pairLog);
                }
            }
        }

        return sortedData;
    }

    private static LinkedList<LogInfo> getMatchedList(List<LinkedList<LogInfo>> commonList, LogInfo log) {
        return commonList.stream()
                .filter(list -> list.contains(log))
                .findFirst().orElse(null);
    }

    private static List<LogInfo> getMatch(Map<LogInfo, LogInfo> map,
                                          LogInfo value) {
        return map.entrySet().stream()
                .filter(entry -> entry.getValue() == value)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private static Map.Entry<LogInfo, LogInfo> findNearestPair(List<LogInfo> withTp,
                                                               List<LogInfo> withoutTp) {
        LogInfo pairLog1 = null;
        LogInfo pairLog2 = null;
        double minDistance = Double.MAX_VALUE;
        for (LogInfo log : withTp) {
            Map.Entry<LogInfo, Double> logDistance = findNearestLog(withoutTp, log);
            LogInfo currentNearestLog = logDistance.getKey();
            double currentDistance = logDistance.getValue();
            if (currentDistance < minDistance) {
                pairLog1 = log;
                pairLog2 = currentNearestLog;
                minDistance = currentDistance;
            }
        }
        return new AbstractMap.SimpleEntry<>(pairLog1, pairLog2);
    }

    private static Map.Entry<LogInfo, Double> findNearestLog(List<LogInfo> logInfos,
                                                             LogInfo comparablePoint) {
        LogInfo logWithMinPoint = null;
        double minValue = Double.MAX_VALUE;
        for (LogInfo logInfo : logInfos) {
            if (logInfo != comparablePoint) {
                double distance = getDistance(logInfo,
                        comparablePoint);
                if (distance < minValue) {
                    logWithMinPoint = logInfo;
                    minValue = distance;
                }
            }
        }
        return new AbstractMap.SimpleEntry<>(logWithMinPoint, minValue);
    }

    private static double getDistance(LogInfo l1, LogInfo l2) {
        DoublePoint p1 = l1.getDoublePoint();
        DoublePoint p2 = l2.getDoublePoint();
        return getDistance(p1, p2);
    }

    public static double getDistance(DoublePoint p1, DoublePoint p2) {
        return Math.sqrt(Math
                .pow((p1.getX() - p2.getX()), 2)
                + Math.
                pow((p1.getY() - p2.getY()), 2));
    }

}
