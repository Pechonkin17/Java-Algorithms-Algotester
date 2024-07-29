import java.util.ArrayList;
import java.util.List;

public class DFS {
    public static void main(String[] args) {
        final int V = 9; // кількість вершин графа

        Graph graph = new Graph(V);

// Додавання ребер до графа
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
        graph.addEdge(0, 6);
        graph.addEdge(1, 2);
        graph.addEdge(6, 5);
        graph.addEdge(6, 7);
        graph.addEdge(2, 3);
        graph.addEdge(2, 5);
        graph.addEdge(3, 4);
        graph.addEdge(5, 4);
        graph.addEdge(4, 8);

        System.out.println("DFS починаючи з вершини 0:");
        graph.DFS(0);
    }
}

class Graph {
    private int V; // Кількість вершин графа
    private List<List<Integer>> adjacencyList; // список суміжності


    // Отже, цей код налаштовує початковий стан графа,
// встановлюючи кількість вершин і створюючи порожні списки суміжності для кожної вершини.
// Це стандартна практика для ініціалізації структури даних, яка представляє граф.

    public Graph(int V) {
        this.V = V;

// Тут створюється новий об'єкт ArrayList, який представлятиме список суміжності для графа.
// Параметр V вказує на початковий розмір списку, який може бути корисним для оптимізації пам'яті.

        adjacencyList = new ArrayList<>(V);


// Цикл створює порожні  списки для кожної вершини графа і додає їх до списку adjacencyList.
// Кожен список представляє суміжні вершини конкретної вершини графа.
// В результаті цього циклу готовий список суміжності буде містити V порожніх списків, один для кожної вершини.

        for (int i = 0; i < V; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int v, int w) {
        adjacencyList.get(v).add(w);
    }

    //private void DFSRecursive(int vertex, boolean[] visited): Цей рядок визначає рекурсивний метод з двома параметрами - vertex (поточна вершина, яку ми відвідуємо) та visited (масив булевих значень, який вказує, чи вже була відвідана дана вершина). Метод рекурсивно викликає сам себе для обходу графа.
//
//visited[vertex] = true;: Цей рядок встановлює значення true в масиві visited для поточної вершини vertex, щоб позначити, що ця вершина була відвідана.
//
//System.out.print(vertex + " ");: Цей рядок виводить номер поточної вершини vertex на консоль. Це дозволяє відстежувати порядок вершин, які відвідує алгоритм DFS.
//
//List<Integer> neighbors = adjacencyList.get(vertex);: Цей рядок отримує список сусідніх вершин для поточної вершини vertex зі списку суміжності.
//
//for (Integer neighbor : neighbors) { ... }: Цикл ітерується по списку сусідніх вершин для поточної вершини vertex.
//
//if (!visited[neighbor]) { DFSRecursive(neighbor, visited); }: У цьому рядку перевіряється, чи вершина neighbor не була відвідана раніше. Якщо це так, то викликається рекурсивно метод DFSRecursive для обходу вершини neighbor. Ця операція рекурсивно викликає метод для всіх сусідніх вершин, які ще не були відвідані, що дозволяє продовжити обхід графа в глибину.
//
//Отже, цей метод виконує алгоритм DFS для графа, відвідуючи вершини в глибину та виводячи їх номери на консоль.

    public void DFS(int startVertex) {
        boolean[] visited = new boolean[V];
        DFSRecursive(startVertex, visited);
    }

    private void DFSRecursive(int vertex, boolean[] visited) {
        visited[vertex] = true;
        System.out.print(vertex + " ");

        List<Integer> neighbors = adjacencyList.get(vertex);
        for (Integer neighbor : neighbors) {
            if (!visited[neighbor]) {
                DFSRecursive(neighbor, visited);
            }
        }
    }
}
