import javafx.util.Pair;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BiggestComponentSearch {

    static RealMatrix leaveOnlyBiggestComponent(RealMatrix matrix) {
        RealMatrix tmp = matrix.copy();
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

        int componentSize;
        for (Node node : nodes) {
            node.visited = Node.UNVISITED_NODE;
        }
        List<Pair<Node, Integer>> ranking = new ArrayList<>();
        List<Node> queue = new LinkedList<>();
        for (int i = 0; i < nodes.size(); ++i) {
            componentSize = 0;
            //BFS---------------------------
            queue.clear();
            Node rootNode = nodes.get(i);

            if(rootNode.visited == Node.VISITED_NODE){
                continue;
            }
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

            ranking.add(new Pair<Node, Integer>(rootNode, componentSize));
            if (componentSize >= nodes.size()) {
                break;
            }
        }


        Node startNode = nodes.get(0);
        componentSize = 0;
        for (Pair<Node, Integer> pair : ranking) {
            if (pair.getValue() > componentSize) {
                startNode = pair.getKey();
            }
        }
        for (int i = 0; i < matrix.getRowDimension(); ++i) {
            for (int j = 0; j < matrix.getColumnDimension(); ++j) {
                matrix.setEntry(i, j, 0);
            }
        }

        //BFS---------------------------
        for (Node node : nodes) {
            node.visited = Node.UNVISITED_NODE;
        }
        startNode.visited = Node.QUEUED_NODE;
        queue.clear();
        queue.add(startNode);
        while (!queue.isEmpty()) {
            Node fromQNode = queue.remove(0);
            matrix.setColumn(fromQNode.index, tmp.getColumn(fromQNode.index));
            matrix.setRow(fromQNode.index, tmp.getRow(fromQNode.index));
            fromQNode.visited = Node.VISITED_NODE;
            for (Node qNodeNeighbour : fromQNode.neighbours) {
                if (qNodeNeighbour.visited == Node.UNVISITED_NODE) {
                    qNodeNeighbour.visited = Node.QUEUED_NODE;
                    queue.add(qNodeNeighbour);
                }
            }
        }
        //BFS_end------------------------
        return matrix;
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