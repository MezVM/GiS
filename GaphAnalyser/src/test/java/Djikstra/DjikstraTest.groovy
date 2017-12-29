package Djikstra

import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix
import spock.lang.Specification
import spock.lang.Unroll

class DjikstraTest extends Specification {

    private Djikstra djikstra

    void setup() {
        djikstra = new Djikstra()
    }

    def "should return path of simple graph"() {
        given:
            double[][] inputData = [[0, 1, 0, 0, 1, 0, 0],
                                    [1, 0, 1, 0, 0, 0, 0],
                                    [0, 1, 0, 1, 0, 0, 0],
                                    [0, 0, 1, 0, 0, 0, 1],
                                    [1, 0, 0, 0, 0, 1, 0],
                                    [0, 0, 0, 0, 1, 0, 1],
                                    [0, 0, 0, 1, 0, 1, 0]]
            RealMatrix input = MatrixUtils.createRealMatrix(inputData)
        when:
            List<Integer> output = djikstra.findPathBetween(input,0,3)
        then:
            output == [1,2]
    }

    def "should return path of graph with retreat situation"() {
        given:
            double[][] inputData = [[0, 1, 0, 1, 0, 0],
                                    [1, 0, 1, 0 ,0 ,0],
                                    [0, 1, 0, 1, 1, 0],
                                    [1, 0, 1, 0, 0, 0],
                                    [0, 0, 1, 0, 0, 1],
                                    [0, 0, 0, 0, 1, 0]]
            RealMatrix input = MatrixUtils.createRealMatrix(inputData)
        when:
            List<Integer> output = djikstra.findPathBetween(input,0,5)
        then:
            output == [1,2,4]
    }

    @Unroll
    def "should return path of graph with exclusion"() {
        given:
            double[][] inputData = [[0, 1, 0, 1, 0, 0 ,0, 0],
                                    [1, 0, 1, 0 ,0 ,0, 0, 0],
                                    [0, 1, 0, 1, 1, 0, 1, 0],
                                    [1, 0, 1, 0, 0, 0, 0, 0],
                                    [0, 0, 1, 0, 0, 1, 0, 0],
                                    [0, 0, 0, 0, 1, 0, 0, 1],
                                    [0, 0, 1, 0, 0, 0, 0, 1],
                                    [0, 0, 0, 0, 0, 1, 1, 0]]
            RealMatrix input = MatrixUtils.createRealMatrix(inputData)
        when:
            List<Integer> output = djikstra.findPathBetweenWithExclude(input,0,5,toExclude)
        then:
            output == expected
        where:
            toExclude   | expected
            [4]         | [1,2,6,7]
            [6]         | [1,2,4]
            [6,7]       | [1,2,4]
            [2]         | null
            [1]         | [3,2,4]
            [3,1]       | null
            [4,6]       | null
            [4,7]       | null
    }
}
