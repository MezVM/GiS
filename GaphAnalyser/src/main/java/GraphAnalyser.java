import org.ejml.simple.SimpleMatrix;

import java.io.*;

public class GraphAnalyser {

    public static void main(String[] args) {
        String filePath = "";
        GraphAnalyser graphAnalyser = GaphAnalysersFactory.create();
        boolean result = false;
        try {
            result = graphAnalyser.specifyPlanarity(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.print(result);
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
        //TODO
        return null;
    }

    private boolean findK33(SimpleMatrix matrix) {
        //TODO
        return false;
    }

    private boolean findK5(SimpleMatrix matrix) {
        //TODO
        return false;
    }

    private SimpleMatrix extractAdjacencyMatrix(File file) {
        //TODO
        return null;
    }

    private SimpleMatrix readFile(String filePath) throws FileNotFoundException {

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String stringCurrentLine;
            int size = 0;

            if((stringCurrentLine = br.readLine()) != null)
                size = Integer.parseInt(stringCurrentLine);
            SimpleMatrix matrix = new SimpleMatrix(size,size);
            int i = 0, j = 0;
            while ((stringCurrentLine = br.readLine()) != null) {
                String[] row = stringCurrentLine.split(" ");
                for (String element : row) {
                    int value = Integer.parseInt(element);
                    matrix.set(i,j,value);
                    j++;
                }
                i++;
                j = 0;
            }
            return matrix;
        } catch (IOException e) {
            throw new FileNotFoundException("Path: "+filePath);
        }
    }
}
