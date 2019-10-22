import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class CacheSimulator {

  private Queue<Arrival> arrivals;

  private double runLength;
  private double time;
  private int hitCount;
  private double warmUp;
  Cache cache;


  public CacheSimulator(int runLength, int warmUp, int m, int n) {

    this.runLength = runLength;
    this.warmUp = warmUp;

    arrivals = new PriorityQueue<>(n, Comparator.comparingDouble(t -> t.time));

    cache = new FIFOCache(m, n);

    for(int i = 0; i < n; i++) {
      scheduleNextArrival(i);
    }

    for(int i = 0; i < warmUp; i++) {
      simulateArrival();
    }
  }

  private void scheduleNextArrival(int k) {
    double u = Math.random();
    double t = -Math.log(u) * k; // Calculates next arrival time with Lk = 1 / k
    arrivals.add(new Arrival(this.time + t, k));
  }


  public double getHitRatio() {
    return hitCount / runLength;
  }

  public double getMissRate() {
    return (runLength - hitCount) / (time - warmUp);
  }

  public int simulateArrival() {
    Arrival a = arrivals.remove();
    time = a.time;
    scheduleNextArrival(a.value);
    return cache.addToCache(a.value);
  }

  public void run() {
    for (int i =0; i < runLength; i++) {
      hitCount += simulateArrival();
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


