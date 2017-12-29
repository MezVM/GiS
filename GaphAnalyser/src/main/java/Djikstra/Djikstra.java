package Djikstra;


import org.apache.commons.math3.linear.RealMatrix;

import java.util.LinkedList;
import java.util.List;

public class Djikstra {

    private List<Predecessor> predecessors = new LinkedList<Predecessor>();
    private List<Successor> currentState = new LinkedList<Successor>();

    public List<Integer> findPathBetween(RealMatrix matrix, int startNode, int targetNode) {

        currentState = initBeginState(matrix);
        predecessors = initPredecesors(matrix);
        int currentNode = startNode;
        do {
            List<Successor> successors = findNeightbours(matrix, currentNode);
            upadateCurrentState(successors);
            updatePredecesors(successors);
            lockLowestValue();
            currentNode = selectNewNode();
        } while (areAllPossibleTargetsSets());
        return null;
    }

    private int selectNewNode() {
        return 0;
    }

    private void lockLowestValue() {

    }

    private void updatePredecesors(List<Successor> successors) {

    }

    private List<Predecessor> initPredecesors(RealMatrix matrix) {
        return null;
    }

    private void upadateCurrentState(List<Successor> currentState) {
    }

    private List<Successor> findNeightbours(RealMatrix matrix, int currentNode) {
        return null;
    }

    private boolean areAllPossibleTargetsSets() {
        return false;
    }

    private List<Successor> initBeginState(RealMatrix matrix) {
        return null;
    }
}
