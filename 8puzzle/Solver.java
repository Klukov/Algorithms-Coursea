import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Collections;

public final class Solver {
    private final int moves;
    private final ArrayList<Board> solution;
    private final boolean isSolvable;

    public Solver(Board initial) {                  // find a solution to the initial board (using the A* algorithm)
        if (initial == null) {throw new java.lang.IllegalArgumentException();}
        ArrayList<Board> solution = new ArrayList<Board>();
        //real parent
        MinPQ<Step> pq = new MinPQ<Step>();
        Step parentStep = new Step(initial);
        boolean isDoneReal = initial.isGoal();
        pq.insert(parentStep);
        // twin parent
        MinPQ<Step> pqTwin = new MinPQ<Step>();
        Step parentTwinStep = new Step(initial.twin());
        boolean isDoneTwin = parentTwinStep.board.isGoal();
        pqTwin.insert(parentTwinStep);
        // check if the case is solved
        boolean solved = isDoneReal || isDoneTwin;
        // find solution
        while (!solved) {
            // real parent part
            parentStep = pq.delMin();
            isDoneReal = parentStep.getBoard().isGoal();
            if (!isDoneReal) {
                for (Board i : parentStep.getBoard().neighbors()) {
                    if (parentStep.parent == null || (!i.equals(parentStep.getParent().getBoard())) ) {
                        pq.insert(new Step(i, parentStep, parentStep.getMoves()+1));
                    }
                }
            }
            // twin parent part
            if (!isDoneReal) {
                parentTwinStep = pqTwin.delMin();
                isDoneTwin = parentTwinStep.getBoard().isGoal();
                if (!isDoneTwin) {
                    for (Board i : parentTwinStep.getBoard().neighbors()) {
                        if ((parentTwinStep.parent == null) || (!i.equals(parentTwinStep.getParent().getBoard()))) {
                            pqTwin.insert(new Step(i, parentTwinStep, parentTwinStep.getMoves()+1));
                        }
                    }
                }
            }
            solved = isDoneReal || isDoneTwin;
        }
        // save solution if it exists
        if (isDoneReal) {
            this.moves = parentStep.getMoves();
            while (parentStep != null) {
                solution.add(parentStep.getBoard());
                parentStep = parentStep.getParent();
            }
            Collections.reverse(solution);
            this.solution = solution;
        }
        else {
            this.solution = null;
            this.moves = -1;
        }
        // save values
        this.isSolvable = isDoneReal;
    }

    private final class Step implements Comparable<Step> {
        private final Board board;
        private final Step parent;
        private final int moves;
        private final int cost;

        public Step(Board board, Step parent, int moves) {
            this.board = board;
            this.parent = parent;
            this.moves = moves;
            this.cost = board.manhattan() + this.moves;
        }
        public Step(Board board) {
            this(board, null, 0);
        }

        public Board getBoard() {
            return this.board;
        }

        public Step getParent() {
            return this.parent;
        }

        public int getMoves() {
            return this.moves;
        }

        @Override
        public int compareTo(Step other) {
            return (this.cost - other.cost);
        }
    }

    public boolean isSolvable() {                   // is the initial board solvable?
        return this.isSolvable;
    }

    public int moves() {                            // min number of moves to solve initial board; -1 if unsolvable
        return this.moves;
    }

    public Iterable<Board> solution() {             // sequence of boards in a shortest solution; null if unsolvable
        return this.solution;
    }

    public static void main(String[] args) {        // solve a slider puzzle (given below)
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
