import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public class BaseballElimination {

    private Map<String,Integer> teams;
    private Map<Integer,String> teamsId;
    private int[] w; //wins
    private int[] l; //loss
    private int[] r; //left
    private int[][] g; // remaining matches


    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        if (filename == null) { throw new java.lang.IllegalArgumentException(); }
        In file = new In(filename);
        int size = Integer.parseInt(file.readLine());
        this.w = new int[size];
        this.l = new int[size];
        this.r = new int[size];
        this.g = new int[size][size];
        this.teams = new TreeMap<>();
        this.teamsId = new HashMap<>();

        int i = 0;
        while (!file.isEmpty()) {
            String[] newLine = file.readLine().trim().split("\\s++");
            if (newLine.length != 4+size) {throw new java.lang.IllegalArgumentException();}
            this.teams.put(newLine[0], i);
            this.teamsId.put(i, newLine[0]);
            this.w[i] = Integer.parseInt(newLine[1]);
            this.l[i] = Integer.parseInt(newLine[2]);
            this.r[i] = Integer.parseInt(newLine[3]);
            for (int j=0; j < size; j++) {
                g[i][j] = Integer.parseInt(newLine[4+j]);
                if (i == j){
                    if (g[i][j] != 0) { throw new java.lang.IllegalArgumentException(); }
                }
            }
            i++;
        }
        if (i != size) { throw new java.lang.IllegalArgumentException(); }
    }

    // number of teams
    public int numberOfTeams() {
        return teams.size();
    }

    // all teams
    public Iterable<String> teams() {
        return teams.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        if (team == null) { throw new java.lang.IllegalArgumentException(); }
        if (this.teams.containsKey(team)) {
            return w[this.teams.get(team)];
        } else { throw new java.lang.IllegalArgumentException(); }
    }

    // number of losses for given team
    public int losses(String team) {
        if (team == null) { throw new java.lang.IllegalArgumentException(); }
        if (this.teams.containsKey(team)) {
            return this.l[this.teams.get(team)];
        } else { throw new java.lang.IllegalArgumentException(); }
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (team == null) { throw new java.lang.IllegalArgumentException(); }
        if (this.teams.containsKey(team)) {
            return this.r[this.teams.get(team)];
        } else { throw new java.lang.IllegalArgumentException(); }
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if ((team1 == null) || (team2 == null)) { throw new java.lang.IllegalArgumentException(); }
        if (this.teams.containsKey(team1) && this.teams.containsKey(team2)) {
            return g[this.teams.get(team1)][this.teams.get(team2)];
        } else { throw new java.lang.IllegalArgumentException(); }
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (team == null) { throw new java.lang.IllegalArgumentException(); }
        if (!this.teams.containsKey(team)) { throw new java.lang.IllegalArgumentException(); }
        int index = this.teams.get(team);
        int n = this.teams.size();

        int possibleWins = this.w[index] + this.r[index];

        // create flowNetwork
        int layerFirstSize = (n-1)*(n-2)/2;
        int layerSecondSize = n;
        int v = 2+layerFirstSize+layerSecondSize;
        FlowNetwork network = new FlowNetwork(v);
        int start = v-2;
        int end = v-1;

        // numeration from the end
        // the reason is that the vertex of index i is the team of index i
        int iterator = 0;
        int sumW = 0;
        for (int i=0; i<n; i++) {
            if (i == index) { iterator++; continue; }
            if (possibleWins < this.w[i]) { return true; }
            sumW += this.w[i];
            network.addEdge(new FlowEdge(iterator,end, ( Math.max(possibleWins-this.w[i], 0) ) ));
            iterator++;
        }

        int maxRequiredFlow = 0;
        for (int i=0; i<n; i++) {
            if (i == index) { continue; }
            for (int j=i+1; j<n; j++) {
                if (j == index) { continue; }
                maxRequiredFlow += g[i][j];
                network.addEdge(new FlowEdge(start,iterator, this.g[i][j]));
                network.addEdge(new FlowEdge(iterator,i, Double.POSITIVE_INFINITY));
                network.addEdge(new FlowEdge(iterator,j, Double.POSITIVE_INFINITY));
                iterator++;
            }
        }
        if (n > 1) { if ((double)possibleWins < (double)((maxRequiredFlow+sumW)/(n-1))) { return true; } }

        // find max flow of the network and compare it with max out flow from the source
        FordFulkerson test = new FordFulkerson(network,start,end);
        if ((double)maxRequiredFlow == test.value()) {return false;}
        return true;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (team == null) { throw new java.lang.IllegalArgumentException(); }
        if (!this.teams.containsKey(team)) { throw new java.lang.IllegalArgumentException(); }
        int index = this.teams.get(team);
        int n = this.teams.size();
        Set<String> result = new TreeSet<>();

        int possibleWins = this.w[index] + this.r[index];

        // create flowNetwork
        int layerFirstSize = (n-1)*(n-2)/2;
        int layerSecondSize = n;
        int v = 2+layerFirstSize+layerSecondSize;
        FlowNetwork network = new FlowNetwork(v);
        int start = v-2;
        int end = v-1;

        // numeration from the end
        // the reason is that the vertex of index i is the team of index i
        int iterator = 0;
        for (int i=0; i<n; i++) {
            if (i == index) { iterator++; continue; }
            if (possibleWins < this.w[i]) {
                result.add(this.teamsId.get(i));
            }
            network.addEdge(new FlowEdge(i,end, ( Math.max((possibleWins-this.w[i]), 0) ) ));
            iterator++;
        }
        for (int i=0; i<n; i++) {
            if (i == index) { continue; }
            for (int j=i+1; j<n; j++) {
                if (j == index) { continue; }
                network.addEdge(new FlowEdge(start, iterator, this.g[i][j]));
                network.addEdge(new FlowEdge(iterator, i, Double.POSITIVE_INFINITY));
                network.addEdge(new FlowEdge(iterator, j, Double.POSITIVE_INFINITY));
                iterator++;
            }
        }

        FordFulkerson test = new FordFulkerson(network,start,end);
        for (int i=0; i<n; i++) {
            if (i == index) { continue; }
            if (test.inCut(i)) {
                result.add(this.teamsId.get(i));
            }
        }
        return result.isEmpty() ? null : result;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

}
