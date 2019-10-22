import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class CacheSimulator {

  private Queue<Arrival> arrivals;
  Cache cache;

  private double runLength;
  private double time;
  private int hitCount;
  private double warmUp;
  private int hit;

  public CacheSimulator(int runLength, int warmUp, int m, int n) {

    this.runLength = runLength;
    this.warmUp = warmUp;

    arrivals = new PriorityQueue<>(n, Comparator.comparingDouble(t -> t.time));
    cache = new FIFOCache(m, n);

    for(int i = 1; i <= n; i++) {
      scheduleNextArrival(i);
    }

    for(int i = 0; i < warmUp; i++) {
      simulateArrival();
    }
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
    return (runLength - hitCount) / (time - warmUp);
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


