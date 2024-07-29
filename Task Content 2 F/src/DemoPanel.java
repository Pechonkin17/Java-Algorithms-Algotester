import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DemoPanel extends JPanel {
    final int maxCol = 15;
    final int maxRow = 10;
    final int nodeSize = 70;
    final int screenWidth = nodeSize * maxCol;
    final int screenHeight = nodeSize * maxRow;

    Node[][] node = new Node[maxCol][maxRow];
    Node startNode, goalNode, goalNode2, currentNode;
    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> checkedList = new ArrayList<>();

    boolean goalReached = false;
    boolean goalReached2 = false;
    int step = 0;

    public DemoPanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setLayout(new GridLayout(maxRow, maxCol));
        this.addKeyListener(new KeyHandler(this));
        this.setFocusable(true);

        int col = 0;
        int row = 0;

        while (col < maxCol && row < maxRow){
            node[col][row] = new Node(col, row);
            this.add(node[col][row]);

            col++;
            if (col == maxCol) {
                col = 0;
                row++;
            }
        }

        setStartNode(3, 6);
        setGoalNode(11, 3);
        setGoalNode2(14, 9);

        setSolidNode(10, 2);
        setSolidNode(10, 3);
        setSolidNode(10, 4);
        setSolidNode(10, 5);
        setSolidNode(10, 6);
        setSolidNode(10, 7);
        setSolidNode(6, 2);
        setSolidNode(7, 2);
        setSolidNode(8, 2);
        setSolidNode(9, 2);
        setSolidNode(11, 7);
        setSolidNode(12, 7);
        setSolidNode(6, 1);

        setCostOnNodes();
    }

    private void setStartNode(int col, int row){
        node[col][row].setAsStart();
        startNode = node[col][row];
        currentNode = startNode;
    }

    private void setGoalNode(int col, int row){
        node[col][row].setAsGoal();
        goalNode = node[col][row];
    }

    private void setGoalNode2(int col, int row) {
        node[col][row].setAsGoal();
        goalNode2 = node[col][row];
    }

    private void setSolidNode(int col, int row){
        node[col][row].setAsSolid();
    }

    private void setCostOnNodes(){
        int col = 0;
        int row = 0;

        while (col<maxCol && row < maxRow){
            getCost(node[col][row]);
            col++;
            if (col == maxCol){
                col = 0;
                row++;
            }
        }
    }

    private void getCost(Node node){
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        node.fCost = node.gCost + node.hCost;

        if (node != startNode && node != goalNode){
            node.setText("<html>F:" + node.fCost + "<br>G:" + node.gCost + "</html>");
        }
    }

    public void search(){
        if(goalReached == false && step < 300){
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            if (row - 1 >= 0){
                openNode(node[col][row - 1]);
            }

            if (col - 1 >= 0){
                openNode(node[col - 1][row]);
            }

            if (row + 1 < maxRow){
                openNode(node[col][row + 1]);
            }

            if (col + 1 < maxCol){
                openNode(node[col + 1][row]);
            }

            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                if (openList.get(i).fCost < bestNodefCost){
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                } else if (openList.get(i).fCost == bestNodefCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost){
                        bestNodeIndex = i;
                    }
                }
            }

            currentNode = openList.get(bestNodeIndex);

            if (currentNode == goalNode){
                goalReached = true;
            }
        }
        step++;
    }

    public void autoSearch() {
        while (!goalReached || !goalReached2) {
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

            if (!goalReached && currentNode == goalNode) {
                goalReached = true;
                dynamicHeuristic(goalNode2); // Update heuristic for the second goal
                trackThePath(goalNode); // Track path for the first goal
            } else if (!goalReached2 && currentNode == goalNode2) {
                goalReached2 = true;
                dynamicHeuristic(goalNode); // Update heuristic for the first goal
                trackThePath(goalNode2); // Track path for the second goal
            }

        }
    }

    private void dynamicHeuristic(Node otherGoal) {
        for (int i = 0; i < maxCol; i++) {
            for (int j = 0; j < maxRow; j++) {
                if (node[i][j] != currentNode && !node[i][j].checked && !node[i][j].solid) {
                    int xDistanceToGoal = Math.abs(node[i][j].col - goalNode.col);
                    int yDistanceToGoal = Math.abs(node[i][j].row - goalNode.row);

                    int xDistanceToOtherGoal = Math.abs(node[i][j].col - otherGoal.col);
                    int yDistanceToOtherGoal = Math.abs(node[i][j].row - otherGoal.row);

                    int minDistance = Math.min(xDistanceToGoal + yDistanceToGoal, xDistanceToOtherGoal + yDistanceToOtherGoal);
                    node[i][j].hCost = minDistance;
                    node[i][j].fCost = node[i][j].gCost + node[i][j].hCost;

                    if (node[i][j] != startNode && node[i][j] != goalNode && node[i][j] != goalNode2) {
                        node[i][j].setText("<html>F:" + node[i][j].fCost + "<br>G:" + node[i][j].gCost + "</html>");
                    }
                }
            }
        }
    }



    private void openNode(Node node){
        if (node.open == false && node.checked == false && node.solid == false){
            node.setAsOpen();
            node.parent = currentNode;
            openList.add(node);
        }
    }

    private void trackThePath(Node goal) {
        Node current = goal;

        while (current != startNode) {
            current = current.parent;
            if (current != startNode) {
                current.setAsPath();
            }
        }
    }

}