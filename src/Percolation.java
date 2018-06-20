import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF item;
    private WeightedQuickUnionUF fullCheck;
    private boolean[] grid;
    private int size;
    private int countOpen;

    public Percolation(int n) {                 // create n-by-n grid, with all sites blocked
        if (n <= 0) { throw new IllegalArgumentException("N have to be greater than 0"); }
        this.item = new WeightedQuickUnionUF((n*n));
        this.fullCheck = new WeightedQuickUnionUF((n*n));
        this.grid = new boolean[(n*n)];
        for (int i=0; i < (n*n); i++){
            this.grid[i] = false;
        }
        this.size = n;
        this.countOpen = 0;
        if (n > 1) {
            for (int i=1; i < n; i++) {
                this.item.union(0, i);
                this.fullCheck.union(0,i);
            }
            int lastRow = n*(n-1);
            for (int i=1; i < n; i++) {
                this.item.union(lastRow, lastRow + i);
            }
        }
    }

    public void open(int row, int col) {        // open site (row, col) if it is not open already
        if (this.isOpen(row, col)) {
            return;
        }

        int position = (row-1)*this.size + col-1;
        grid[position] = true;

        if (col != (this.size)) {
            if (this.grid[position + 1]) {
                this.item.union(position, position + 1);
                this.fullCheck.union(position, position + 1);
            }
        }
        if (col != 1) {
            if (this.grid[position - 1]) {
                this.item.union(position, position - 1);
                this.fullCheck.union(position, position - 1);
            }
        }
        if (row != (this.size)) {
            if (this.grid[position + size]) {
                this.item.union(position, position + size);
                this.fullCheck.union(position, position + size);
            }
        }

        if (row != 1) {
            if (this.grid[position - size]) {
                this.item.union(position, position - size);
                this.fullCheck.union(position, position - size);
            }
        }
        this.countOpen++;
    }

    public boolean isOpen(int row, int col) {   // is site (row, col) open?
        if (row > (this.size) || row <= 0 || col > (this.size) || col <= 0) {
            throw new IllegalArgumentException("invalid input");
        }
        return grid[(row-1)*this.size + col-1];
    }

    public boolean isFull(int row, int col) {   // is site (row, col) full?
        if (row > (this.size) || row <= 0 || col > (this.size) || col <= 0) {
            throw new IllegalArgumentException("invalid input");
        }
        return (grid[(row-1)*this.size + col-1] && this.fullCheck.connected(0,(row-1)*this.size + col-1));
    }

    public int numberOfOpenSites() {            // number of open sites
        return this.countOpen;
    }

    public boolean percolates()   {             // does the system percolate?
        if (size == 1) {
            return grid[0];
        }
        return this.item.connected(0, this.size*this.size-1);
    }

    public static void main(String[] args) {
        int n = 4;
        Percolation test = new Percolation(n);
        test.open(1,2);
        test.open(2,2);
        test.open(3,3);
        test.open(4,4);
        test.open(2,3);
        test.open(4,2);
        test.open(3,1);

        System.out.println(test.percolates());

        System.out.println();
        for (int i=1; i <= n; i++) {
            for (int j=1; j <= n; j++) {
                if (test.isOpen(i, j)) {
                    if (test.isFull(i,j)) {
                        StdOut.print("*");
                    }
                    else {
                        StdOut.print("+");
                    }
                }
                else {
                    StdOut.print("0");
                }
            }
            System.out.println();
        }
    }
}
