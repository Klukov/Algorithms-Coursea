import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

public class SAP {

    private final Digraph graph;
    private final Digraph reverse;
    private final int size;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.graph = new Digraph(G);
        this.reverse = G.reverse();
        this.size = this.graph.V();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v >= this.size || w >= this.size) { throw new java.lang.IllegalArgumentException(); }
        if (v == w) { return 0; }
        BreadthFirstDirectedPaths check1 = new BreadthFirstDirectedPaths(this.graph, v);
        BreadthFirstDirectedPaths check2 = new BreadthFirstDirectedPaths(this.graph, w);
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < this.graph.V(); i++) {
            if (check1.hasPathTo(i) && check2.hasPathTo(i)) {
                int dist = check1.distTo(i) + check2.distTo(i);
                if (dist < min) {
                    min = dist;
                }
            }
        }
		if (min == Integer.MAX_VALUE) {return -1;}
        return min;
}

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v >= this.size || w >= this.size) { throw new java.lang.IllegalArgumentException(); }
        if (v == w) { return v; }
        BreadthFirstDirectedPaths check1 = new BreadthFirstDirectedPaths(this.graph, v);
        BreadthFirstDirectedPaths check2 = new BreadthFirstDirectedPaths(this.graph, w);
        int min = Integer.MAX_VALUE;
        int common = -1;
        for (int i = 0; i < this.graph.V(); i++) {
            if (check1.hasPathTo(i) && check2.hasPathTo(i)) {
                int dist = check1.distTo(i) + check2.distTo(i);
                if (dist < min) {
                    min = dist;
                    common = i;
                }
            }
        }
        return common;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) { throw new java.lang.IllegalArgumentException(); }
        if (v == w ) {return 0;}
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(this.graph, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(this.graph, w);
        int dist = Integer.MAX_VALUE;
        for (int i=0; i<this.graph.V(); i++) {
            if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i)) {
                int check = bfs1.distTo(i) + bfs2.distTo(i);
                if (check < dist) {
                    dist = check;
                }
            }
        }
        if (dist == Integer.MAX_VALUE) {return -1;}
        return dist;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) { throw new java.lang.IllegalArgumentException(); }
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(this.graph, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(this.graph, w);
        int common = -1;
        int dist = Integer.MAX_VALUE;
        for (int i=0; i<this.graph.V(); i++) {
            if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i)) {
                int check = bfs1.distTo(i) + bfs2.distTo(i);
                if (check < dist) {
                    dist = check;
                    common = i;
                }
            }
        }
        return common;
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}