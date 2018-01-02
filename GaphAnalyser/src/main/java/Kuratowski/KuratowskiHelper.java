package Kuratowski;

import Djikstra.Djikstra;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class KuratowskiHelper {

    public static List<Integer> getNodesByDegree(RealMatrix matrix) {
        List<NodeDegree> nodeByDegreeList = new LinkedList<>();
        for (int node = 0; node < matrix.getRowDimension(); ++node) {
            NodeDegree nodeDegree = new NodeDegree(node, 0);
            double[] oneRow = matrix.getColumn(node);
            for (double elementInRow : oneRow) {
                nodeDegree.degree += elementInRow;
            }
            nodeByDegreeList.add(nodeDegree);
        }
        Collections.sort(nodeByDegreeList);

        List<Integer> nodeSortedList = new LinkedList<>();
        for (NodeDegree nodeDegree : nodeByDegreeList) {
            nodeSortedList.add(nodeDegree.node);
        }
        return nodeSortedList;
    }

    //returns list of kuratowski nodes or NULL
    public static List<Integer> findKuratowskiGraph(RealMatrix matrix) {
        List<Integer> nodeByDegreeList = KuratowskiHelper.getNodesByDegree(matrix);
        //please, don't make this number big, it will count forever :/
        //but it must be bigger than 5 in K5 nad 6 in K3,3
        int windowSize = 9;
        if (windowSize > matrix.getColumnDimension()) {
            windowSize = matrix.getColumnDimension();
        }
        int startIndex = 0;
        int stopIndex = startIndex + windowSize - 1;
        int nextStart = windowSize - 4;
        List<Integer> k5nodes;
        while (stopIndex < nodeByDegreeList.size()) {
            k5nodes = searchK5inWindow(matrix, nodeByDegreeList.subList(startIndex, stopIndex + 1));

            if (k5nodes != null) {
                return k5nodes;
            }

            startIndex += nextStart;
            stopIndex += nextStart;
        }
        return null;
    }

    private static List<Integer> searchK5inWindow(RealMatrix matrix, List<Integer> window) {
        int k = 5;
        int n = window.size();
        List<Integer> candidates = new ArrayList<>(Collections.nCopies(k, 0));
        List<Integer> K5List = combinationRecursionK5(window, candidates, 0, n - 1, 0, k, matrix);
        return K5List;
    }

    private static List<Integer> combinationRecursionK5(List<Integer> window, List<Integer> candidates, int start,
                                                        int end, int index, int k, RealMatrix matrix) {
        if (index == k) {
            if (checkIfK5(matrix, candidates)) {
                return candidates;
            }
            return null;
        }

        for (int i = start; i <= end && end - i + 1 >= k - index; i++) {
            candidates.set(index, window.get(i));
            if (combinationRecursionK5(window, candidates, i + 1, end, index + 1, k, matrix) != null) {
                return candidates;
            }
        }
        return null;
    }

    private static boolean checkIfK5(RealMatrix matrix, List<Integer> candidates) {
        Djikstra djikstra = new Djikstra();
        List<Integer> otherCandidates = new LinkedList<>();
        List<Integer> excluded = new LinkedList<>();
        for (int i = 0; i < 4; ++i) {
            for (int j = i + 1; j < 5; ++j) {

                //direct connection
                if (matrix.getEntry(i, j) > 0) {
                    continue;
                }

                otherCandidates.clear();
                otherCandidates.addAll(candidates);
                otherCandidates.remove(j);
                otherCandidates.remove(i);
                excluded.addAll(otherCandidates);
                List<Integer> path = djikstra.findPathBetweenWithExclude(matrix.copy(), i, j, excluded);
                if (path == null) {
                    return false;
                }
                excluded.removeAll(otherCandidates);
                excluded.addAll(path);

            }
        }
        return true;
    }





    public static void printNodes(List<Integer> nodes) {
        if (nodes != null) {
            for (Integer node : nodes) {
                System.out.print(node + " ");
            }
            System.out.println();
        }
    }
}
