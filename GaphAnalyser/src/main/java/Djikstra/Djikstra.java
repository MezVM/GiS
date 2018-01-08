package Djikstra;

import org.apache.commons.math3.linear.RealMatrix;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


/**
 * Class to manage searching connection between two nodes in matrix
 */
public class Djikstra {

    private List<Predecessor> predecessors = new LinkedList<Predecessor>();
    private List<Successor> currentState = new LinkedList<Successor>();
    private int targetNode;

    /**
     * Search path between 2 nodes
     *
     * @param matrix
     * @param startNode
     * @param targetNode
     * @return path between nodes - list of nodes
     */
    public List<Integer> findPathBetween(RealMatrix matrix, int startNode, int targetNode) {
        return findPathBetween(matrix, startNode, targetNode, new LinkedList<>());
    }

    /**
     * Search path between 2 nodes, ignore nodes in toExclude list
     *
     * @param matrix
     * @param startNode
     * @param targetNode
     * @param toExclude
     * @return path between nodes - list of nodes
     */
    public List<Integer> findPathBetweenWithExclude(RealMatrix matrix,
                                                    int startNode,
                                                    int targetNode,
                                                    List<Integer> toExclude) {
        return findPathBetween(matrix, startNode, targetNode, toExclude);
    }

    private List<Integer> findPathBetween(RealMatrix matrix, int startNode, int targetNode, List<Integer> toExclude) {
        this.targetNode = targetNode;
        matrix = prepereExclude(matrix, toExclude);
        currentState = initBeginState(matrix, startNode, toExclude);
        predecessors = initPredecesors(matrix, startNode);
        int currentNode = startNode;
        do {
            List<Successor> successors = findNeightbours(matrix, currentNode);
            upadateCurrentStateAndPredecessors(successors, currentNode);
            currentNode = lockLowestValueAndSelectNewNode();
        } while (!targetsLocked());
        return findPath(startNode);
    }

    private RealMatrix prepereExclude(RealMatrix matrix, List<Integer> toExclude) {
        double[] zeroArray = new double[matrix.getColumnDimension()];
        for (Integer excluded : toExclude) {
            matrix.setColumn(excluded, zeroArray);
            matrix.setRow(excluded, zeroArray);
        }
        return matrix;
    }

    private List<Integer> findPath(int startNode) {
        Predecessor predecessor = findPredecesor(targetNode).getPredecesor();
        List<Integer> path = new LinkedList<>();
        if (predecessor == null)
            return null;
        while (predecessor.id() != startNode) {
            path.add(predecessor.id());
            predecessor = predecessor.getPredecesor();
            if (predecessor == null)
                return null;
        }
        Collections.reverse(path);
        return path;
    }

    private boolean targetsLocked() {
        return currentState.stream().filter(state -> state.id() == targetNode).findFirst().get().isLock();
    }

    private int lockLowestValueAndSelectNewNode() {
        Successor state = currentState.stream()
                .filter(successor -> !successor.isLock())
                .min(Comparator.comparingInt(o -> o.cost()))
                .get();
        state.lock();
        return state.id();
    }

    private void updatePredecesor(Successor successor, int currentNode) {
        Predecessor currentPredecesor = findPredecesor(currentNode);
        findPredecesor(successor).setPredecesor(currentPredecesor);
    }

    private Predecessor findPredecesor(int currentNode) {
        return predecessors.stream()
                .filter(predecessor -> predecessor.id() == currentNode)
                .findFirst()
                .get();
    }

    private Predecessor findPredecesor(Successor successor) {
        return predecessors.stream()
                .filter(predecessor -> predecessor.id() == successor.id())
                .findFirst()
                .get();
    }

    private List<Predecessor> initPredecesors(RealMatrix matrix, int startNode) {
        LinkedList<Predecessor> predecessors = new LinkedList<Predecessor>();
        int size = matrix.getColumnDimension();
        for (int i = 0; i < size; i++) {
            if (i == startNode) {
                Predecessor predecessor = new Predecessor(i);
                predecessor.setPredecesor(predecessor);
                predecessors.add(predecessor);
            } else {
                predecessors.add(new Predecessor(i));
            }
        }
        return predecessors;
    }

    private void upadateCurrentStateAndPredecessors(List<Successor> successors, int currentNode) {
        Successor currentSuccessor = findSuccesor(currentNode);
        successors.forEach(successor -> findAndUpdateStateForSuccesor(successor, currentSuccessor));
    }

    private Successor findSuccesor(int currentNode) {
        return currentState.stream()
                .filter(successor -> successor.id() == currentNode)
                .findFirst()
                .get();
    }

    private void findAndUpdateStateForSuccesor(Successor successor, Successor currentSuccessor) {
        Successor lookingState = currentState.stream()
                .filter(state -> state.id() == successor.id())
                .findFirst()
                .get();
        if (lookingState.isLock())
            return;
        if (currentSuccessor.cost() + successor.cost() <= lookingState.cost()) {
            lookingState.addCost(successor.cost());
            updatePredecesor(successor, currentSuccessor.id());
        }
    }

    private List<Successor> findNeightbours(RealMatrix matrix, int currentNode) {
        double[] nodes = matrix.getColumn(currentNode);
        List<Successor> neightbours = new LinkedList<Successor>();
        for (int i = 0; i < matrix.getColumnDimension(); i++) {
            if (nodes[i] == 1) {
                neightbours.add(new Successor(i, 1));
            }
        }
        return neightbours;
    }

    private List<Successor> initBeginState(RealMatrix matrix, int startNode, List<Integer> toExclude) {
        LinkedList<Successor> newState = new LinkedList<Successor>();
        int size = matrix.getColumnDimension();
        for (int i = 0; i < size; i++) {
            if (toExclude.contains(i)) {
                Successor successor = new Successor(i);
                successor.lock();
                newState.add(successor);
                continue;
            }
            if (i == startNode) {
                Successor successor = new Successor(i, 0);
                successor.lock();
                newState.add(successor);
            } else {
                newState.add(new Successor(i));
            }
        }
        return newState;
    }
}
