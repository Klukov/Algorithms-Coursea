import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Examples {

    public static void main(String[] args) {
        int n = 3;
        Percolation test = new Percolation(n);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (test.isOpen(p,q)) continue;
            test.open(p, q);
            for (int i=0; i < n; i++) {
                for (int j=0; j < n; j++) {
                    if (test.isOpen(i, j)) {
                        StdOut.print("*");
                    }
                    else {
                        StdOut.print("0");
                    }
                }
                StdOut.println();
            }
        }
    }

}
