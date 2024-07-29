import java.util.ArrayList;
import java.util.List;

public class DFS {
    public static void main(String[] args) {
        final int V = 7;

        Graph graph = new Graph(V);

        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 5);
        graph.addEdge(2, 6);

        System.out.println("DFS починаючи з вершини 0:");
        graph.DFS(0);
    }
}

class Graph {
    private int V;
    private List<List<Integer>> adjacencyList;

    public Graph(int V) {
        this.V = V;

        for (int i = 0; i < V; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }


    public void addEdge(int v, int w) {
        adjacencyList.get(v).add(w);
    }

    public void DFS(int startVertex) {
        boolean[] visited = new boolean[V];
        DFSRecursive(startVertex, visited);
    }

    private void DFSRecursive(int vertex, boolean[] visited) {
        visited[vertex] = true;
        List<Integer> neighbors = adjacencyList.get(vertex);

        for (Integer neighbor: neighbors){
            if (!visited[neighbor]){
                DFSRecursive(neighbor, visited);
            }
        }

    }
}
