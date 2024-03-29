import java.util.LinkedList;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FIFOCache extends Cache {

  public FIFOCache(int m) {
    super(m);
    cache = IntStream
        .rangeClosed(1, m).boxed().collect(Collectors.toCollection(LinkedList::new));
  }

  // Returns true if cache hit else false
  @Override
  public int addToCache(int i) {
    if(cache.contains(i)) return 1;
    cache.removeFirst();
    cache.addLast(i);
    return 0;
  }


}
