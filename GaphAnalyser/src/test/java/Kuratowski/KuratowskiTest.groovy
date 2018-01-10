package Kuratowski

import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix
import spock.lang.Specification

class KuratowskiTest extends Specification {


    def "should return sorted list of nodes by degree"() {
        given:
        double[][] inputData = [[0, 1, 0, 1, 1, 1],
                                [1, 0, 1, 0, 0, 1],
                                [0, 1, 0, 1, 1, 1],
                                [1, 0, 1, 0, 0, 1],
                                [1, 0, 1, 0, 0, 1],
                                [1, 1, 1, 1, 1, 0]]
        RealMatrix input = MatrixUtils.createRealMatrix(inputData)
        when:
        List<Integer> output = KuratowskiHelper.getNodesByDegree(input)
        then:
        output == [5, 0, 2, 1, 3, 4]
    }

    def "should return list of Kuratowski K5 nodes sorted by degree"() {
        given:
        double[][] inputData = [[0, 1, 1, 1, 1, 1],
                                [1, 0, 1, 1, 1, 1],
                                [1, 1, 0, 1, 1, 1],
                                [1, 1, 1, 0, 1, 1],
                                [1, 1, 1, 1, 0, 1],
                                [1, 1, 1, 1, 1, 0]]
        RealMatrix input = MatrixUtils.createRealMatrix(inputData)
        when:
        List<Integer> output = KuratowskiHelper.findKuratowskiGraphK5(input)
        then:
        output == [0, 1, 2, 3, 4]
    }

    def "should return list of Kuratowski K33 nodes sorted by degree"() {
        given:
        double[][] inputData = [[0, 0, 0, 1, 1, 1, 0, 0, 0, 1],
                                [0, 0, 0, 1, 1, 1, 0, 0, 0, 1],
                                [0, 0, 0, 1, 1, 1, 0, 0, 0, 1],
                                [1, 1, 1, 0, 0, 0, 1, 0, 1, 0],
                                [1, 1, 1, 0, 0, 0, 1, 0, 1, 0],
                                [1, 1, 1, 0, 0, 0, 1, 0, 1, 0],
                                [0, 0, 0, 1, 1, 1, 0, 0, 0, 0],
                                [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                                [0, 0, 0, 1, 1, 1, 0, 0, 0, 0],
                                [1, 1, 1, 0, 0, 0, 0, 0, 0, 0]]
        RealMatrix input = MatrixUtils.createRealMatrix(inputData)
        when:
        List<Integer> output = KuratowskiHelper.findKuratowskiGraphK33(input)
        then:
        output == [3, 4, 5, 0, 1, 2]
    }

    def "should return null, there is not Kuratowski graph"() {
        given:
        double[][] inputData = [[0, 0, 1, 1, 1],
                                [0, 0, 1, 1, 1],
                                [1, 1, 0, 1, 1],
                                [1, 1, 1, 0, 1],
                                [1, 1, 1, 1, 0]]
        RealMatrix input = MatrixUtils.createRealMatrix(inputData)
        when:
        List<Integer> output = KuratowskiHelper.findKuratowskiGraphK5(input)
        then:
        output == null
    }
}
