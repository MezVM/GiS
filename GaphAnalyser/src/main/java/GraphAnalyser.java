import org.ejml.simple.SimpleMatrix;

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
        SimpleMatrix matrix = readFile(filePath);
        matrix = optimalizeMatrix(matrix);
        boolean isFound;
        isFound = findK5(matrix);
        isFound = isFound || findK33(matrix);
        return !isFound;
    }

    private SimpleMatrix optimalizeMatrix(SimpleMatrix matrix) {
        matrix = removeSelfLoops(matrix);
        matrix = removeParallelEdges(matrix);
        matrix = removeSecondDegreeNodes(matrix);
        return matrix;
    }

    private SimpleMatrix removeSecondDegreeNodes(SimpleMatrix matrix) {
        return NodeRemover.removeFirstAndSecondDegree(matrix);
    }

    private SimpleMatrix removeParallelEdges(SimpleMatrix matrix) {
        int numRows = matrix.numRows();
        int numCols = matrix.numCols();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if(matrix.get(i,j) >1)
                    matrix.set(i,j,1);
            }
        }
        return matrix;
    }

    private SimpleMatrix removeSelfLoops(SimpleMatrix matrix) {
        int numRows = matrix.numRows();
        for (int i = 0; i < numRows; i++) {
            matrix.set(i,i,0);
        }
        return matrix;
    }

    private boolean findK33(SimpleMatrix matrix) {
        //TODO
        return false;
    }

    private boolean findK5(SimpleMatrix matrix) {
        //TODO
        return false;
    }

    private SimpleMatrix readFile(String filePath) throws FileNotFoundException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            String stringCurrentLine;
            int size = 0;

            if ((stringCurrentLine = bufferedReader.readLine()) != null)
                size = Integer.parseInt(stringCurrentLine);
            SimpleMatrix matrix = new SimpleMatrix(size, size);

            int i = 0, j = 0;
            while ((stringCurrentLine = bufferedReader.readLine()) != null) {
                for (String element : stringCurrentLine.split(SEPARATION_REGEX)) {
                    matrix.set(i, j, Integer.parseInt(element));
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
