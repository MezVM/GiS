package Kuratowski;


public class NodeDegree implements Comparable<NodeDegree> {
    int node; // node index
    int degree; // degree of node

    public NodeDegree(int node, int degree) {
        this.node = node;
        this.degree = degree;
    }

    @Override
    public int compareTo(NodeDegree secondNode) {
        return secondNode.degree - this.degree;
    }
}
