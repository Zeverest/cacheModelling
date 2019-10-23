import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RANDCache extends Cache {

    public RANDCache(int m) {
        super(m);
        cache = IntStream
            .rangeClosed(1, m).boxed().collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public int addToCache(int i) {
        if (cache.contains(i)) return 1;
        int index = (int) Math.floor(Math.random() * m);
        cache.remove(index);
        cache.add(index, i);
        return 0;
    }
}
