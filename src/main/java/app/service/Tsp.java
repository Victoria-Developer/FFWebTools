package app.service;

import app.dto.Coordinate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class Tsp {

    private static final double MARGIN = 1.5;
    private static final double MAX_DIST = Double.MAX_VALUE;
    private static final double THRESHOLD = 10;

    public LinkedList<Coordinate> solve(Coordinate[] teleports, List<Coordinate> playerC) {
        List<LinkedList<Coordinate>> routes = new ArrayList<>();

        LinkedList<Coordinate> allCoordinates = new LinkedList<>(Arrays.asList(teleports));
        List<Coordinate> mergedPlayerC = mergeCloseCoordinates(playerC);
        allCoordinates.addAll(mergedPlayerC);
        double[][] allDistances = getDistances(allCoordinates);

        // Find nearest teleport-coordinate pair
        double minDistance = MAX_DIST;
        int nearestTeleportInd = 0;
        int nearestCIndex = teleports.length;
        for (int i = 0; i < teleports.length; i++) {
            double[] nextDist = allDistances[i];
            for (int j = 0; j < nextDist.length; j++) {
                double distance = nextDist[j];
                if (i != j && nextDist[j] < minDistance) {
                    nearestTeleportInd = i;
                    nearestCIndex = j;
                    minDistance = distance;
                }
            }
        }

        Coordinate teleport = allCoordinates.get(nearestTeleportInd);
        Coordinate nearestPoint = allCoordinates.get(nearestCIndex);
        LinkedList<Coordinate> firstRoute = new LinkedList<>(List.of(teleport, nearestPoint));
        routes.add(firstRoute);
        mergedPlayerC.remove(nearestPoint);

        Set<Integer> visitedIndexes = new HashSet<>();

        visitedIndexes.add(nearestCIndex);
        LinkedList<Coordinate> currentRoute = firstRoute;
        int currentTeleport = nearestTeleportInd;
        int nextIndex = nearestCIndex; // Start with the nearest player coordinate

        // Use Nearest Neighbor to build routes
        while (visitedIndexes.size() < mergedPlayerC.size() + 1) {
            boolean shouldCreateRoute = false;
            double minDist = MAX_DIST;

            double[] nextDist = allDistances[nextIndex];
            for (int j = teleports.length; j < nextDist.length; j++) {
                double distance = nextDist[j];
                if (!visitedIndexes.contains(j) && distance < minDist) {
                    nextIndex = j;
                    minDist = distance;
                }
            }

            if (minDist >= THRESHOLD) {

                // Check if any other teleport is closer to the current coordinate
                for (LinkedList<Coordinate> route : routes) {
                    int routeLastC = allCoordinates.indexOf(route.getLast());
                    double[] routeLastCDistances = allDistances[routeLastC];
                    double routeLastCDistance = routeLastCDistances[nextIndex];
                    if (routeLastC != nextIndex && routeLastCDistance < minDist) {
                        minDist = routeLastCDistance;
                        currentRoute = route;
                    }
                }

                // Check if there's any other teleport-coordinate pair with less distance
                for (int i = 0; i < teleports.length; i++) {
                    double[] otherDistances = allDistances[i];
                    for (int j = teleports.length; j < nextDist.length; j++) {
                        double distance = otherDistances[j];
                        if (!visitedIndexes.contains(j) && distance < minDist) {
                            nextIndex = j;
                            minDist = distance;

                            currentTeleport = i;
                            shouldCreateRoute = true;
                        }
                    }
                }
            }

            if (shouldCreateRoute) {
                Coordinate t = allCoordinates.get(currentTeleport);
                currentRoute = new LinkedList<>(List.of(t));
                routes.add(currentRoute);
            }
            currentRoute.add(allCoordinates.get(nextIndex));
            visitedIndexes.add(nextIndex);
        }

        // Merge all routes to create a single list
        return routes.stream()
                .flatMap(List::stream)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private double[][] getDistances(List<Coordinate> points) {
        int totalPoints = points.size();
        double[][] distances = new double[totalPoints][totalPoints];
        for (int i = 0; i < totalPoints; i++) {
            for (int j = 0; j < totalPoints; j++) {
                distances[i][j] = calcEuclideanDistance(points.get(i), points.get(j));
            }
        }
        return distances;
    }

    private double calcEuclideanDistance(Coordinate c1, Coordinate c2) {
        double dx = c1.getX() - c2.getX();
        double dy = c1.getY() - c2.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private List<Coordinate> mergeCloseCoordinates(List<Coordinate> list) {
        for (int i = 0; i < list.size(); i++) {
            Coordinate current = list.get(i);

            for (int j = i + 1; j < list.size(); j++) {
                Coordinate other = list.get(j);

                if (calcEuclideanDistance(current, other) <= MARGIN) {
                    if (!current.getName().equals(other.getName()))
                        current.setName(current.getName() + " & " + other.getName());
                    list.remove(j);
                    j--;
                }
            }
        }
        return list;
    }

}
