
public class RANDCache extends Cache {


    public RANDCache(int m, int n) {
        super(m, n);
    }

    @Override
    public int addToCache(int i) {
        if (cache.contains(i)) return 1;
        int u = (int)Math.floor(Math.random()) * n;
        cache.remove(u);
        cache.add(u, i);
        return 0;
    }
}
