package Djikstra;

/**
 * helper Djikstra class
 */
public class Predecessor {

    private int id;
    private Predecessor predecesor;

    Predecessor(int id) {
        this.id = id;
    }

    public void setPredecesor(Predecessor predecesor) {
        this.predecesor = predecesor;
    }

    int id() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Predecessor getPredecesor() {
        return predecesor;
    }
}
