import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class CacheSimulator {

  private Queue<Arrival> arrivals;

  private double totalSimTime;
  private double time;
  private int hitCount;
  private int numAccesses;
  private double warmUpTime;
  Cache cache;


  public CacheSimulator(int totalSimTime, int warmUpTime, int m, int n) {

    arrivals = new PriorityQueue<>(n, Comparator.comparingDouble(t -> t.time));

    for(int i = 0; i < n; i++) {
      scheduleNextArrival(i);
    }

    cache = new FIFOCache(m, n);

    // simulate arrivals until time T


  }

  private void scheduleNextArrival(int k) {
    double u = Math.random();
    double t = -Math.log(u) * k; // Calculates next arrival time with Lk = 1 / k
    arrivals.add(new Arrival(this.time + t, k));
  }



  public int getHitRatio() {
    return hitCount / numAccesses;
  }

  public double getMissRate() {
    return (numAccesses - hitCount) / time;
  }

  private void run() {
    for(int i = 0; i < totalSimTime; i++) {
      Arrival a = arrivals.remove();
      time = a.time;
      hitCount += cache.addToCache(a.value);
      scheduleNextArrival(a.value);
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


