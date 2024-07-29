import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int maxRow = scanner.nextInt();
        int maxCol = scanner.nextInt();

        Node[][] nodes = new Node[maxCol][maxRow];
        List<int[]> goals = new ArrayList<>();

        for (int i = 0; i < maxRow; i++) {
            String rowInput = scanner.next();
            for (int j = 0; j < maxCol; j++) {
                nodes[j][i] = new Node(j, i);
                char inputChar = rowInput.charAt(j);
                if (inputChar == '*') {
                    nodes[j][i].setAsGoal();
                    goals.add(new int[]{j, i});
                }
            }
        }

        DemoPanel demoPanel = new DemoPanel(maxCol, maxRow, nodes, goals);
        demoPanel.autoSearch();
    }
}

class DemoPanel {
    int maxCol;
    int maxRow;

    Node[][] node;
    Node startNode, currentNode;
    List<Node> goalNodes = new ArrayList<>();
    List<Node> openList = new ArrayList<>();
    boolean[] goalReached;
    int step = 0;

    public DemoPanel(int maxCol, int maxRow, Node[][] node, List<int[]> goals) {
        this.maxCol = maxCol;
        this.maxRow = maxRow;
        this.node = node;
        this.goalReached = new boolean[goals.size()];

        setStartNode(0, 0);
        setGoalNodes(goals);
        setCostOnNodes();
    }

    public void setStartNode(int col, int row) {
        node[col][row].setAsStart();
        startNode = node[col][row];
        currentNode = startNode;
    }

    public void setGoalNodes(List<int[]> goals) {
        for (int[] goal : goals) {
            int col = goal[0];
            int row = goal[1];
            node[col][row].setAsGoal();
            goalNodes.add(node[col][row]);
            if (node[col][row] == startNode) {
                node[col][row].setAsOpen();
                goalReached[goalNodes.indexOf(node[col][row])] = true;
            }
        }
    }

    public void setCostOnNodes() {
        for (int i = 0; i < maxCol; i++) {
            for (int j = 0; j < maxRow; j++) {
                getCost(node[i][j]);
            }
        }
    }

    private void getCost(Node node) {
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = Math.max(xDistance, yDistance);  // Use max instead of sum
    }


    public void autoSearch() {
        while (!allGoalsReached()) {
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            openList.remove(currentNode);

            tryOpenNode(col, row - 1);
            tryOpenNode(col - 1, row);
            tryOpenNode(col, row + 1);
            tryOpenNode(col + 1, row);

            dynamicHeuristic();
            moveCurrentNode();

            step++;
        }
        System.out.println(step + goalNodes.size());
    }

    private void tryOpenNode(int col, int row) {
        if (isValidCoordinate(col, row)) {
            Node nodeToOpen = node[col][row];
            if (!nodeToOpen.open && !nodeToOpen.checked && !nodeToOpen.solid) {
                nodeToOpen.setAsOpen();
                nodeToOpen.parent = currentNode;
                openList.add(nodeToOpen);
            }
        }
    }

    private void moveCurrentNode() {
        Node bestNode = findBestNode();
        if (bestNode != null) {
            currentNode = bestNode;
            updateGoalReached();
            dynamicHeuristic();
            trackThePath(bestNode);
        }
    }

    private Node findBestNode() {
        Node bestNode = null;
        int bestNodeIndex = -1;
        int bestNodefCost = Integer.MAX_VALUE;

        for (int i = 0; i < openList.size(); i++) {
            Node currentNode = openList.get(i);
            if (currentNode.fCost < bestNodefCost || (currentNode.fCost == bestNodefCost && currentNode.gCost < openList.get(bestNodeIndex).gCost)) {
                bestNode = currentNode;
                bestNodeIndex = i;
                bestNodefCost = currentNode.fCost;
            }
        }
        return bestNode;
    }

    private boolean allGoalsReached() {
        for (boolean reached : goalReached) {
            if (!reached) {
                return false;
            }
        }
        return true;
    }

    private void dynamicHeuristic() {
        for (int i = 0; i < maxCol; i++) {
            for (int j = 0; j < maxRow; j++) {
                if (node[i][j].open && !node[i][j].checked && !node[i][j].solid) {
                    int minDistance = Integer.MAX_VALUE;

                    for (Node goalNode : goalNodes) {
                        if (!goalReached[goalNodes.indexOf(goalNode)]) {
                            int xDistanceToGoal = Math.abs(node[i][j].col - goalNode.col);
                            int yDistanceToGoal = Math.abs(node[i][j].row - goalNode.row);
                            int totalDistance = Math.max(xDistanceToGoal, yDistanceToGoal);

                            minDistance = Math.min(minDistance, totalDistance);
                        }
                    }

                    node[i][j].hCost = minDistance;
                    node[i][j].fCost = node[i][j].gCost + node[i][j].hCost;
                }
            }
        }

        for (Node goalNode : goalNodes) {
            if (!goalReached[goalNodes.indexOf(goalNode)] && currentNode == goalNode) {
                currentNode.hCost = 0;
                currentNode.fCost = currentNode.gCost;
            }
        }
    }




    private void updateGoalReached() {
        for (Node goalNode : goalNodes) {
            if (!goalReached[goalNodes.indexOf(goalNode)] && currentNode == goalNode) {
                goalReached[goalNodes.indexOf(goalNode)] = true;
            }
        }
    }

    private void trackThePath(Node goal) {
        Node current = goal;
        while (current != null && current != startNode) {
            current.setAsChecked();
            current = current.parent;
        }
    }

    private boolean isValidCoordinate(int col, int row) {
        return col >= 0 && col < maxCol && row >= 0 && row < maxRow;
    }
}

class Node {
    int col;
    int row;
    int gCost;
    int hCost;
    int fCost;
    boolean start;
    boolean goal;
    boolean solid;
    boolean open;
    boolean checked;
    Node parent;

    public Node(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public void setAsStart() {
        start = true;
    }

    public void setAsGoal() {
        goal = true;
    }

    public void setAsOpen() {
        open = true;
    }

    public void setAsChecked() {
        checked = true;
    }
}
