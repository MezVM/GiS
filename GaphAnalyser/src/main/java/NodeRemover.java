import org.ejml.simple.SimpleMatrix;

import java.util.LinkedList;
import java.util.List;

class NodeRemover {
    static SimpleMatrix removeFirstAndSecondDegree(SimpleMatrix matrix) {
        List<Integer> toDelete = new LinkedList<Integer>();
        do {
            toDelete.clear();
            int numCols = matrix.numCols();
            for (int columnIndex = 0; columnIndex < numCols; columnIndex++) {
                List<Integer> neightbours = nodeNeightbours(matrix, columnIndex);
                if (neightbours.size() == 2) {
                    toDelete.add(columnIndex);
                    Integer firstNeighbour = neightbours.get(0);
                    Integer secondNeighbour = neightbours.get(1);
                    matrix.set(firstNeighbour, secondNeighbour, 1);
                    matrix.set(secondNeighbour, firstNeighbour, 1);
                    break;
                }
                if (neightbours.size() <= 1) {
                    toDelete.add(columnIndex);
                    break;
                }
            }
            matrix = removeNodes(matrix, toDelete);
        } while (toDelete.size() != 0);
        return matrix;
    }

    private static SimpleMatrix removeNodes(SimpleMatrix matrix, List<Integer> toDelete) {
        int newSize = matrix.numRows()-toDelete.size();
        List<List<Integer>> newRows = new LinkedList<List<Integer>>();
        int numRows = matrix.numRows();
        int numCols = matrix.numCols();

        for (int i = 0; i < numRows; i++) {
            if(toDelete.contains(i))
                continue;
            List<Integer> newColumns = new LinkedList<Integer>();
            for (int j = 0; j < numCols; j++) {
                if(!toDelete.contains(j)) {
                    newColumns.add((int) matrix.get(i,j));
                }
            }
            newRows.add(newColumns);
        }
        return convertToMatrix(newSize, newRows);
    }

    private static SimpleMatrix convertToMatrix(int newSize, List<List<Integer>> newRows) {
        SimpleMatrix newMatrix = new SimpleMatrix(newSize,newSize);
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                newMatrix.set(i,j,newRows.get(i).get(j));
            }
        }
        return newMatrix;
    }

    private static List<Integer> nodeNeightbours(SimpleMatrix matrix, int columnIndex) {
        int numRows = matrix.numRows();
        List<Integer> neightobours = new LinkedList<Integer>();
        for (int i = 0; i < numRows; i++) {
            if(matrix.get(i, columnIndex)>0)
                neightobours.add(i);
        }
        return neightobours;
    }
}
