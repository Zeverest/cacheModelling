import java.util.LinkedList;

public abstract class Cache {
    protected int m;  // max capacity
    protected LinkedList<Integer> cache;

    public Cache(int m) {
        this.m = m;
    }

    // Returns true if cache hit else false
    public abstract int addToCache(int i);
}
