import java.util.LinkedList;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FIFO {
  private int m;  // max capacity
  private int n;  // total options
  private LinkedList<Integer> cache;

  public FIFO(int m, int n) {
    this.m = m;
    this.n = n;
    cache = IntStream.rangeClosed(1, m).boxed().collect(Collectors.toCollection(LinkedList::new));
  }

  // Returns true if cache hit else false
  public int addToCache(int i) {
    if(cache.contains(i)) return 1;
    cache.removeFirst();
    cache.addLast(i);
    return 0;
  }


}
