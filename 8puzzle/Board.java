import edu.princeton.cs.algs4.StdRandom;
import java.util.LinkedList;

public final class Board {
    private final int n;
    private final int m;
    private final int[] board;
    private final int hamming;
    private final int manhattan;
    private final int zeroPosition;
    private Board twin;

    public Board(int[][] blocks) {              // construct a board from an n-by-n array of blocks
                                                // (where blocks[i][j] = block in row i, column j)
        this.n = blocks.length;
        this.m = this.n*this.n;
        this.board = new int [this.m];
        for (int i=0; i<n; i++) {
            for (int j=0; j<n; j++) {
                this.board[j+i*n] = blocks[i][j];
            }
        }
        int[] res = restConstructor();
        this.hamming = res[0];
        this.manhattan = res[1];
        this.zeroPosition = res[2];
        this.twin = null;
    }

    private Board (int[] blocks) {              // Create a board from 1D array, used only in class
        this.m = blocks.length;
        this.n = (int) Math.sqrt((double)m);
        this.board = new int [this.m];
        for (int i=0; i<this.m; i++) {
                this.board[i] = blocks[i];
        }
        int[] res = restConstructor();
        this.hamming = res[0];
        this.manhattan = res[1];
        this.zeroPosition = res[2];
        this.twin = null;
    }

    private int[] restConstructor() {
        int[] sol = new int[3];
        int hamming = 0;
        int manhattan = 0;
        int zeroPosition = this.m-1;
        for (int i=0; i<this.m; i++) {
            //finding zero
            if (this.board[i] == 0) { zeroPosition = i; }
            else {
                // Hamming
                if ((i+1) != this.board[i]) { hamming++; }
                // Manhattan
                // where the point is
                int x1 = (this.board[i]-1)/n;
                int y1 = (this.board[i]-1)%n;
                // where it should be
                int x2 = i/n;
                int y2 = i%n;
                manhattan += Math.abs(x1-x2) + Math.abs(y1-y2);
            }
        }
        sol[0] = hamming;
        sol[1] = manhattan;
        sol[2] = zeroPosition;
        return sol;
    }

    public int dimension() {                    // board dimension n
        return n;
    }

    public int hamming() {                      // number of blocks out of place
        return this.hamming;
    }

    public int manhattan() {                    // sum of Manhattan distances between blocks and goal
        return this.manhattan;
    }

    public boolean isGoal() {                   // is this board the goal board?
        if (this.hamming == 0) {return true;}
        return false;
    }

    public Board twin() {                       // a board that is obtained by exchanging any pair of blocks
        if (this.twin == null) {
            int x = 0;
            int y = 0;
            while (this.board[x]==this.board[y] || this.board[x]==0 || this.board[y]==0) {
                x = StdRandom.uniform(this.m);
                y = StdRandom.uniform(this.m);
            }
            int[] newBoard = this.board.clone();
            int temp = newBoard[x];
            newBoard[x] = newBoard[y];
            newBoard[y] = temp;
            this.twin = new Board(newBoard);
        }
        return this.twin;
    }

    public boolean equals(Object y) {           // does this board equal y?
        if (y == this) {return true;}
        if (y == null) {return false;}
        if (y.getClass() != this.getClass()) {return false;}
        Board other = (Board)y;
        if (other.dimension() != this.dimension()) {return false;}
        if (other.hamming() != this.hamming()) {return false;}
        if (other.manhattan() != this.manhattan()) {return false;}
        for (int i=0; i < this.m; i++) {
            if (other.board[i] != this.board[i]) {return false;}
        }
        return true;
    }

    public Iterable<Board> neighbors() {        // all neighboring boards
        LinkedList<Board> neighbors = new LinkedList<>();
        int x0 = (this.zeroPosition)/n;
        int y0 = (this.zeroPosition)%n;
        if (y0 != 0) {
            int[] newBoard = copyBoard();
            newBoard[this.zeroPosition] = newBoard[this.zeroPosition-1];
            newBoard[this.zeroPosition-1] = 0;
            neighbors.add(new Board(newBoard));
        }
        if (y0 != this.n-1) {
            int[] newBoard = copyBoard();
            newBoard[this.zeroPosition] = newBoard[this.zeroPosition+1];
            newBoard[this.zeroPosition+1] = 0;
            neighbors.add(new Board(newBoard));
        }
        if (x0 != 0) {
            int[] newBoard = copyBoard();
            newBoard[this.zeroPosition] = newBoard[this.zeroPosition-this.n];
            newBoard[this.zeroPosition-this.n] = 0;
            neighbors.add(new Board(newBoard));
        }
        if (x0 != this.n-1) {
            int[] newBoard = copyBoard();
            newBoard[this.zeroPosition] = newBoard[this.zeroPosition+this.n];
            newBoard[this.zeroPosition+this.n] = 0;
            neighbors.add(new Board(newBoard));
        }
        return neighbors;
    }

    private int[] copyBoard() {
        int[] newBoard = new int[this.m];
        for (int i=0; i<this.m; i++) {
            newBoard[i] = this.board[i];
        }
        return newBoard;
    }

    public String toString() {                  // string representation of this board (in the output format specified below)
        StringBuilder output = new StringBuilder();
        output.append(this.n + "\n");
        for (int i=0; i < this.n; i++ ) {
            for (int j=0; j<this.n; j++) {
                output.append(String.format("%2d ", this.board[j+i*this.n]));
            }
            output.append("\n");
        }
        return output.toString();
    }

    public static void main(String[] args) {    // unit tests (not graded)
        int n =4;
        int[][] array = new int[n][n];
        int[][] array2 = new int[n][n];

        for (int i=0; i<n; i++) {
            for (int j=0; j<n; j++) {
                array[i][j] = i*n +j+1;
                array2[i][j] = i*n +j+1;
            }
        }
        //array[n-1][n-1] = array[0][1];
        //array[0][1] = 0;
        Board board = new Board(array);
        Board boardnext = new Board(array2);
        System.out.println(board.equals(boardnext));
    }
}
