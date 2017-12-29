package Djikstra;

public class Successor {

    private int id;
    private int cost;
    private boolean lock;

    Successor(int id) {
        this.id = id;
        this.cost = Integer.MAX_VALUE;
        lock = false;
    }

    Successor(int id, int cost) {
        this.id = id;
        this.cost = cost;
        lock = false;
    }

    int id() {
        return id;
    }

    void addCost(int newCost) {
        if (cost == Integer.MAX_VALUE)
            cost = 0;
        cost += newCost;
    }

    public int cost() {
        return cost;
    }

    public void lock() {
        this.lock = true;
    }

    public boolean isLock() {
        return lock;
    }
}
