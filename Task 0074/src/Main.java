import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    static class Column {
        double x, y;

        Column(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        double leftBoundary = scanner.nextDouble();
        double rightBoundary = scanner.nextDouble();
        double radius = scanner.nextDouble();
        int numberOfColumns = scanner.nextInt();

        List<Column> columns = new ArrayList<>();
        for (int i = 0; i < numberOfColumns; i++) {
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            columns.add(new Column(x, y));
        }

        double result = findMaxVaseDiameter(leftBoundary, rightBoundary, radius, numberOfColumns, columns);
        System.out.printf("%.4f\n", result * 2);
    }

    static boolean doColumnsOverlap(Column column1, Column column2, double radius) {
        double x1 = column1.x, y1 = column1.y;
        double x2 = column2.x, y2 = column2.y;
        double distanceSquared = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
        return distanceSquared <= (2 * radius) * (2 * radius);
    }

    static boolean dfs(List<List<Integer>> adjacencyList, int currentNode, Set<Integer> visitedNodes, double currentRadius, double leftLimit, double rightLimit, Set<Integer> currentPath, List<Column> columns, double columnRadius) {
        visitedNodes.add(currentNode);
        currentPath.add(currentNode);

        for (int neighbor : adjacencyList.get(currentNode)) {
            if (!visitedNodes.contains(neighbor)) {
                double minX = currentPath.stream().mapToDouble(i -> columns.get(i).x).min().orElse(Double.POSITIVE_INFINITY);
                double maxX = currentPath.stream().mapToDouble(i -> columns.get(i).x).max().orElse(Double.NEGATIVE_INFINITY);

                if (minX - currentRadius - columnRadius < leftLimit && maxX + currentRadius + columnRadius > rightLimit) {
                    return true;
                }

                if (dfs(adjacencyList, neighbor, visitedNodes, currentRadius, leftLimit, rightLimit, currentPath, columns, columnRadius)) {
                    return true;
                }
            }
        }

        double minX = currentPath.stream().mapToDouble(i -> columns.get(i).x).min().orElse(Double.POSITIVE_INFINITY);
        double maxX = currentPath.stream().mapToDouble(i -> columns.get(i).x).max().orElse(Double.NEGATIVE_INFINITY);

        return minX - currentRadius - columnRadius < leftLimit && maxX + currentRadius + columnRadius > rightLimit;
    }


    static double findMaxVaseDiameter(double leftLimit, double rightLimit, double columnRadius, int numOfColumns, List<Column> listOfColumns) {
        double left = 0, right = Math.abs(leftLimit - rightLimit) / 2;
        double epsilon = 1e-9;

        while (right - left > epsilon) {
            double currentRadius = (left + right) / 2;
            List<List<Integer>> graph = new ArrayList<>(numOfColumns);

            for (int i = 0; i < numOfColumns; i++) {
                graph.add(new ArrayList<>());
            }

            double leftLimit1 = leftLimit + currentRadius;
            double rightLimit1 = rightLimit - currentRadius;

            for (int i = 0; i < numOfColumns; i++) {
                for (int j = i + 1; j < numOfColumns; j++) {
                    if (doColumnsOverlap(listOfColumns.get(i), listOfColumns.get(j), columnRadius + currentRadius)) {
                        graph.get(i).add(j);
                    }
                    if (doColumnsOverlap(listOfColumns.get(j), listOfColumns.get(i), columnRadius + currentRadius)) {
                        graph.get(j).add(i);
                    }
                }
            }

            Set<Integer> visitedNodes = new HashSet<>();
            int count = 0;

            for (int i = 0; i < numOfColumns; i++) {
                Set<Integer> currentPath = new HashSet<>();
                if (dfs(graph, i, visitedNodes, currentRadius, leftLimit1, rightLimit1, currentPath, listOfColumns, columnRadius)) {
                    count += 1;
                    break;
                }
            }
            if (count >= 1) {
                right = currentRadius;
            } else {
                left = currentRadius;
            }
        }
        return left;
    }
}
