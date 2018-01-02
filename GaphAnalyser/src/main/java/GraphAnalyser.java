import Kuratowski.KuratowskiHelper;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.*;
import java.util.List;

public class GraphAnalyser {

    public static final String SEPARATION_REGEX = " ";

    public static void main(String[] args) {
        //TODO-------------------------------------------------------------------------
        String filePath = "graph23_size-289_dens-0.19_K33.txt";
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

        List<Integer> kuratowskiNodes;

        kuratowskiNodes = KuratowskiHelper.findKuratowskiGraphK5(matrix);
        if (kuratowskiNodes != null) {
            System.out.println("Kuratowski graph K5 found. Nodes: ");
            KuratowskiHelper.printNodes(kuratowskiNodes);
        } else {
            kuratowskiNodes = KuratowskiHelper.findKuratowskiGraphK33(matrix);
            if (kuratowskiNodes != null) {
                System.out.println("Kuratowski graph K3,3 found. Nodes: ");
                KuratowskiHelper.printNodes(kuratowskiNodes);
            } else {
                System.out.println("Kuratowski graph NOT found");
            }
        }
        return false;
    }

    private RealMatrix optimalizeMatrix(RealMatrix matrix) {
        //TODO find najwieksza skaldowa spojna
        matrix = removeSelfLoops(matrix);
        matrix = removeParallelEdges(matrix);
        matrix = removeFirstAndSecondDegreeNodes(matrix);
        return matrix;
    }

    private RealMatrix removeFirstAndSecondDegreeNodes(RealMatrix matrix) {
        return NodeRemover.removeFirstAndSecondDegree(matrix);
    }

    private RealMatrix removeParallelEdges(RealMatrix matrix) {
        int numRows = matrix.getRowDimension();
        int numCols = matrix.getColumnDimension();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (matrix.getEntry(i, j) > 1)
                    matrix.setEntry(i, j, 1);
            }
        }
        return matrix;
    }

    private RealMatrix removeSelfLoops(RealMatrix matrix) {
        int numRows = matrix.getRowDimension();
        for (int i = 0; i < numRows; i++) {
            matrix.setEntry(i, i, 0);
        }
        return matrix;
    }


    private RealMatrix readFile(String filePath) throws FileNotFoundException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            String stringCurrentLine;
            int size = 0;

            if ((stringCurrentLine = bufferedReader.readLine()) != null)
                size = Integer.parseInt(stringCurrentLine);
//            SimpleMatrix matrix = new SimpleMatrix(size, size);
            RealMatrix matrix = MatrixUtils.createRealMatrix(size, size);
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
