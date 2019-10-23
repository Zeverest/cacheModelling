import java.io.PrintStream;

/**
 * Generates <numObservations> observations from a cache simulation and
 * computes a point estimate and confidence interval for each hit ratio and
 * miss rate in the steady state (equilibrium). A warm-up allows the model
 * to reach equilibrium before measurements are made.

 * Only supports independent replications (runReplicatedSims).
**/

public class RunCacheSimulator {

    private Measure hitRatioMeasure = new Measure();
    private Measure missRateMeasure = new Measure();

    private int runLength, numObservations, warmUp, m, n;
    private String cacheType;

    public RunCacheSimulator(String cacheType, int m, int n, int runLength,
            int warmUp, int numObservations) {
        this.cacheType = cacheType;
        this.m = m;
        this.n = n;
        this.runLength = runLength;
        this.warmUp = warmUp;
        this.numObservations = numObservations;
    }

    private void addToMeasures(double hitRatio, double missRate) {
        hitRatioMeasure.addObservation(hitRatio);
        missRateMeasure.addObservation(missRate);
    }

    public void displayResults(PrintStream out) {
        double hitRatioSampleMean = hitRatioMeasure.sampleMean();
        double missRateSampleMean = missRateMeasure.sampleMean();
        if (numObservations == 1) {
            out.printf("%.4f\n", hitRatioSampleMean);
            out.printf("%.4f\n", missRateSampleMean);
        } else {
            double hitRatioHalfWidth = hitRatioMeasure.ciHalfWidth(95);
            double missRateHalfWidth = missRateMeasure.ciHalfWidth(95);
            out.printf("Hit ratio: %.4f, 95%% CI = (%.4f, %.4f)\n",
                hitRatioSampleMean,
                    hitRatioSampleMean - hitRatioHalfWidth, hitRatioSampleMean + hitRatioHalfWidth);
            out.printf("Miss rate: %.4f, 95%% CI = (%.4f, %.4f)\n",
                missRateSampleMean,
                    missRateSampleMean - missRateHalfWidth, missRateSampleMean + missRateHalfWidth);
        }
    }

    // Each run sets up a fresh cache.
    // There is a separate warm-up for each.
    public void runReplicatedSims() {
        for (int i = 0; i < numObservations; i++) {
            var cacheSimulator = new CacheSimulator(cacheType, runLength,
                warmUp, m, n);
            cacheSimulator.run();
            addToMeasures(cacheSimulator.getHitRatio(), cacheSimulator.getMissRate());
        }
    }

    // Usage: java ... RunCacheSimulator <m> <n> <runLength>
    // <warmup> <numObservations>
    public static void main(String[] args) {
        var sim = new RunCacheSimulator(
                args[0],
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4]),
                Integer.parseInt(args[5]));
        sim.runReplicatedSims();
        sim.displayResults(System.out);
    }
}


