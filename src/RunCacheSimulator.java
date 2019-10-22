import java.io.PrintStream;

//
// Generates <numObservations> observations from a cache simulation and
// computes a point estimate and confidence interval for each board location
// probability in the steady state (equilibrium). A warm-up allows the model
// to reach equilibrium before measurements are made.
//
// Supports independent replications (runReplicatedSims) and a single run with multiple
// batches (runOneSim).
//
// The system is ergodic, so the two approaches yield the same expected values
// for the steady-state probabilities.
//

public class RunCacheSimulator {
    private Measure hitRatioMeasure = new Measure();
    private Measure missRateMeasure = new Measure();
    private int runLength, numObservations, warmUp, m, n;

    public RunCacheSimulator(int runLength, int numObservations, int warmUp, int m, int n) {
        this.runLength = runLength;
        this.numObservations = numObservations;
        this.warmUp = warmUp;
        this.m = m;
        this.n = n;
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
            out.printf("hitRatio: %.4f, 95%% CI = (%.4f, %.4f)\n", hitRatioSampleMean,
                    hitRatioSampleMean - hitRatioHalfWidth, hitRatioSampleMean + hitRatioHalfWidth);
            out.printf("MissRate: %.4f, 95%% CI = (%.4f, %.4f)\n", missRateSampleMean,
                    missRateSampleMean - missRateHalfWidth, missRateSampleMean + missRateHalfWidth);
        }
    }

    // Each run sets up a fresh board.
    // There is a separate warm-up for each.
    public void runReplicatedSims() {
        for (int i = 0; i < numObservations; i++) {
            var cacheSimulator = new CacheSimulator(runLength, warmUp, m, n);
            cacheSimulator.run();

            addToMeasures(cacheSimulator.getHitRatio(), cacheSimulator.getMissRate());

        }
    }


    // Usage: java ... RunCacheSimulator <runLength> <numObservations> <warmup>
    public static void main(String[] args) {
        var sim = new RunCacheSimulator(Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4]));
        sim.runReplicatedSims();
        sim.displayResults(System.out);
    }
}


