package optimalRoutes;

import java.awt.image.BufferedImage;
import java.util.*;

public class Calculator {

    private static boolean isSimilarPoints(LogInfo log1, LogInfo log2) {
        if (log1 == log2) return false;
        DoublePoint doublePoint1 = log1.getDoublePoint();
        DoublePoint doublePoint2 = log2.getDoublePoint();
        double x1 = doublePoint1.getX();
        double x2 = doublePoint2.getX();
        double y1 = doublePoint1.getY();
        double y2 = doublePoint2.getY();
        double margin = 1.5;

        boolean isSimilar = false;
        boolean isSame = x1 == x2 && y1 == y2;
        boolean isClosePoint = Math.abs(x1 - x2) < margin
                && Math.abs(y1 - y2) < margin;
        if (isSame || isClosePoint) isSimilar = true;
        return isSimilar;
    }

    public static void checkSimilarPoints(List<LogInfo> list) {
        ArrayList<LogInfo> toRemove = new ArrayList<>(); //to ignore and to remove
        list.forEach(checkableLog -> {
            boolean isAllowed = !toRemove.contains(checkableLog); //no value of ignore list
            if (isAllowed) {
                list.forEach(nextLog -> {
                    if (isSimilarPoints(checkableLog, nextLog)) { //if logs are similar
                        toRemove.add(nextLog);
                        checkableLog.setName(checkableLog.getName()
                                + " and " + nextLog.getName()); // add name of similar log
                    }
                });
            }
        });
        list.removeAll(toRemove);
    }

    private static DoublePoint calculateInGamePoint(BufferedImage map, DoublePoint point) {
        double differenceInStartPoint = 1;
        double gameX = point.getX() - differenceInStartPoint;
        double gameY = point.getY() - differenceInStartPoint;

        int imageHeight = map.getHeight();
        int imageWidth = map.getWidth();

        double cellNumber = 41;

        double cellHeight = imageHeight / cellNumber;
        double cellWidth = imageWidth / cellNumber;

        double resultY = cellHeight * gameY;
        double resultX = cellWidth * gameX;

        return new DoublePoint(resultX, resultY);
    }

    public static void calculateInGamePoints(BufferedImage image,
                                             List<LinkedList<LogInfo>> logInfos) {
        logInfos.forEach(list -> list.forEach(log ->
                log.setScaledGamePoint(calculateInGamePoint(image, log.getDoublePoint()))));
    }

    public static LinkedList<LogInfo> getSingleCheapestRoute(LOCATION location,
                                                             List<LogInfo> logInfos) {
        LogInfo logInfo = new LogInfo();//some cheapest aetheryte
        LinkedList<LogInfo> sortedList =
                new LinkedList<>(Collections.singletonList(logInfo));
        for (LogInfo log : logInfos) {

        }
        return sortedList;

    }

    public static List<LinkedList<LogInfo>> getShortestRoute(LOCATION location,
                                                             List<LogInfo> logInfos) {
        List<LinkedList<LogInfo>> sortedData = new ArrayList<>();
        List<LogInfo> copy = new ArrayList<>(logInfos);

        //creating logs about aetherytes
        Arrays.stream(location.getAetherytes())
                .forEach(a -> {
                    LogInfo aetheryteLog = new LogInfo(a.getName(),
                            a.getCoordinates(), true);
                    LinkedList<LogInfo> linkedList =
                            new LinkedList<>(Collections.singletonList(aetheryteLog));
                    sortedData.add(linkedList);
                });

        /*LogInfo testStartPoint = new LogInfo();//test start point
        testStartPoint.setTeleport(false);
        testStartPoint.setDoublePoint(new DoublePoint(30, 14));
        testStartPoint.setName("Start point");
        sortedData.add(new LinkedList<>(Collections.singletonList(testStartPoint)));*/

        //find nearest pair aetheryte-point
        LinkedList<LogInfo> leadingList = null;
        LogInfo pairLog = null;
        double minDistance = Double.MAX_VALUE;
        for (LinkedList<LogInfo> list : sortedData) {
            LogInfo aetheryteLog = list.getFirst();
            Map.Entry<LogInfo, Double> logDistance =
                    findNearestLog(logInfos, aetheryteLog);
            LogInfo nearestLog = logDistance.getKey();
            double distance = logDistance.getValue();
            if (distance < minDistance) {
                leadingList = list;
                pairLog = nearestLog;
                minDistance = distance;
            }
        }

        leadingList.add(pairLog);
        copy.remove(pairLog);

        LinkedList<LogInfo> listToAdd = leadingList;
        int copySize = copy.size();
        for (int i = 0; i < copySize; i++) {
            LogInfo comparable = listToAdd.getLast();
            Map.Entry<LogInfo, Double> logDistance =
                    findNearestLog(copy, comparable);
            LogInfo nextNearestLog = logDistance.getKey();
            double minDistance1 = logDistance.getValue();

            for (LinkedList<LogInfo> altList : sortedData) {
                Map.Entry<LogInfo, Double> altLogDistance =
                        findNearestLog(altList, nextNearestLog);
                LogInfo nearestAlt = altLogDistance.getKey();
                double altMinDistance = altLogDistance.getValue();
                if (altMinDistance < minDistance1) {
                    listToAdd = altList;
                    comparable = nearestAlt;
                    minDistance1 = altMinDistance;
                }
            }

            if (comparable.isTeleport()
                    && listToAdd.size() > 1) {
                LinkedList<LogInfo> newList =
                        new LinkedList<>(Collections.singletonList(comparable));
                sortedData.add(newList);
                listToAdd = newList;
            }
            listToAdd.add(listToAdd.indexOf(comparable) + 1,
                    nextNearestLog);
            copy.remove(nextNearestLog);
        }

        return sortedData;
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
