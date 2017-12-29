package Djikstra;

import org.apache.commons.math3.linear.RealMatrix;

import java.util.*;

public class Djikstra {

    private List<Predecessor> predecessors = new LinkedList<Predecessor>();
    private List<Successor> currentState = new LinkedList<Successor>();
    private int targetNode;

    public List<Integer> findPathBetween(RealMatrix matrix, int startNode, int targetNode) {
        this.targetNode = targetNode;
        currentState = initBeginState(matrix, startNode);
        predecessors = initPredecesors(matrix);
        int currentNode = startNode;
        do {
            List<Successor> successors = findNeightbours(matrix, currentNode);
            upadateCurrentStateAndPredecessors(successors, currentNode);
            currentNode = lockLowestValueAndSelectNewNode();
        } while (!targetsLocked());
        return findPath(startNode);
    }

    private List<Integer> findPath(int startNode) {
        Predecessor predecessor = findPredecesor(targetNode).getPredecesor();
        List<Integer> path = new LinkedList<>();
        while (predecessor.id() != startNode) {
            path.add(predecessor.id());
            predecessor = predecessor.getPredecesor();
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

    private List<Predecessor> initPredecesors(RealMatrix matrix) {
        LinkedList<Predecessor> predecessors = new LinkedList<Predecessor>();
        int size = matrix.getColumnDimension();
        for (int i = 0; i < size; i++) {
            predecessors.add(new Predecessor(i));
        }
        return predecessors;
    }

    private void upadateCurrentStateAndPredecessors(List<Successor> successors, int currentNode) {
        Successor currentSuccessor = findSuccesor(currentNode);
        successors.forEach(successor -> findAndUpdateStateForSuccesor(successor,currentSuccessor));
    }

    private Successor findSuccesor(int currentNode) {
        return currentState.stream()
                .filter(successor -> successor.id() == currentNode)
                .findFirst()
                .get();
    }

    private void findAndUpdateStateForSuccesor(Successor successor, Successor currentSuccessor){
        Successor lookingState = currentState.stream()
                .filter(state -> state.id() == successor.id())
                .findFirst()
                .get();
        if (lookingState.isLock())
            return;
        if (currentSuccessor.cost() + successor.cost() <= lookingState.cost()) {
            lookingState.addCost(successor.cost());
            updatePredecesor(successor, currentSuccessor.id());
        } else {
            int a = 1+2;
        }
    }

    private List<Successor> findNeightbours(RealMatrix matrix, int currentNode) {
        double[] nodes = matrix.getColumn(currentNode);
        List<Successor> neightbours = new LinkedList<Successor>();
        for (int i = 0; i < matrix.getColumnDimension(); i++) {
            if(nodes[i] == 1) {
                neightbours.add(new Successor(i,1));
            }
        }
        return neightbours;
    }

    private List<Successor> initBeginState(RealMatrix matrix, int startNode) {
        LinkedList<Successor> newState = new LinkedList<Successor>();
        int size = matrix.getColumnDimension();
        for (int i = 0; i < size; i++) {
            if (i == startNode) {
                Successor successor = new Successor(i, 1);
                successor.lock();
                newState.add(successor);
            } else {
                newState.add(new Successor(i));
            }
        }
        return newState;
    }
}
