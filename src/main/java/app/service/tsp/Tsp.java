package app.service.tsp;

import app.dto.Coordinate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class Tsp {

    private static final double MARGIN = 1.5;
    private static final double MAX_DIST = Double.MAX_VALUE;
    private static final double THRESHOLD = 10;

    private record Node(Coordinate coordinate1, Coordinate coordinate2, double distance) {
    }

    public LinkedList<Coordinate> solve(Coordinate[] teleports, List<Coordinate> playerC) {
        List<Coordinate> mergedPlayerC = mergeSimilarCoordinates(playerC);
        List<LinkedList<Node>> routes = new ArrayList<>();

        // Find nearest neighbour for each teleport
        Arrays.stream(teleports).forEach(t -> {
            LinkedList<Coordinate> c = new LinkedList<>(List.of(t));
            c.addAll(mergedPlayerC);
            routes.add(nNeighbor(c));
        });

        // Sort by overall distance
        routes.sort(Comparator.comparingDouble(list -> list.stream().mapToDouble(c -> c.distance).sum()));
        // Remove all connected nodes for other teleports
        for (int i = 1; i < routes.size(); i++) {
            LinkedList<Node> list = routes.get(i);
            list.subList(1, list.size()).clear();
        }

        // Find the shortest route and split into arrays
        // if distance between two connected nodes is bigger than the threshold
        LinkedList<Node> optimalRoute = routes.get(0);
        LinkedList<Node> outliers = new LinkedList<>();

        for (Node node : optimalRoute) {
            if (node.distance >= THRESHOLD) outliers.add(node);
        }

        if (!outliers.isEmpty()) {
            // Remove all nodes starting from the first outlier
            Node firstOutlier = outliers.getFirst();
            optimalRoute.subList(optimalRoute.indexOf(firstOutlier), optimalRoute.size()).clear();

            // Connect each coordinate-outlier to the nearest point of existing route.
            for (Node outlier : outliers) {
                double minDistance = outlier.distance;
                LinkedList<Node> nearestRoute = null;
                Coordinate nearestCoordinate = null;
                boolean shouldCreateNewList = false;

                for (LinkedList<Node> route : routes) {
                    Coordinate lastCoordinate = route.getLast().coordinate2;
                    double tDistance = calcEuclideanDistance(lastCoordinate, outlier.coordinate2);
                    if (tDistance <= minDistance) {
                        minDistance = tDistance;
                        nearestRoute = route;
                        nearestCoordinate = lastCoordinate;
                    }
                }

                // Check if any teleport without connected coordinates is the closest coordinate itself
                for (Coordinate teleport : teleports) {
                    double tDistance = calcEuclideanDistance(teleport, outlier.coordinate2);
                    if (tDistance <= minDistance) {
                        minDistance = tDistance;
                        nearestCoordinate = teleport;
                        shouldCreateNewList = true;
                    }
                }

                if (shouldCreateNewList) {
                    LinkedList<Node> newList = new LinkedList<>();
                    newList.add(new Node(nearestCoordinate, nearestCoordinate, 0));
                    nearestRoute = newList;
                    routes.add(nearestRoute);
                }
                nearestRoute.add(new Node(nearestCoordinate, outlier.coordinate2, minDistance));
            }
        }

        return routes.stream()
                .filter(route -> route.size() > 1) // Remove any teleport-list without connected nodes
                .flatMap(route -> route.stream()
                        .map(Node::coordinate2)) // Convert to LinkedList<Coordinate>
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private LinkedList<Node> nNeighbor(LinkedList<Coordinate> coordinates) {
        double[][] distances = getDistances(coordinates);

        LinkedList<Node> nodes = new LinkedList<>();
        LinkedList<Integer> visited = new LinkedList<>();

        Coordinate previousC = coordinates.get(0);
        int nNeighbour = 0;
        while (visited.size() < distances.length) {  // Find nearest neighbors and build nodes
            double currentMin = MAX_DIST;
            double[] nextDist = distances[nNeighbour]; // Distances of the next neighbor
            for (int j = 0; j < nextDist.length; j++) {
                double currentDist = nextDist[j];
                if (!visited.contains(j) && currentDist < currentMin) {
                    currentMin = currentDist;
                    nNeighbour = j;
                }
            }
            visited.add(nNeighbour);
            Coordinate currentC = coordinates.get(nNeighbour);
            nodes.add(new Node(previousC, currentC, currentMin));
            previousC = currentC;
        }
        return nodes;
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

    private List<Coordinate> mergeSimilarCoordinates(List<Coordinate> list) {
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
