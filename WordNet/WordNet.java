import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {

    private Digraph graph;
    private HashMap<Integer, String> synsetData = new HashMap<>();
    private HashMap<String, ArrayList<Integer>> nouns = new HashMap<>();

    private SAP sap;
    private int size;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) { throw new java.lang.IllegalArgumentException(); }
        In synsetInput = new In(synsets);
        In hypernymsInput = new In(hypernyms);

        // read the synset
        while (!synsetInput.isEmpty()) {
            String[] data = synsetInput.readLine().toString().split(",");
            int newInt = Integer.parseInt(data[0]);
            this.synsetData.put(newInt, data[1]);
            String[] strings = data[1].split(" ");
            for (String i : strings) {
                if (!this.nouns.containsKey(i)) {
                    ArrayList<Integer> newList = new ArrayList<Integer>();
                    newList.add(newInt);
                    this.nouns.put(i, newList);
                }
                else {
                    ArrayList<Integer> oldList = this.nouns.get(i);
                    oldList.add(newInt);
                    this.nouns.put(i, oldList);
                }
            }
        }

        // set variables
        this.size = this.synsetData.size();
        this.graph = new Digraph(this.size);

        //read the hypernyms
        while (!hypernymsInput.isEmpty()) {
            String[] data = hypernymsInput.readLine().toString().split(",");
            for (int i=1; i < data.length; i++) {
                this.graph.addEdge(Integer.parseInt(data[0]), Integer.parseInt(data[i]));
            }
        }
		
		//check for cycles
        DirectedCycle cycle = new DirectedCycle(this.graph);
        if (cycle.hasCycle()) { throw new java.lang.IllegalArgumentException(); }
		
        // check roots
        int roots = 0;
        for (int i = 0; i < this.graph.V(); i++) {
            if (this.graph.outdegree(i) == 0) { roots++; }
        }
        if (roots != 1) { throw new java.lang.IllegalArgumentException(); }
		
        //create a graph
        this.sap = new SAP(this.graph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) { throw new java.lang.IllegalArgumentException(); }
        return this.nouns.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) { throw new java.lang.IllegalArgumentException(); }
        if (!(this.isNoun(nounA) && this.isNoun(nounB))) {
            throw new java.lang.IllegalArgumentException();
        }
        if (nounA.equals(nounB)) {return 0;}
        return sap.length(this.nouns.get(nounA), this.nouns.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) { throw new java.lang.IllegalArgumentException(); }
        if (!(this.isNoun(nounA) && this.isNoun(nounB))) {
            throw new java.lang.IllegalArgumentException();
        }
        int i = sap.ancestor(this.nouns.get(nounA), this.nouns.get(nounB));
        return this.synsetData.get(i);
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}