//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//
//        final int col = scanner.nextInt();
//        final int row = scanner.nextInt();
//
//        DemoPanel demoPanel = new DemoPanel(col, row);
//
//        Node[][] node = new Node[col][row];
//
//        char[][] charArray = new char[col][row];
//        for (int i = 0; i < col; i++) {
//            for (int j = 0; j < row; j++) {
//                charArray[i][j] = scanner.next().charAt(0);
//            }
//        }
//
//        demoPanel.setStartNode(0, 0);
//        demoPanel.setGoalNode(col, row);
//
//        for (int i = 0; i < col; i++) {
//            for (int j = 0; j < row; j++) {
//               if (charArray[i][j] == '*'){
//                   demoPanel.setSolidNode(i, j);
//               }
//            }
//        }
//
//        demoPanel.setCostOnNodes();
//
//        demoPanel.autoSearch();
//
//
//        for (int i = 0; i < col; i++) {
//            for (int j = 0; j < row; j++) {
//                System.out.print(" " + charArray[i][j]);
//            }
//            System.out.println();
//        }
//    }
//}







import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введіть кількість стовпців: ");
        final int col = scanner.nextInt();

        System.out.print("Введіть кількість рядків: ");
        final int row = scanner.nextInt();

        DemoPanel demoPanel = new DemoPanel(col, row);

        Node[][] node = new Node[col][row];

        char[][] charArray = new char[col][row];
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                charArray[i][j] = scanner.next().charAt(0);
            }
        }

        demoPanel.setStartNode(0, 0);
        demoPanel.setGoalNode(col - 1, row - 1);

        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                if (charArray[i][j] == '*') {
                    demoPanel.setSolidNode(i, j);
                }
            }
        }

        demoPanel.setCostOnNodes();

        demoPanel.autoSearch();

        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                System.out.print(" " + charArray[i][j]);
            }
            System.out.println();
        }
    }
}

class Node {
    Node parent;
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

    public void setAsSolid() {
        solid = true;
    }

    public void setAsOpen() {
        open = true;
    }

    public void setAsChecked() {
        checked = true;
    }

    public void setAsPath() {
        // You can define your own representation for path in the console
        System.out.print(" P ");
    }
}

class DemoPanel {
    private int maxCol;
    private int maxRow;
    Node[][] node = new Node[maxCol][maxRow];
    Node startNode, goalNode, currentNode;
    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> checkedList = new ArrayList<>();
    boolean goalReached = false;
    int step = 0;

    public DemoPanel(int maxCol, int maxRow) {
        this.maxCol = maxCol;
        this.maxRow = maxRow;
    }

    public void setStartNode(int col, int row) {
        if (col >= 0 && col < maxCol && row >= 0 && row < maxRow) {
            node[col][row].setAsStart();
            startNode = node[col][row];
            currentNode = startNode;
        } else {
            System.out.println("Помилка: введені координати за межами масиву.");
        }
    }

    public void setGoalNode(int col, int row) {
        node[col][row].setAsGoal();
        goalNode = node[col][row];
    }

    public void setSolidNode(int col, int row) {
        node[col][row].setAsSolid();
    }

    public void setCostOnNodes() {
        int col = 0;
        int row = 0;

        while (col < maxCol && row < maxRow) {
            getCost(node[col][row]);
            col++;
            if (col == maxCol) {
                col = 0;
                row++;
            }
        }
    }

    private void getCost(Node node) {
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        node.fCost = node.gCost + node.hCost;
    }

    public void search() {
        if (goalReached == false && step < 300) {
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            if (row - 1 >= 0) {
                openNode(node[col][row - 1]);
            }

            if (col - 1 >= 0) {
                openNode(node[col - 1][row]);
            }

            if (row + 1 < maxRow) {
                openNode(node[col][row + 1]);
            }

            if (col + 1 < maxCol) {
                openNode(node[col + 1][row]);
            }

            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                if (openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                } else if (openList.get(i).fCost == bestNodefCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            currentNode = openList.get(bestNodeIndex);

            if (currentNode == goalNode) {
                goalReached = true;
            }
        }
        step++;
    }

    public void autoSearch() {
        while (goalReached == false) {
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            if (row - 1 >= 0) {
                openNode(node[col][row - 1]);
            }

            if (col - 1 >= 0) {
                openNode(node[col - 1][row]);
            }

            if (row + 1 < maxRow) {
                openNode(node[col][row + 1]);
            }

            if (col + 1 < maxCol) {
                openNode(node[col + 1][row]);
            }

            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                if (openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                } else if (openList.get(i).fCost == bestNodefCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            currentNode = openList.get(bestNodeIndex);

            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
        }
    }

    private void openNode(Node node) {
        if (node.open == false && node.checked == false && node.solid == false) {
            node.setAsOpen();
            node.parent = currentNode;
            openList.add(node);
        }
    }

    private void trackThePath() {
        Node current = goalNode;

        while (current != startNode) {
            current = current.parent;
            if (current != startNode) {
                current.setAsPath();
            }
        }
    }
}
