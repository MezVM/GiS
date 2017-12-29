package Djikstra;

public class Successor {

    private int id;
    private int cost;
    private boolean lock;

    public Successor(int id, int cost) {
        this.id = id;
        this.cost = cost;
        lock = false;
    }

    public int getId() {
        return id;
    }

    public int getCost() {
        return cost;
    }

    public void lock() {
        this.lock = true;
    }

    public boolean isLock() {
        return lock;
    }
}
