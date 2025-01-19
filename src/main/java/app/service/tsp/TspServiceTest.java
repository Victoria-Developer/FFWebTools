package app.service.tsp;

import app.dto.Coordinate;

import java.util.*;

public class TspServiceTest {

    private static final double MAX_DIST = Double.MAX_VALUE;
    private static final double THRESHOLD = 10;

    public static void main(String[] args) {
        Coordinate t1 = new Coordinate("T1",13.3, 31.1);
        Coordinate t2 = new Coordinate("T2", 31.7, 18);
        Coordinate[] teleports = new Coordinate[]{t1, t2};

        Coordinate p1 = new Coordinate("A",14.9, 26.6);
        Coordinate p2 = new Coordinate("B", 19.9, 9.5);
        Coordinate p3 = new Coordinate("C", 28.0, 20.8);
        Coordinate p4 = new Coordinate("D", 29.4, 29.9);
        Coordinate p5 = new Coordinate("E", 29.8, 12.1);
        Coordinate p6 = new Coordinate("F", 20, 29.9);
        Coordinate p7 = new Coordinate("G",15, 24);

        List<Coordinate> playerC = new ArrayList<>(List.of(p1, p2, p3, p4, p5));
        TspServiceTest tspService = new TspServiceTest();

        // Step 1: Build optimal route for teleports using nNeighbor
        List<Coordinate> optimalTeleportRoute = tspService.nNeighbor(new LinkedList<>(Arrays.asList(teleports)));

        // Step 2: Split into sub-lists based on threshold
        List<List<Coordinate>> subRoutes = tspService.splitRouteByThreshold(optimalTeleportRoute);

        // Step 3: Assign outliers to the nearest cluster
        tspService.assignOutliersToClusters(subRoutes, playerC, teleports);
    }

    // Step 1: Use nNeighbor to find the optimal teleport route
    private List<Coordinate> nNeighbor(LinkedList<Coordinate> coordinates) {
        double[][] distances = getDistances(coordinates);

        LinkedList<Coordinate> route = new LinkedList<>();
        LinkedList<Integer> visited = new LinkedList<>();

        int nNeighbour = 0;
        while (visited.size() < distances.length) {
            double currentMin = MAX_DIST;
            double[] nextDist = distances[nNeighbour];
            for (int j = 0; j < nextDist.length; j++) {
                double currentDist = nextDist[j];
                if (!visited.contains(j) && currentDist < currentMin) {
                    currentMin = currentDist;
                    nNeighbour = j;
                }
            }
            visited.add(nNeighbour);
            Coordinate currentC = coordinates.get(nNeighbour);
            route.add(currentC);
        }

        return route;
    }

    // Step 2: Split the optimal route into sub-routes if distance exceeds threshold
    public List<List<Coordinate>> splitRouteByThreshold(List<Coordinate> optimalRoute) {
        List<List<Coordinate>> subRoutes = new ArrayList<>();
        List<Coordinate> currentSubRoute = new ArrayList<>();
        currentSubRoute.add(optimalRoute.get(0));

        for (int i = 1; i < optimalRoute.size(); i++) {
            Coordinate prev = optimalRoute.get(i - 1);
            Coordinate curr = optimalRoute.get(i);
            if (calcEuclideanDistance(prev, curr) > THRESHOLD) {
                subRoutes.add(new ArrayList<>(currentSubRoute));
                currentSubRoute.clear();
            }
            currentSubRoute.add(curr);
        }

        if (!currentSubRoute.isEmpty()) {
            subRoutes.add(currentSubRoute);
        }

        return subRoutes;
    }

    // Step 3: Assign outliers to the nearest cluster
    public void assignOutliersToClusters(List<List<Coordinate>> subRoutes, List<Coordinate> outliers, Coordinate[] teleports) {
        for (Coordinate outlier : outliers) {
            // Check for nearest teleport
            Coordinate nearestTeleport = Arrays.stream(teleports)
                    .min(Comparator.comparingDouble(teleport -> calcEuclideanDistance(teleport, outlier)))
                    .orElseThrow();

            // Check which sub-list is closest to the outlier
            List<Coordinate> nearestRoute = subRoutes.stream()
                    .min(Comparator.comparingDouble(route -> calcEuclideanDistance(route.get(route.size() - 1), outlier)))
                    .orElseThrow();

            // Add to nearest sub-route
            nearestRoute.add(outlier);
            //System.out.println("Assigned " + outlier.getName() + " to sub-route starting with " + nearestRoute.get(0).getName());
        }
        subRoutes.forEach(r->{
            System.out.println("--------Routes--------");
            r.forEach(c-> System.out.println(c.toString()));
            System.out.println("----------------------");
        });
    }

    // Calculate Euclidean distance between two coordinates
    private double calcEuclideanDistance(Coordinate c1, Coordinate c2) {
        double dx = c1.getX() - c2.getX();
        double dy = c1.getY() - c2.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    // Calculate all pairwise distances between coordinates
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

}
