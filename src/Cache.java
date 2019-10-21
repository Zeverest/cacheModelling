import java.util.LinkedList;

public abstract class Cache {
    protected int m;  // max capacity
    protected int n;  // total options
    protected LinkedList<Integer> cache;

    public Cache(int m, int n) {
        this.m = m;
        this.n = n;
    }

    // Returns true if cache hit else false
    public abstract int addToCache(int i);
}
