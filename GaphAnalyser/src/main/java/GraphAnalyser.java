import Kuratowski.KuratowskiHelper;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main class
 * Analyse graph's planarity by searching Kuratowski subgraphs K5 or K33
 * Input in DIRECTORY folder {@link #DIRECTORY} - all graphs in this folder will be analysed
 * Results saved in LOG_FILE_NAME {@link #LOG_FILE_NAME}
 */
public class GraphAnalyser {
    private static final String DIRECTORY = "Gnone";
    private static final String LOG_FILE_NAME = "lognone_w10_bezOptymalizacji.txt";

    private static final String SEPARATION_REGEX = " ";

    public static void main(String[] args) {
        File folder = new File(DIRECTORY);
        File[] listOfFiles = folder.listFiles();
        GraphAnalyser graphAnalyser = GaphAnalysersFactory.create();

        if (listOfFiles == null) {
            System.out.println("No files to test");
        }

        for (File file : listOfFiles) {
            if (file.isFile()) {
                String filePath = file.getPath();
                System.out.println(filePath);
                List<Integer> nodes = null;
                boolean exceptionOccured = false;
                long elapsedTime = 0;
                try {
                    long start = System.currentTimeMillis();
                    nodes = graphAnalyser.specifyPlanarity(filePath);
                    elapsedTime = System.currentTimeMillis() - start;
                } catch (FileNotFoundException | NumberFormatException e) {
                    e.printStackTrace();
                    exceptionOccured = true;
                } finally {
                    if(!exceptionOccured){
                        saveResultInFile(generateMessage(nodes, filePath, elapsedTime), LOG_FILE_NAME);
                    }
                }
            }
        }
    }

    /**
     * returns string with info about graph:
     * "K5/K33/NONE  size  density  time  file_name  kuratowski_nodes"
     *
     * @param nodes    kuratowski graph nodes; according to is's size "K5","K33" or "NONE" is written to file
     * @param filename should contains: size-[0-9]+_ and dens-.+_
     *                 size and density are NOT counted in algorithm, this info should be included in file name
     * @param time
     */
    private static String generateMessage(List<Integer> nodes, String filename, long time) {
        //"K5" or "K33" or "NONE"
        String kuratowskiFound;
        if (nodes == null || nodes.isEmpty()) {
            kuratowskiFound = "NONE";
        } else if (nodes.size() == 5) {
            kuratowskiFound = "K5";
        } else if (nodes.size() == 6) {
            kuratowskiFound = "K33";
        } else {
            kuratowskiFound = "ERROR";
        }

        //size
        //graph23_size-289_dens-0.19_K33
        String size = "";
        Pattern p = Pattern.compile("size-[0-9]+_");
        Matcher m = p.matcher(filename);
        if (m.find()) {
            size = m.group(0).substring(5, m.group(0).length() - 1);
        }

        //density
        //graph23_size-289_dens-0.19_K33
        String density = "";
        Pattern p2 = Pattern.compile("dens-.+_");
        Matcher m2 = p2.matcher(filename);
        if (m2.find()) {
            density = m2.group(0).substring(5, m2.group(0).length() - 1);
        }

        //kuratowski nodes
        String nodesStr = "";
        if (nodes != null) {
            for (Integer node : nodes) {
                nodesStr = nodesStr + node + " ";
            }
        }

        return kuratowskiFound + "\t" + size + "\t" + density + "\t" + time
                + "\t" + filename
                + "\t" + nodesStr
                ;
    }

    /**
     * Saves message in logFileName
     *
     * @param message
     * @param logFileName
     */
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

    /**
     * returns list of Kuratowski subgraph's nodes in matrix from file,
     * or empty list or null in case of Kuratowski not found
     *
     * @param filePath file must contains adjacency matrix
     *                 in first line must be number of nodes, next lines are rows ; example:
     *                 3
     *                 0 1 1
     *                 1 0 0
     *                 1 0 0
     * @return list of Kuratowski nodes
     * @throws FileNotFoundException
     * @throws NumberFormatException
     */
    public List<Integer> specifyPlanarity(String filePath) throws FileNotFoundException, NumberFormatException {
        System.out.println("specifyPlanarity");
        RealMatrix matrix;
        try {
            matrix = readFile(filePath);
        } catch (FileNotFoundException | NumberFormatException e) {
            throw e;
        }
        List<Integer> history = new LinkedList<>();
//        matrix = optimalizeMatrix(matrix, history);
        if (matrix.getRowDimension() < 5) {
            return null;
        }

        List<Integer> kuratowskiNodes = KuratowskiHelper.findKuratowskiGraphK5(matrix);
        if (kuratowskiNodes == null) {
            kuratowskiNodes = KuratowskiHelper.findKuratowskiGraphK33(matrix);
        }

        //index from original matrix
        if (kuratowskiNodes != null && !history.isEmpty()) {
            for (int i = 0; i < kuratowskiNodes.size(); ++i) {
                kuratowskiNodes.set(i, history.get(kuratowskiNodes.get(i)));
            }
        }

        return kuratowskiNodes;
    }

    /**
     * optimalize matrix
     *
     * @param matrix  matrix to be optimalized
     * @param history (return in param)
     *                while optimalizing some of matrix's nodes may be deleted,
     *                what indicates that index may be changed.
     *                history list contains info of previous node index:
     *                history[present_index] = previous_index
     * @return optimalized matrix, may be smaller
     */
    private RealMatrix optimalizeMatrix(RealMatrix matrix, List<Integer> history) {
        matrix = findBiggestComponent(matrix);
        matrix = removeSelfLoops(matrix);
        matrix = removeParallelEdges(matrix);
        matrix = removeZeroFirstAndSecondDegreeNodes(matrix, history);
        return matrix;
    }

    //finds biggest component in matrix and zeroes other connections (doesn't change mat size)

    /**
     * finds biggest connectivity, deletes connection (edges) in other
     *
     * @param matrix
     * @return matrix
     */
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


    private RealMatrix readFile(String filePath) throws FileNotFoundException, NumberFormatException {
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
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Path: " + filePath);
        }
    }
}
