package app.service.tsp;

import app.dto.Coordinate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class Tsp {

    private static final double MAX_DIST = Double.MAX_VALUE;
    private static final double THRESHOLD = 10;

    private record Node(Coordinate coordinate1, Coordinate coordinate2, double distance) {
    }

    public LinkedList<Coordinate> solve(Coordinate[] teleports, List<Coordinate> playerC) {
        List<LinkedList<Node>> routes = new ArrayList<>();

        // Split by proximity to teleports and build routes
        clusterByTeleports(teleports, playerC)
                .forEach(list -> routes.add(nNeighbor(list)));
        log("Clustered routes", routes);
        // Merge routes if ending points' distance is lesser than THRESHOLD
        mergeRoutes(routes);
        log("Merged routes", routes);
        // split if teleport is nearer than connected point
        return routes.stream()
                .flatMap(route -> route.stream()
                        .map(Node::coordinate2))
                .collect(Collectors.toCollection(LinkedList::new));
       /*return routes.stream()
                .map(route -> route.stream()
                        .map(Node::coordinate2)
                        .collect(Collectors.toCollection(LinkedList::new)))
                .collect(Collectors.toList());*/
    }

    private void log(String title, List<LinkedList<Node>> routes) {
        System.out.println("----" + title + "----");
        routes.forEach(list -> {
            list.forEach(node -> System.out.println(node.coordinate2.toString()));
            System.out.println("--------------------------");
        });
    }

    private List<LinkedList<Coordinate>> clusterByTeleports(Coordinate[] teleports, List<Coordinate> playerC) {
        List<LinkedList<Coordinate>> proximityList = new ArrayList<>();
        for (Coordinate t : teleports) {
            proximityList.add(new LinkedList<>(List.of(t)));
        }

        for (Coordinate coordinate : playerC) {
            proximityList.stream()
                    .min(Comparator.comparingDouble(list
                            -> calcEuclideanDistance(list.get(0), coordinate)) // Teleport has index 0
                    )
                    .ifPresent(listWithNearestTeleport -> listWithNearestTeleport.add(coordinate));
        }

        return proximityList;
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
        double dx = c1.getPoint().x() - c2.getPoint().x();
        double dy = c1.getPoint().y() - c2.getPoint().y();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private void mergeRoutes(List<LinkedList<Node>> routes) {
        for (int i = 0; i < routes.size(); i++) {
            LinkedList<Node> route1 = routes.get(i);
            Coordinate lastC1 = route1.getLast().coordinate2;

            for (int j = i + 1; j < routes.size(); j++) {
                LinkedList<Node> route2 = routes.get(j);
                Coordinate lastC2 = route2.getLast().coordinate2;

                if (calcEuclideanDistance(lastC1, lastC2) < THRESHOLD) {
                    // Distance between teleport and first connected player's coordinate
                    boolean shouldMergeFirstList = route1.get(1).distance <= route2.get(1).distance;
                    LinkedList<Node> listToMerge = shouldMergeFirstList ? route1 : route2;
                    LinkedList<Node> listToRemove = shouldMergeFirstList ? route2 : route1;

                    Collections.reverse(listToRemove);
                    listToRemove.removeLast();
                    listToMerge.addAll(listToRemove);
                    routes.remove(listToRemove);
                    j--;
                    i--;
                }
            }
        }
    }

}
