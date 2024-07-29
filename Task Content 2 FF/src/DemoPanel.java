import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DemoPanel extends JPanel {
    final int maxCol = 15;
    final int maxRow = 10;
    final int nodeSize = 70;
    final int screenWidth = nodeSize * maxCol;
    final int screenHeight = nodeSize * maxRow;

    Node[][] node = new Node[maxCol][maxRow];
    Node startNode, currentNode;
    List<Node> goalNodes = new ArrayList<>();
    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> checkedList = new ArrayList<>();
    boolean[] goalReached;
    int step = 0;

    public DemoPanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setLayout(new GridLayout(maxRow, maxCol));
        this.addKeyListener(new KeyHandler(this));
        this.setFocusable(true);

        int col = 0;
        int row = 0;

        while (col < maxCol && row < maxRow) {
            node[col][row] = new Node(col, row);
            this.add(node[col][row]);

            col++;
            if (col == maxCol) {
                col = 0;
                row++;
            }
        }

        setStartNode(0, 0);
//        setGoalNodes(Arrays.asList(new int[]{2, 1}, new int[]{0, 2}));
//        setGoalNodes(Arrays.asList(new int[]{1, 0}, new int[]{2, 0}, new int[]{3, 0}, new int[]{4, 0}, new int[]{5, 0}, new int[]{6, 0}
//        ,new int[]{7, 0}, new int[]{8, 0}, new int[]{9, 0}, new int[]{10, 0}, new int[]{11, 0}, new int[]{12, 0},
//                new int[]{13, 0}, new int[]{14, 0}, new int[]{15, 0}, new int[]{16, 0}, new int[]{17, 0}, new int[]{18, 0},
//                new int[]{19, 0}, new int[]{20, 0}));

        setGoalNodes(Arrays.asList(
                new int[]{1, 0}, new int[]{3, 0},
                new int[]{4, 0}, new int[]{1, 1},
                new int[]{0, 2}, new int[]{1, 2}
        ));

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

    private void setStartNode(int col, int row) {
        node[col][row].setAsStart();
        startNode = node[col][row];
        currentNode = startNode;
    }

    private void setGoalNodes(List<int[]> goals) {
        goalReached = new boolean[goals.size()];
        for (int i = 0; i < goals.size(); i++) {
            int[] goal = goals.get(i);
            node[goal[0]][goal[1]].setAsGoal();
            goalNodes.add(node[goal[0]][goal[1]]);
        }
    }

    private void setSolidNode(int col, int row) {
        node[col][row].setAsSolid();
    }

    private void setCostOnNodes() {
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

        for (Node goalNode : goalNodes) {
            int xDistanceToGoal = Math.abs(node.col - goalNode.col);
            int yDistanceToGoal = Math.abs(node.row - goalNode.row);
            node.hCost += xDistanceToGoal + yDistanceToGoal;
        }

        node.fCost = node.gCost + node.hCost;

        if (node != startNode && !goalNodes.contains(node)) {
            node.setText("<html>F:" + node.fCost + "<br>G:" + node.gCost + "</html>");
        }
    }

    public void autoSearch() {
        while (!allGoalsReached() && step < 300) {
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

            for (int i = 0; i < goalNodes.size(); i++) {
                Node goalNode = goalNodes.get(i);
                if (!goalReached[i] && currentNode == goalNode) {
                    goalReached[i] = true;
                    dynamicHeuristic(goalNodes.get((i + 1) % goalNodes.size()));
                    trackThePath(goalNode);
                }
            }

            step++;
        }
    }

    private boolean allGoalsReached() {
        for (boolean reached : goalReached) {
            if (!reached) {
                return false;
            }
        }
        return true;
    }

    private void dynamicHeuristic(Node otherGoal) {
        for (int i = 0; i < maxCol; i++) {
            for (int j = 0; j < maxRow; j++) {
                if (node[i][j] != currentNode && !node[i][j].checked && !node[i][j].solid) {
                    int xDistanceToGoal = Math.abs(node[i][j].col - goalNodes.get(0).col);
                    int yDistanceToGoal = Math.abs(node[i][j].row - goalNodes.get(0).row);

                    int xDistanceToOtherGoal = Math.abs(node[i][j].col - otherGoal.col);
                    int yDistanceToOtherGoal = Math.abs(node[i][j].row - otherGoal.row);

                    int minDistance = Math.min(xDistanceToGoal + yDistanceToGoal, xDistanceToOtherGoal + yDistanceToOtherGoal);
                    node[i][j].hCost = minDistance;
                    node[i][j].fCost = node[i][j].gCost + node[i][j].hCost;

                    if (node[i][j] != startNode && !goalNodes.contains(node[i][j])) {
                        node[i][j].setText("<html>F:" + node[i][j].fCost + "<br>G:" + node[i][j].gCost + "</html>");
                    }
                }
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

