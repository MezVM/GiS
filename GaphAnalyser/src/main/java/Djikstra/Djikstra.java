package Djikstra;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.*;
import java.util.stream.Collectors;

public class Djikstra {

    private List<Predecessor> predecessors = new LinkedList<Predecessor>();
    private List<Successor> currentState = new LinkedList<Successor>();
    private int targetNode;

    public List<Integer> findPathBetween(RealMatrix matrix, int startNode, int targetNode) {
        this.targetNode = targetNode;
        currentState = initBeginState(matrix);
        predecessors = initPredecesors(matrix);
        int currentNode = startNode;
        do {
            List<Successor> successors = findNeightbours(matrix, currentNode);
            upadateCurrentState(successors);
            updatePredecesors(successors, currentNode);
            currentNode = lockLowestValueAndSelectNewNode();
        } while (!targetsLocked());
        return findPath(startNode);
    }

    private List<Integer> findPath(int startNode) {
        Predecessor predecessor = findPredecesor(targetNode);
        List<Integer> path = new LinkedList<>();
        while (predecessor.id() != startNode) {
            path.add(predecessor.id());
            predecessor = predecessor.getPredecesor();
        }
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

    private void updatePredecesors(List<Successor> successors, int currentNode) {
        Predecessor currentPredecesor = findPredecesor(currentNode);
        successors.stream()
                .map(successor -> findPredecesor(successor))
                .forEach(predecessor -> predecessor.setPredecesor(currentPredecesor));
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

    private void upadateCurrentState(List<Successor> successors) throws RuntimeException {
        successors.forEach(this::findAndUpdateStateForSuccesor);
    }

    private void findAndUpdateStateForSuccesor(Successor successor){
        Optional<Successor> lookingState = currentState.stream()
                                                       .filter(state -> state.id() == successor.id())
                                                       .findFirst();
        lookingState.ifPresent(state -> state.addCost(successor.cost()));
    }

    private List<Successor> findNeightbours(RealMatrix matrix, int currentNode) {
        Double[] objectIndexes = ArrayUtils.toObject(matrix.getColumn(currentNode));
        List<Double> nodes = Arrays.asList(objectIndexes);
        return nodes.stream()
                    .filter(node -> node.intValue() == 1)
                    .map(aDouble -> new Successor(aDouble.intValue(),1))
                    .collect(Collectors.toList());
    }

    private List<Successor> initBeginState(RealMatrix matrix) {
        LinkedList<Successor> newState = new LinkedList<Successor>();
        int size = matrix.getColumnDimension();
        for (int i = 0; i < size; i++) {
            newState.add(new Successor(i));
        }
        return newState;
    }
}
