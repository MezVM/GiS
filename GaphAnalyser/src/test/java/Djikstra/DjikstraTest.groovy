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
            List<Integer> output = djikstra.findPathBetweenWithExclude(input,start,stop,toExclude)
        then:
            output == expected
        where:
            toExclude   | expected  | start | stop
            [4]         | [1,2,6,7] | 0     | 5
            [6]         | [1,2,4]   | 0     | 5
            [6,7]       | [1,2,4]   | 0     | 5
            [2]         | null      | 0     | 5
            [1]         | [3,2,4]   | 0     | 5
            [3,1]       | null      | 0     | 5
            [4,6]       | null      | 0     | 5
            [4,7]       | null      | 0     | 5
            [2]         | []        | 5     | 4
            [2]         | []        | 4     | 4
            [0]         | [2]       | 3     | 1
            [1,6]       | [4,2,3]   | 5     | 0
    }
}
