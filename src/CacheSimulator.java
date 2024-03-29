import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class CacheSimulator {

  private Queue<Arrival> arrivals;
  Cache cache;

  private double runLength;

  private int hitCount;
  private int hit;

  private double time;
  private double warmUpTime;

  public CacheSimulator(String cacheType, int runLength, int warmUpLength,
      int m,
      int n) {

    this.runLength = runLength;
    arrivals = new PriorityQueue<>(n, Comparator.comparingDouble(t -> t.time));

    if (cacheType.equals("FIFO")) {
      cache = new FIFOCache(m);
    } else if (cacheType.equals("RAND")) {
      cache = new RANDCache(m);
    } else {
      System.out.println("Unknown cache type! Terminating...");
      System.exit(1);
    }

    for(int i = 1; i <= n; i++) {
      scheduleNextArrival(i);
    }

    for(int i = 0; i < warmUpLength; i++) {
      simulateArrival();
    }

    warmUpTime = time;
  }

  /**
   *  We sample Uniform(0, 1), and then transform this value so that we have
   *  sampled Exp(1/k).
   */
  private void scheduleNextArrival(int k) {
    double u = Math.random();
    double t = - Math.log(u) * k;
    arrivals.add(new Arrival(this.time + t, k));
  }

  public double getHitRatio() {
    return hitCount / runLength;
  }

  public double getMissRate() {
    return (runLength - hitCount) / (time - warmUpTime);
  }

  public void simulateArrival() {
    Arrival a = arrivals.poll();
    time = a.time;
    scheduleNextArrival(a.value);
    hit = cache.addToCache(a.value);
  }

  public void run() {
    for (int i = 0; i < runLength; i++) {
      simulateArrival();
      hitCount += hit;
    }
  }

  private class Arrival {
    Double time;
    Integer value;

    Arrival(double time, int value) {
      this.time = time;
      this.value = value;
    }
  }
}


