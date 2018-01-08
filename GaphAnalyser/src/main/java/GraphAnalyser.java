import Kuratowski.KuratowskiHelper;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class GraphAnalyser {

    public static final String SEPARATION_REGEX = " ";

    public static void main(String[] args) {

        //files from directory are analyzed and results are saved in logs.txt file
        String directory = "folderK5";
        String logFileName = "log.txt";
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
        GraphAnalyser graphAnalyser = GaphAnalysersFactory.create();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String filePath = listOfFiles[i].getPath();
                System.out.println(filePath);
                List<Integer> nodes;
                try {
                    long start = System.currentTimeMillis();
                    nodes = graphAnalyser.specifyPlanarity(filePath);
                    long elapsedTime = System.currentTimeMillis() - start;
                    saveResultInFile(generateMessage(nodes, filePath, elapsedTime), logFileName);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String generateMessage(List<Integer> nodes, String filename, long time) {
        boolean isPlanar = (nodes == null);
        String nodesStr = "";
        if (nodes != null) {
            for (Integer node : nodes) {
                nodesStr = nodesStr + node + " ";
            }
        }
        return isPlanar + " " + nodesStr + "\t" + filename + " time[ms]:" + time;
    }

    private static void saveResultInFile(String message, String logFileName) {
        try (FileWriter fw = new FileWriter(logFileName, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            System.out.println(message);
            out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> specifyPlanarity(String filePath) throws FileNotFoundException {
        RealMatrix matrix = readFile(filePath);
        if (matrix == null) {
            return null;
        }
        List<Integer> history = new LinkedList<>();
        matrix = optimalizeMatrix(matrix, history);
        if (matrix.getRowDimension() < 5) {
            return null;
        }

        List<Integer> kuratowskiNodes = KuratowskiHelper.findKuratowskiGraphK5(matrix);
        if (kuratowskiNodes == null) {
            kuratowskiNodes = KuratowskiHelper.findKuratowskiGraphK33(matrix);
        }

        //index from original matrix
        if (kuratowskiNodes != null) {
            for (int i = 0; i < kuratowskiNodes.size(); ++i) {
                kuratowskiNodes.set(i, history.get(kuratowskiNodes.get(i)));
            }
        }

        return kuratowskiNodes;
    }

    private RealMatrix optimalizeMatrix(RealMatrix matrix, List<Integer> history) {
        matrix = findBiggestComponent(matrix);
        matrix = removeSelfLoops(matrix);
        matrix = removeParallelEdges(matrix);
        matrix = removeZeroFirstAndSecondDegreeNodes(matrix, history);
        return matrix;
    }

    //finds biggest component in matrix and zeroes other connections (doesn't change mat size)
    private RealMatrix findBiggestComponent(RealMatrix matrix) {
        return BiggestComponentSearch.leaveOnlyBiggestComponent(matrix);
    }

    private RealMatrix removeZeroFirstAndSecondDegreeNodes(RealMatrix matrix, List<Integer> history) {
        for (int i = 0; i < matrix.getRowDimension(); ++i) {
            history.add(i);
        }
        return NodeRemover.removeFirstAndSecondDegree(matrix, history);
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
