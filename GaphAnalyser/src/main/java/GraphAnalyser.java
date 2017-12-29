import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.*;

public class GraphAnalyser {

    public static final String SEPARATION_REGEX = " ";

    public static void main(String[] args) {
        String filePath = "";
        GraphAnalyser graphAnalyser = GaphAnalysersFactory.create();
        boolean result = false;
        try {
            result = graphAnalyser.specifyPlanarity(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean specifyPlanarity(String filePath) throws FileNotFoundException {
        RealMatrix matrix = readFile(filePath);
        matrix = optimalizeMatrix(matrix);
        boolean isFound;
        isFound = findK5(matrix);
        isFound = isFound || findK33(matrix);
        return !isFound;
    }

    private RealMatrix optimalizeMatrix(RealMatrix matrix) {
        matrix = removeSelfLoops(matrix);
        matrix = removeParallelEdges(matrix);
        matrix = removeSecondDegreeNodes(matrix);
        return matrix;
    }

    private RealMatrix removeSecondDegreeNodes(RealMatrix matrix) {
        return NodeRemover.removeFirstAndSecondDegree(matrix);
    }

    private RealMatrix removeParallelEdges(RealMatrix matrix) {
        int numRows = matrix.getRowDimension();
        int numCols = matrix.getColumnDimension();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if(matrix.getEntry(i,j) >1)
                    matrix.setEntry(i,j,1);
            }
        }
        return matrix;
    }

    private RealMatrix removeSelfLoops(RealMatrix matrix) {
        int numRows = matrix.getRowDimension();
        for (int i = 0; i < numRows; i++) {
            matrix.setEntry(i,i,0);
        }
        return matrix;
    }

    private boolean findK33(RealMatrix matrix) {
        //TODO
//        Djikstra.findPathBetween(matrix,0,1);
        return false;
    }

    private boolean findK5(RealMatrix matrix) {
        //TODO
        return false;
    }

    private RealMatrix readFile(String filePath) throws FileNotFoundException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            String stringCurrentLine;
            int size = 0;

            if ((stringCurrentLine = bufferedReader.readLine()) != null)
                size = Integer.parseInt(stringCurrentLine);
//            SimpleMatrix matrix = new SimpleMatrix(size, size);
            RealMatrix matrix = MatrixUtils.createRealMatrix(size,size);
            int i = 0, j = 0;
            while ((stringCurrentLine = bufferedReader.readLine()) != null) {
                for (String element : stringCurrentLine.split(SEPARATION_REGEX)) {
                    matrix.setEntry(i, j, Integer.parseInt(element));
                    j++;
                }
                i++;
                j = 0;
            }
            return matrix;
        } catch (IOException e) {
            throw new FileNotFoundException("Path: " + filePath);
        }
    }
}
