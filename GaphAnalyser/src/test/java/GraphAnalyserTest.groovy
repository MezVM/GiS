import Kuratowski.KuratowskiHelper
import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll

class GraphAnalyserTest extends Specification {

    private static String noneTypefilePath = "src\\test\\resources\\none.txt"
    private static String toReduceTypeFilePath = "src\\test\\resources\\toReduce.txt"
    private static String toReduceTypeFilePath1 = "src\\test\\resources\\toReduce1.txt"
    private static String k5filePath = "none.txt"
    private static String k33filePath = "none.txt"
    private GraphAnalyser analyser

    void setup() {
        analyser = GaphAnalysersFactory.create()
    }

    def "shuld correctly load a file"() {
        given:
        double[][] data = [[2, 0, 0, 0, 0, 0],
                           [0, 2, 0, 0, 0, 0],
                           [0, 0, 1, 0, 0, 0],
                           [0, 0, 0, 1, 0, 0],
                           [0, 0, 0, 0, 1, 0],
                           [0, 0, 0, 0, 0, 1]]
        RealMatrix example = MatrixUtils.createRealMatrix(data)
        when:
        RealMatrix output = analyser.readFile(noneTypefilePath)
        then:
        example == output
    }

    def "shuld leave only biggest subgraph"() {
        given:
        double[][] inputData = [[1, 1, 1, 0, 0, 0, 0, 0],
                                [1, 1, 1, 0, 0, 0, 0, 0],
                                [1, 1, 1, 0, 0, 0, 0, 0],
                                [0, 0, 0, 1, 1, 1, 1, 1],
                                [0, 0, 0, 1, 1, 1, 1, 1],
                                [0, 0, 0, 1, 1, 1, 1, 1],
                                [0, 0, 0, 1, 1, 1, 1, 1],
                                [0, 0, 0, 1, 1, 1, 1, 1]]
        RealMatrix input = MatrixUtils.createRealMatrix(inputData)
        when:
        double[][] output = BiggestComponentSearch.leaveOnlyBiggestComponent(input).data
        then:
        output == [[0, 0, 0, 0, 0, 0, 0, 0],
                   [0, 0, 0, 0, 0, 0, 0, 0],
                   [0, 0, 0, 0, 0, 0, 0, 0],
                   [0, 0, 0, 1, 1, 1, 1, 1],
                   [0, 0, 0, 1, 1, 1, 1, 1],
                   [0, 0, 0, 1, 1, 1, 1, 1],
                   [0, 0, 0, 1, 1, 1, 1, 1],
                   [0, 0, 0, 1, 1, 1, 1, 1]]
    }

    @Ignore
    //TODO
    def "shuld optimalize graph to one cell"() {
        given:
        RealMatrix example = MatrixUtils.createRealMatrix(1, 1)
        RealMatrix input = analyser.readFile(toReduceTypeFilePath)
        when:
        RealMatrix output = analyser.optimalizeMatrix(input)
        then:
        example.equals(output)
    }

    @Ignore
    //TODO
    def "shuld optimalize graph"() {
        given:
        double[][] data = [[0, 1, 1, 1],
                           [1, 0, 1, 1],
                           [1, 1, 0, 1],
                           [1, 1, 1, 0]]
        RealMatrix example = MatrixUtils.createRealMatrix(data)
        RealMatrix input = analyser.readFile(toReduceTypeFilePath1)
        when:
        RealMatrix output = analyser.optimalizeMatrix(input)
        then:
        example.equals(output)
    }

    @Ignore
    //TODO
    def "should not detect planarity"() {
        when:
        boolean result = analyser.specifyPlanarity(noneTypefilePath)
        then:
        result
    }

    @Ignore
    //TODO
    @Unroll
    def "should detect planarity (#filePath)"() {
        when:
        boolean result = analyser.specifyPlanarity(filePath)
        then:
        !result
        where:
        filePath << [k5filePath, k33filePath]
    }
}
