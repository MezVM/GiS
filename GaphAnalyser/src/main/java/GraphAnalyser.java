import org.ejml.simple.SimpleMatrix;

import java.io.File;
import java.io.FileNotFoundException;

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
        File file = loadGraphFile(filePath);
        SimpleMatrix matrix = extractAdjacencyMatrix(file);
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

    private File loadGraphFile(String filePath) {
        //TODO
        return null;
    }
}
