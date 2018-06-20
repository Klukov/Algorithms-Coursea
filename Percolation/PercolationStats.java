
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] solutions;
    private double mean;
    private double standardDerivation;
    private double condienceIntervalLow;
    private double condienceIntervalHigh;


    public PercolationStats(int n, int trials) {        // perform trials independent experiments on an n-by-n grid
        if (n <= 0) { throw new IllegalArgumentException("N have to be greater than 0"); }
        if (trials <= 0) { throw new IllegalArgumentException("The number of trials have to be greater than 0"); }
        this.solutions = new double [trials];
        for (int i = 0; i < trials; i++) {
            Percolation test = new Percolation(n);
            boolean opened = false;
            while (!opened) {
                int row = 1+StdRandom.uniform(n);
                int col = 1+StdRandom.uniform(n);
                while (test.isOpen(row, col)) {
                    row = 1+StdRandom.uniform(n);
                    col = 1+StdRandom.uniform(n);
                }
                test.open(row, col);
                opened = test.percolates();
            }
            solutions[i] = test.numberOfOpenSites()/((double)(n*n));
        }
        this.mean = StdStats.mean(this.solutions);
        this.standardDerivation = StdStats.stddev(this.solutions);
        this.condienceIntervalLow = this.mean - 1.96*this.standardDerivation/Math.sqrt((trials));
        this.condienceIntervalHigh = this.mean + 1.96*this.standardDerivation/Math.sqrt((trials));
    }
    public double mean() {                              // sample mean of percolation threshold
        return mean;
    }
    public double stddev() {                            // sample standard deviation of percolation threshold
        return standardDerivation;
    }
    public double confidenceLo() {                      // low  endpoint of 95% confidence interval
        return condienceIntervalLow;
    }
    public double confidenceHi() {                      // high endpoint of 95% confidence interval
        return condienceIntervalHigh;
    }

    public static void main(String[] args)  {           // test client (described below)
        int n = 10;
        int t = 10000;
        PercolationStats test = new PercolationStats(n, t);
        StdOut.println("Solution data: ");
        for (int i = 0; i < t; i++) {
            System.out.println(test.solutions[i]);
        }
        StdOut.println();
        StdOut.println("Mean value: " + test.mean());
        StdOut.println("Standard derivation: " + test.stddev());
    }

}
