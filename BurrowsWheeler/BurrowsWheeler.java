
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;
import java.util.HashMap;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {

        // read string and create circular suffix array
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);

        for (int i=0; i< s.length(); i++) {
            int check = csa.index(i);
            if (check == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i=0; i< s.length(); i++) {
            int id = csa.index(i)-1;
            if (id < 0) { id = s.length()-1; }
            BinaryStdOut.write(s.charAt(id));
        }

        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {

        // read int and string
        int firstIndex = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();

        // create new sorted array from s and sort it
        // moreover it creates array of first char indexes in sorted array
        char[] sortedString = s.toCharArray();
        int[] count = new int[256];
        int[] index = new int[256]; Arrays.fill(index, -1);
        for (int i=0; i<sortedString.length; i++) {
            count[sortedString[i]]++;
        }
        int iterator = 0;
        for (int i=0; i<count.length; i++) {
            for (int j=0; j<count[i]; j++) {
                sortedString[iterator] = (char) i;
                if (j == 0) {
                    index[sortedString[iterator]] = iterator;
                }
                iterator++;
            }
        }

        // create next[] array
        count = new int[256];
        int[] next = new int[s.length()];
        for (int i=0; i < s.length(); i++) {
            int sortedIndex = index[s.charAt(i)] + count[s.charAt(i)];
            next[sortedIndex] = i;
            count[s.charAt(i)]++;
        }

        // output
        for (int i=0; i < sortedString.length; i++) {
            BinaryStdOut.write(sortedString[firstIndex],8);
            firstIndex = next[firstIndex];
        }

        BinaryStdOut.close();
    }

    private static void specialCharSorting (char[] input) {
        int[] count = new int[256];
        for (int i=0; i<input.length; i++) {
            count[input[i]]++;
        }
        int iterator = 0;
        for (int i=0; i<count.length; i++) {
            for (int j=0; j<count[i]; j++) {
                input[iterator] = (char) i;
                iterator++;
            }
        }
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args.length == 0) {return;}
        if (args[0].equals("-")) {
            transform();
        }
        if (args[0].equals("+")) {
            inverseTransform();
        }
    }
}
