package app.service.tsp;

import app.dto.Coordinate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        // Keep only route with the least overall distance
        LinkedList<Node> optimalRoute = routes.stream()
                .min(Comparator.comparingDouble(route -> route.stream().mapToDouble(node -> node.distance).sum()))
                .orElse(new LinkedList<>());
        routes.clear();
        routes.add(optimalRoute);

        // Split if distance between two connected nodes is bigger than the threshold
        LinkedList<Node> outliers = optimalRoute.stream()
                .filter(node -> node.distance >= THRESHOLD)
                .collect(Collectors.toCollection(LinkedList::new));

        if (!outliers.isEmpty()) {
            Node firstOutlier = outliers.getFirst();
            // Remove all nodes starting from the first outlier
            optimalRoute.subList(optimalRoute.indexOf(firstOutlier), optimalRoute.size()).clear();
            // Connect each coordinate-outlier to the nearest point of existing route
            connectOutliers(routes, outliers, teleports);
        }

        // Convert to LinkedList<Coordinate>
        return routes.stream()
                .flatMap(route -> route.stream().map(Node::coordinate2))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private void connectOutliers(List<LinkedList<Node>> routes, LinkedList<Node> outliers, Coordinate[] teleports) {
        for (Node outlier : outliers) {
            double minDistance = Double.MAX_VALUE;
            LinkedList<Node> nearestRoute = null;
            Coordinate nearestCoordinate = null;

            // Existing routes + teleports
            Stream<Coordinate> allCoordinates = Stream.concat(
                    routes.stream().map(route -> route.getLast().coordinate2), // Last nodes of each route
                    Arrays.stream(teleports) // Teleports
            );

            // Find the nearest coordinate
            for (Coordinate coordinate : allCoordinates.toList()) {
                double distance = calcEuclideanDistance(coordinate, outlier.coordinate2);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestCoordinate = coordinate;
                    nearestRoute = routes.stream()
                            .filter(route -> route.getLast().coordinate2.equals(coordinate))
                            .findFirst()
                            .orElse(null);
                }
            }

            // Create new list if such route doesn't exist
            if (nearestRoute == null) {
                Node startNode = new Node(nearestCoordinate, nearestCoordinate, 0);
                nearestRoute = new LinkedList<>(List.of(startNode));
                routes.add(nearestRoute);
            }

            nearestRoute.add(new Node(nearestCoordinate, outlier.coordinate2, minDistance));
        }
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
