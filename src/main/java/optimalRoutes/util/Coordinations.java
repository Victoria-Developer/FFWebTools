package optimalRoutes.util;

import optimalRoutes.DoublePoint;
import optimalRoutes.LogInfo;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class Coordinations {
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
}
