import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class CacheSimulator {

  private Queue<Arrival> arrivals;
  private double time;
  private double hitCount;

  public void main(String[] args) {

    int m = Integer.parseInt(args[0]); // Size of cache
    int n = Integer.parseInt(args[1]); // Size of sample set
    int T = Integer.parseInt(args[2]); // Total Simulation Time

    arrivals = new PriorityQueue<>(n, Comparator.comparingDouble(t -> t.time));

    for(int i = 0; i < n; i++) {
      scheduleNextArrival(i);
    }

    FIFO cache = new FIFO(m, n);

    for(int i = 0; i < T; i++) {
      Arrival a = arrivals.remove();
      time = a.time;
      hitCount += cache.addToCache(a.value);
      scheduleNextArrival(a.value);
    }

    double pHit = hitCount / T;
    System.out.println("Miss rate =" + (1 - pHit));
  }

  private void scheduleNextArrival(int k) {
    double u = Math.random();
    double t = -Math.log(u) * k; // Calculates next arrival time with Lk = 1 / k
    arrivals.add(new Arrival(this.time + t, k));
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


