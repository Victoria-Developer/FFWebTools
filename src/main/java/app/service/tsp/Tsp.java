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

        // Find nearest neighbour for each teleport
        Arrays.stream(teleports).forEach(t -> {
            LinkedList<Coordinate> c = new LinkedList<>(List.of(t));
            c.addAll(playerC);
            routes.add(nNeighbor(c));
        });

        // Sort by overall distance
        routes.sort(Comparator.comparingDouble(list -> list.stream().mapToDouble(c -> c.distance).sum()));

        // Find the shortest route and split into arrays
        // if distance between two connected nodes is bigger than threshold
        LinkedList<Node> optimalRoute = routes.get(0);
        LinkedList<LinkedList<Node>> separatedRoutes = new LinkedList<>();
        optimalRoute.forEach(node->{

        });
        // Connect each array-outlier to the nearest point starting from the first array
        // Point can be the ending point of existing shortest route or any teleport.

        // Flatten to LinkedList<Coordinate>
        return routes.stream()
                .flatMap(route -> route.stream()
                        .map(Node::coordinate2))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private void log(String title, List<LinkedList<Node>> routes) {
        System.out.println("----" + title + "----");
        routes.forEach(list -> {
            list.forEach(node -> System.out.println(node.coordinate2.toString()));
            System.out.println("--------------------------");
        });
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

}
