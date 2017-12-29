package Djikstra;

public class Predecessor {

    private int id;
    private Predecessor predecesor;

    public Predecessor(int id) {
        this.id = id;
    }

    public void setPredecesor(Predecessor predecesor) {
        this.predecesor = predecesor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Predecessor getPredecesor() {
        return predecesor;
    }
}
