import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix
import org.paukov.combinatorics.CombinatoricsVector
import org.paukov.combinatorics.Generator
import org.paukov.combinatorics.ICombinatoricsVector
import org.paukov.combinatorics.combination.simple.SimpleCombinationGenerator
import spock.lang.Specification

import javax.xml.stream.FactoryConfigurationError

class GraphAnalyserTest extends Specification {

    private static String noneTypefilePath = "src\\test\\resources\\none.txt"
    private static String toReduceTypeFilePath = "src\\test\\resources\\toReduce.txt"
    private static String toReduceTypeFilePath1 = "src\\test\\resources\\toReduce1.txt"
    private static String almostK5FilePath = "src\\test\\resources\\almostK5.txt"
    private static String K5FilePath = "src\\test\\resources\\K5.txt"
    private GraphAnalyser analyser

    void setup() {
        analyser = GaphAnalysersFactory.create()
    }

    def "asd"() {
        given:
        ICombinatoricsVector<String> initialVector = new CombinatoricsVector<String>(["a","b","c","d"])
        Generator<String> gen = new SimpleCombinationGenerator<String>(initialVector, 3)
        for (ICombinatoricsVector<String> combination : gen) {
            System.out.println(combination.vector)
        }
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

    def "shuld optimalize graph to one cell"() {
        given:
        RealMatrix example = MatrixUtils.createRealMatrix(1, 1)
        RealMatrix input = analyser.readFile(toReduceTypeFilePath)
        when:
        RealMatrix output = analyser.optimalizeMatrix(input, new LinkedList<>())
        then:
        example.equals(output)
    }

    def "shuld optimalize graph"() {
        given:
        double[][] data = [[0, 1, 1, 1],
                           [1, 0, 1, 1],
                           [1, 1, 0, 1],
                           [1, 1, 1, 0]]
        RealMatrix example = MatrixUtils.createRealMatrix(data)
        RealMatrix input = analyser.readFile(toReduceTypeFilePath1)
        when:
        RealMatrix output = analyser.optimalizeMatrix(input, new LinkedList<>())
        then:
        example.equals(output)
    }

    def "should not detect planarity"() {
        when:
        List<Integer> output = analyser.specifyPlanarity(almostK5FilePath)
        then:
        output == null
    }

    def "should detect planarity"() {
        when:
        List<Integer> output = analyser.specifyPlanarity(K5FilePath)
        then:
        output == [0, 1, 2, 3, 4]
    }
}
