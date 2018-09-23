
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CircularSuffixArray {

    private List<Integer> result;
    private String str;

    private class Node implements Comparable<Node> {
        private int index;
        private char c;

        private Node (char c, int index) {
            this.index = index;
            this.c = c;
        }
        @Override
        public int compareTo(Node that) {
            if (this.c > that.c) {return 1;}
            if (this.c < that.c) {return -1;}
            int i = 1;
            while (str.charAt((this.index +i) % str.length()) == str.charAt( (that.index +i) % str.length())) {
                i++;
                if (i == str.length()) {return 1;}
            }
            return (str.charAt((this.index +i) % str.length()) > str.charAt((that.index +i) % str.length())) ? 1 : -1;
        }
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        this.str = s;
        if (s == null ) { throw new java.lang.IllegalArgumentException(); }
        if (s.length() == 0) {this.result = new ArrayList<>(); return;}
        if (s.length() == 1) {this.result = new ArrayList<>(); this.result.add(0); return;}
        Set<Node> solver = new TreeSet<>();
        for (int i =0; i < s.length(); i++) {
            solver.add(new Node(s.charAt(i), i));
        }
        this.result = new ArrayList<>();
        for (Node i : solver) {
            this.result.add(i.index);
        }
    }

    // length of s
    public int length() {
        return this.str.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > this.str.length()-1 ) { throw new java.lang.IllegalArgumentException(); }
        return this.result.get(i);
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray test = new CircularSuffixArray(args[0]);
        for (int i=0; i< test.length(); i++) {
            System.out.println(test.index(i));
        }
    }

}
