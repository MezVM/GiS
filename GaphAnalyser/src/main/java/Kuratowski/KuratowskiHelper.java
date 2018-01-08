package Kuratowski;

import Djikstra.Djikstra;
import org.apache.commons.math3.linear.RealMatrix;
import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to manage searching for Kuratowski subgraphs
 */
public class KuratowskiHelper {
    private static final int WINDOW_SIZE = 10;

    /**
     * Returns list of nodes in graph sorted descending by degree
     *
     * @param matrix
     * @return list
     */
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

    /**
     * returns list of K5 nodes or NULL in case K5 not found
     *
     * @param matrix
     * @return K5 nodes list
     */
    public static List<Integer> findKuratowskiGraphK5(RealMatrix matrix) {
        System.out.println("findKuratowskiGraphK5");
        if (matrix.getRowDimension() < 5) {
            return null;
        }
        List<Integer> nodeByDegreeList = KuratowskiHelper.getNodesByDegree(matrix);
        //must be bigger than 5 in K5 nad 6 in K3,3
        int windowSize = WINDOW_SIZE;
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

    /**
     * returns list of K3,3 nodes or NULL in case K5 not found
     *
     * @param matrix
     * @return K3, 3 nodes list
     */
    public static List<Integer> findKuratowskiGraphK33(RealMatrix matrix) {
        if (matrix.getRowDimension() < 6) {
            return null;
        }
        List<Integer> nodeByDegreeList = KuratowskiHelper.getNodesByDegree(matrix);
        //must be bigger than 5 in K5 nad 6 in K3,3
        int windowSize = WINDOW_SIZE;
        if (windowSize > matrix.getColumnDimension()) {
            windowSize = matrix.getColumnDimension();
        }
        int startIndex = 0;
        int stopIndex = startIndex + windowSize - 1;
        int nextStart = windowSize - 5;
        List<Integer> k33nodes;
        while (stopIndex < nodeByDegreeList.size()) {
            k33nodes = searchK33inWindow(matrix, nodeByDegreeList.subList(startIndex, stopIndex + 1));
            if (k33nodes != null) {
                return k33nodes;
            }

            startIndex += nextStart;
            stopIndex += nextStart;
        }
        return null;
    }

    private static List<Integer> searchK5inWindow(RealMatrix matrix, List<Integer> window) {
        System.out.println("searchK5inWindow");
        ICombinatoricsVector<Integer> initialVector = Factory.createVector(window);

        Generator<Integer> gen = Factory.createSimpleCombinationGenerator(initialVector, 5);

        for (ICombinatoricsVector<Integer> combination : gen) {
            System.out.println("search: " + combination.getVector());
            boolean found;
            Djikstra djikstra = new Djikstra();
            List<Integer> otherCandidates = new LinkedList<>();
            List<Integer> excluded = new LinkedList<>();

            found = checkIfK5(matrix, combination.getVector(), djikstra,
                    otherCandidates, excluded);
            if (found)
                return combination.getVector();
        }

        return null;
    }


    private static boolean checkIfK5(RealMatrix matrix, List<Integer> candidates
            , Djikstra djikstra, List<Integer> otherCandidates, List<Integer> excluded) {
        otherCandidates.clear();
        excluded.clear();
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
                List<Integer> path = djikstra.findPathBetweenWithExclude(matrix.copy(),
                        candidates.get(i), candidates.get(j), excluded);
                if (path == null || path.isEmpty()) {
                    return false;
                }
                excluded.removeAll(otherCandidates);
                excluded.addAll(path);
            }
        }
        return true;
    }

    private static List<Integer> searchK33inWindow(RealMatrix matrix, List<Integer> window) {
        System.out.println("searchK33inWindow");
        ICombinatoricsVector<Integer> initialVector = Factory.createVector(window);

        Generator<Integer> gen = Factory.createSimpleCombinationGenerator(initialVector, 6);

        for (ICombinatoricsVector<Integer> combination : gen) {
            System.out.println("search: " + combination.getVector());
            boolean found;
            Djikstra djikstra = new Djikstra();
            List<Integer> otherCandidates = new LinkedList<>();
            List<Integer> excluded = new LinkedList<>();

            found = checkIfK33(matrix, combination.getVector(), djikstra,
                    otherCandidates, excluded);
            if (found)
                return combination.getVector();
        }
        return null;
    }

    private static boolean checkIfK33(RealMatrix matrix, List<Integer> candidates,
                                      Djikstra djikstra,
                                      List<Integer> otherCandidates,
                                      List<Integer> excluded) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 3; j < 6; ++j) {
                //direct connection
                if (matrix.getEntry(i, j) > 0) {
                    continue;
                }

                otherCandidates.clear();
                otherCandidates.addAll(candidates);
                otherCandidates.remove(j);
                otherCandidates.remove(i);
                excluded.addAll(otherCandidates);
                List<Integer> path = djikstra.findPathBetweenWithExclude(matrix.copy(),
                        candidates.get(i), candidates.get(j), excluded);
                if (path == null || path.isEmpty()) {
                    return false;
                }
                excluded.removeAll(otherCandidates);
                excluded.addAll(path);

            }
        }
        return true;
    }

    //helpers
    public static void printMatrix(RealMatrix matrix) {
        if (matrix != null) {
            for (int i = 0; i < matrix.getRowDimension(); ++i) {
                for (int j = 0; j < matrix.getColumnDimension(); ++j) {
                    System.out.print(matrix.getEntry(i, j) + " ");
                }
                System.out.println();
            }
        }
    }
}
