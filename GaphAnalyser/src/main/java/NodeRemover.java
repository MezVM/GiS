import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class NodeRemover {
    static RealMatrix removeFirstAndSecondDegree(RealMatrix matrix, List<Integer> history) {
        List<Integer> toDelete = new LinkedList<Integer>();
        do {
            toDelete.clear();
            int numCols = matrix.getColumnDimension();
            if (numCols == 1)
                return matrix;
            for (int columnIndex = 0; columnIndex < numCols; columnIndex++) {
                List<Integer> neightbours = nodeNeightbours(matrix, columnIndex);
                if (neightbours.size() == 2) {
                    toDelete.add(columnIndex);
                    Integer firstNeighbour = neightbours.get(0);
                    Integer secondNeighbour = neightbours.get(1);
                    matrix.setEntry(firstNeighbour, secondNeighbour, 1);
                    matrix.setEntry(secondNeighbour, firstNeighbour, 1);
                    break;
                }
                if (neightbours.size() <= 1) {
                    toDelete.add(columnIndex);
                    break;
                }
            }
            matrix = removeNodes(matrix, toDelete, history);
        } while (toDelete.size() != 0);
        return matrix;
    }

    private static RealMatrix removeNodes(RealMatrix matrix, List<Integer> toDelete, List<Integer> history) {
        int newSize = matrix.getRowDimension() - toDelete.size();
        List<List<Integer>> newRows = new LinkedList<List<Integer>>();
        int numRows = matrix.getRowDimension();
        int numCols = matrix.getColumnDimension();

        for (int i = 0; i < numRows; i++) {
            if (toDelete.contains(i))
                continue;
            List<Integer> newColumns = new LinkedList<Integer>();
            for (int j = 0; j < numCols; j++) {
                if (!toDelete.contains(j)) {
                    newColumns.add((int) matrix.getEntry(i, j));
                }
            }
            newRows.add(newColumns);
        }
        Collections.sort(toDelete);
        for (int deleteIndex = toDelete.size() - 1; deleteIndex >= 0; deleteIndex--) {
            history.remove(deleteIndex);
        }
        return convertToMatrix(newSize, newRows);
    }

    private static RealMatrix convertToMatrix(int newSize, List<List<Integer>> newRows) {
        RealMatrix newMatrix;
        if (newSize != 0)
            newMatrix = MatrixUtils.createRealMatrix(newSize, newSize);
        else
            newMatrix = MatrixUtils.createRealMatrix(1, 1);
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                newMatrix.setEntry(i, j, newRows.get(i).get(j));
            }
        }
        return newMatrix;
    }

    private static List<Integer> nodeNeightbours(RealMatrix matrix, int columnIndex) {
        int numRows = matrix.getRowDimension();
        List<Integer> neightobours = new LinkedList<Integer>();
        for (int i = 0; i < numRows; i++) {
            if (matrix.getEntry(i, columnIndex) > 0)
                neightobours.add(i);
        }
        return neightobours;
    }
}
