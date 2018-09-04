
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BoggleSolver
{
    private static final String ALPHABET_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int R = ALPHABET_STRING.length();
    private static HashMap<Character, Integer> ALPHABET;
    private Node root;

    {
        ALPHABET = new HashMap<>();
        int i = 0;
        for ( char c : ALPHABET_STRING.toCharArray()) {
            ALPHABET.put(c, i);
            i++;
        }
    }
    private static class Node {
        private boolean isWord;
        private Node[] next = new Node[R];
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null ) { throw new java.lang.IllegalArgumentException(); }
        for (String s : dictionary) {
            put(s.toUpperCase());
        }
    }
    private void put(String key) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
        this.root = put(this.root, key, 0);
    }

    private Node put(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.isWord = true;
            return x;
        }
        int index = ALPHABET.get(key.charAt(d));
        x.next[index] = put(x.next[index], key, d+1);
        return x;
    }

    private boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.isWord;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        int index = ALPHABET.get(key.charAt(d));
        return get(x.next[index], key, d+1);
    }

    private Node getByChar (Node x, char c) {
        if (x == null) return null;
        int index = ALPHABET.get(c);
        return x.next[index];
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> result = new HashSet<>();
        for (int col=0; col< board.cols(); col++) {
            for (int row=0; row<board.rows(); row++) {
                // string builder creation
                char c = board.getLetter(row,col);
                StringBuilder key = new StringBuilder(2);
                key.append(c);

                // get node corresponding to a given char
                Node node = getByChar(this.root,  c);

                // if there is no next node return null
                if ( node == null) {continue;}

                // marked matrix
                boolean[] marked = new boolean[board.cols()*board.rows()];
                marked [col+row*board.cols()] = true;

                // q-check
                if (c == 'Q') {
                    key.append('U');
                    node = getByChar(node, 'U');
                    if (node == null) { continue; }
                }

                // go forward to the next
                for (int i = -1; i < 2; i++) {
                    if ( (i+col < 0) || (i+col+1 > board.cols()) ) { continue;}
                    for (int j =-1; j < 2; j++) {
                        if (i==0 && j==0) { continue;}
                        if ( (j+row < 0) || (j+row+1) > board.rows() ) { continue; }
                        getStrings(result, marked, key, node, board, i+col, j+row);
                    }
                }
            }
        }
        return result;
    }

    private void getStrings(Set<String> result, boolean[] prevMarked, StringBuilder currentString, Node previousNode, BoggleBoard board, int col, int row ) {

        // check if puzzle is marked
        if (prevMarked[col+row*board.cols()]) {return;}
        boolean[] marked = Arrays.copyOf(prevMarked, prevMarked.length);
        marked[col+row*board.cols()] = true;

        // get the char
        char c = board.getLetter(row,col);

        // get node corresponding to a given char
        Node node = getByChar(previousNode,  c);

        // if there is no next node return
        if ( node == null) {return;}

        // create new key
        StringBuilder key = new StringBuilder(currentString.length()+2);
        key.append(currentString);
        key.append(c);

        // q-check
        if (c == 'Q') {
            key.append('U');
            node = getByChar(node, 'U');
            if (node == null) { return; }
        }

        if (node.isWord) {
            if (node.isWord) {
                if (key.length() > 2) {
                    result.add(key.toString());
                }
            }
        }

        // go forward to the next
        for (int i = -1; i < 2; i++) {
            if ( (i+col < 0) || (i+col+1 > board.cols()) ) { continue;}
            for (int j =-1; j < 2; j++) {
                if (i==0 && j==0) { continue;}
                if ( (j+row < 0) || (j+row+1) > board.rows() ) { continue; }
                getStrings(result, marked, key, node, board, i+col, j+row);
            }
        }
        return;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) { throw new IllegalArgumentException("argument to contains() is null"); }
        if (!contains(word.toUpperCase())) { return 0; }
        int size = word.length();
        if (size > 7) { return 11;}
        else if (size > 6) {return 5;}
        else if (size > 5) {return 3;}
        else if (size > 4) {return 2;}
        else if (size > 2) {return 1;}
        return 0;
    }

    // TEST
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}
