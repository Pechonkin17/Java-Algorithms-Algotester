import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        Map<Integer, List<Integer>> graph = new HashMap<>();

        for (int i = 0; i < m; i++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            graph.computeIfAbsent(a, k -> new ArrayList<>()).add(b);
        }

        int result = findToiletRoom(graph, n);
        System.out.println(result);
    }

    public static int findToiletRoom(Map<Integer, List<Integer>> graph, int n) {
        for (int room = 1; room <= n; room++) {
            Set<Integer> visited = new HashSet<>();
            dfs(graph, room, visited);
            if (visited.size() == n) {
                return room;
            }
        }
        return -1;
    }

    public static void dfs(Map<Integer, List<Integer>> graph, int room, Set<Integer> visited) {
        if (!visited.contains(room)) {
            visited.add(room);
            List<Integer> neighbors = graph.get(room);
            if (neighbors != null) {
                for (int neighbor : neighbors) {
                    dfs(graph, neighbor, visited);
                }
            }
        }
    }
}
