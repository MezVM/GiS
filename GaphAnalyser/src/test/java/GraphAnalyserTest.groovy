import org.ejml.data.DenseMatrix64F
import org.ejml.simple.SimpleMatrix
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll

class GraphAnalyserTest extends Specification {

    private static String noneTypefilePath = "src\\test\\resources\\none.txt"
    private static String k5filePath = "none.txt"
    private static String k33filePath = "none.txt"
    private GraphAnalyser analyser

    void setup() {
        analyser = GaphAnalysersFactory.create()
    }

    def "shuld correctly load a file"() {
        given:
            double[][] data = [[2,0,0,0,0,0],
                               [0,2,0,0,0,0],
                               [0,0,1,0,0,0],
                               [0,0,0,1,0,0],
                               [0,0,0,0,1,0],
                               [0,0,0,0,0,1]]
            SimpleMatrix example = new SimpleMatrix(data)
        when:
            SimpleMatrix output = analyser.readFile(noneTypefilePath)
        then:
            compareMatrixes(example, output)
    }

    @Ignore //TODO
    def "should not detect planarity"() {
        when:
            boolean result = analyser.specifyPlanarity(noneTypefilePath)
        then:
            result
    }

    @Ignore //TODO
    @Unroll
    def "should detect planarity (#filePath)"() {
        when:
            boolean result = analyser.specifyPlanarity(filePath)
        then:
            !result
        where:
            filePath << [k5filePath, k33filePath]
    }

    //Z jakiegos glupiego powodu na SimpleMatrix ani na DensMatrix64d nie działa == i .equals()
    //trzeba szpachlować
    private boolean compareMatrixes(SimpleMatrix m1, SimpleMatrix m2) {
        if(m1.numRows() != m2.numRows())
            return false
        if(m1.numCols() != m2.numCols())
            return false
        int numRows = m1.numRows()
        int numCols = m1.numCols()
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                double m1val = m1.get(i,j)
                double m2val = m2.get(i,j)
                if(m1val !=  m2val)
                    return false
            }
        }
        return true
    }
}
