import java.util.LinkedList;
import java.util.Queue;


public class Main {


    public static void main(String args[]) {
        BFS bfs = new BFS(10); // Створюємо граф з 6 вершинами


// Додаємо ребра до графа
        bfs.addEdge(0, 1);
        bfs.addEdge(0, 2);
        bfs.addEdge(0, 3);
        bfs.addEdge(0, 6);
        bfs.addEdge(1, 2);
        bfs.addEdge(6, 5);
        bfs.addEdge(6, 7);
        bfs.addEdge(2, 3);
        bfs.addEdge(2, 5);
        bfs.addEdge(3, 4);
        bfs.addEdge(5, 4);
        bfs.addEdge(4, 8);




        System.out.println("Результат обходу BFS (з початкової вершини 0):");
        bfs.breadthFirstSearch(0);
    }
}


class BFS {
    private int V; // Кількість вершин
    private LinkedList<Integer> adjacencyList[]; // Список суміжності


    public BFS(int v) {
        V = v;
        adjacencyList = new LinkedList[v];
        for (int i = 0; i < v; ++i) {
            adjacencyList[i] = new LinkedList<>();
        }
    }


    // Додавання ребра до графа
    public void addEdge(int v, int w) {
        adjacencyList[v].add(w);
    }

    // Пошук в ширину з початкової вершини
    public void breadthFirstSearch(int startVertex) {
// Масив для відстеження відвіданих вершин
        boolean visited[] = new boolean[V];


// Створення черги для BFS
        Queue<Integer> queue = new LinkedList<>();


// Початкову вершину додаємо в чергу і позначаємо як відвідану
        visited[startVertex] = true;
        queue.add(startVertex);


        while (!queue.isEmpty()) {
// Витягуємо вершину з черги і виводимо її
            int currentVertex = queue.poll();
            System.out.print(currentVertex + " ");


// Обходимо всі сусідні вершини поточної вершини
            for (Integer neighbor : adjacencyList[currentVertex]) {
// Якщо вершина ще не відвідана, додаємо її в чергу і позначаємо як відвідану
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.add(neighbor);
                }
            }
        }
    }
}


