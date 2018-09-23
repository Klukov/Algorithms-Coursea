
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Iterator;
import java.util.LinkedList;

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> alphabet = new LinkedList<>();
        for (char i =0; i< 256; i++) {
            alphabet.add(i);
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
//            int position = 0;
//            Iterator<Character> iterator = alphabet.iterator();
//            while (iterator.hasNext()) {
//                if (iterator.next().equals(Character.valueOf(c))) {
//                    BinaryStdOut.write(position, 8);
//                    alphabet.remove(position);
//                    alphabet.addFirst(c);
//                }
//                position++;
//            }
            int index = alphabet.indexOf(c);
            BinaryStdOut.write(index, 8);
            alphabet.remove(index);
            alphabet.addFirst(c);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Integer> alphabet = new LinkedList<>();
        for (int i =0; i< 256; i++) {
            alphabet.add(i);
        }
        while (!BinaryStdIn.isEmpty()) {
            int c = alphabet.remove(BinaryStdIn.readChar());
            BinaryStdOut.write(c, 8);
            alphabet.addFirst(c);
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length == 0) {return;}
        if (args[0].equals("-")) {
            encode();
        }
        if (args[0].equals("+")) {
            decode();
        }
    }
}