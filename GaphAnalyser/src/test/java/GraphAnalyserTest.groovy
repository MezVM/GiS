import org.ejml.simple.SimpleMatrix
import spock.lang.Specification
import spock.lang.Unroll

class GraphAnalyserTest extends Specification {

    private static String noneTypefilePath = "none.txt"
    private static String k5filePath = "none.txt"
    private static String k33filePath = "none.txt"
    private GraphAnalyser analyser

    void setup() {
        analyser = GaphAnalysersFactory.create()
    }

    def "shuld correctly load a file"() {
        when:
            File file = analyser.loadGraphFile(noneTypefilePath)
        then:
            file.size() != 0
    }

    def "should parse a matrix"() {
        given:
            File file = analyser.loadGraphFile(noneTypefilePath)
            double[][] data = [[2,0,0,0,0,0],
                               [0,2,0,0,0,0],
                               [0,0,1,0,0,0],
                               [0,0,0,1,0,0],
                               [0,0,0,0,1,0],
                               [0,0,0,0,0,1]]
            SimpleMatrix example = new SimpleMatrix(data)
        when:
            SimpleMatrix matrix = analyser.extractAdjacencyMatrix(file)
        then:
            matrix == example
    }

    def "should not detect planarity"() {
        when:
            boolean result = analyser.specifyPlanarity(noneTypefilePath)
        then:
            result
    }

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
