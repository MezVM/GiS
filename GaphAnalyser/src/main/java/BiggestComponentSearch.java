import javafx.util.Pair;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * helper class to find biggest connectivity in graph
 */
public class BiggestComponentSearch {

    /**
     * finds biggest connectivity, deletes connection (edges) in other
     *
     * @param matrix
     * @return matrix
     */
    static RealMatrix leaveOnlyBiggestComponent(RealMatrix matrix) {

        List<Node> nodes = getListOfNodes(matrix);
        Node startNode = getStartNodeOfBiggestComponent(nodes);
//        for(Node node:nodes){
//            System.out.println("Node: " + node.index);
//            for(Node neigh : node.neighbours){
//                System.out.print(" " + neigh.index);
//            }
//            System.out.println();
//        }
        zeroNodesInOtherComponents(matrix, nodes, startNode);
        return matrix;
    }

    //prepare list with nodes and their neighbours
    private static List<Node> getListOfNodes(RealMatrix matrix) {
        List<Node> nodes = new LinkedList<>();
        for (int i = 0; i < matrix.getColumnDimension(); ++i) {
            nodes.add(new Node(i));
        }
        for (int i = 0; i < matrix.getColumnDimension(); ++i) {
            for (int j = 0; j < matrix.getColumnDimension(); ++j) {
                if (matrix.getEntry(i, j) > 0) {
                    nodes.get(i).neighbours.add(nodes.get(j));
                }
            }
        }
        return nodes;
    }

    private static Node getStartNodeOfBiggestComponent(List<Node> nodes) {
        int componentSize;
        for (Node node : nodes) {
            node.visited = Node.UNVISITED_NODE;
        }
        List<Pair<Node, Integer>> ranking = new ArrayList<>();
        List<Node> queue = new LinkedList<>();

        for (int i = 0; i < nodes.size(); ++i) {
            Node rootNode = nodes.get(i);
            if (rootNode.visited == Node.VISITED_NODE) {
                continue;
            }
            componentSize = 0;
            //BFS---------------------------
            queue.clear();
            rootNode.visited = Node.QUEUED_NODE;
            queue.add(rootNode);
            while (!queue.isEmpty()) {
                Node fromQNode = queue.remove(0);
                componentSize++;
                fromQNode.visited = Node.VISITED_NODE;
                for (Node qNodeNeighbour : fromQNode.neighbours) {
                    if (qNodeNeighbour.visited == Node.UNVISITED_NODE) {
                        qNodeNeighbour.visited = Node.QUEUED_NODE;
                        queue.add(qNodeNeighbour);
                    }
                }
            }
            //BFS_end------------------------
            ranking.add(new Pair<>(rootNode, componentSize));
            if (componentSize >= nodes.size() / 2) {
                return rootNode;
            }
        }
        Node startNode = nodes.get(0);
        componentSize = 0;
        for (Pair<Node, Integer> pair : ranking) {
            if (pair.getValue() > componentSize) {
                componentSize = pair.getValue();
                startNode = pair.getKey();
            }
        }
        return startNode;
    }

    private static void zeroNodesInOtherComponents(RealMatrix matrix, List<Node> nodes, Node startNode) {
        //BFS---------------------------
        for (Node node : nodes) {
            node.visited = Node.UNVISITED_NODE;
        }
        startNode.visited = Node.QUEUED_NODE;
        List<Node> queue = new LinkedList<>();
        queue.clear();
        queue.add(startNode);
        while (!queue.isEmpty()) {
            Node fromQNode = queue.remove(0);
            fromQNode.visited = Node.VISITED_NODE;
            for (Node qNodeNeighbour : fromQNode.neighbours) {
                if (qNodeNeighbour.visited == Node.UNVISITED_NODE) {
                    qNodeNeighbour.visited = Node.QUEUED_NODE;
                    queue.add(qNodeNeighbour);
                }
            }
        }

        //zeros unvisited nodes
        double[] emptyRow = new double[matrix.getRowDimension()];
        for (double d : emptyRow) {
            d = 0;
        }
        for (Node node : nodes) {
            if (node.visited == Node.UNVISITED_NODE) {
                matrix.setRow(node.index, emptyRow);
                matrix.setColumn(node.index, emptyRow);
            }
        }
    }
}

class Node {
    final static int UNVISITED_NODE = 0;
    final static int QUEUED_NODE = 1;
    final static int VISITED_NODE = 2;
    int index;
    int visited;

    List<Node> neighbours;

    Node(int index) {
        this.index = index;
        this.visited = UNVISITED_NODE;
        neighbours = new ArrayList<>();
    }
}