import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first;
    private Node<Item> last;
    private int size;

    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> previous;

        private Node(Item item, Node<Item> next, Node<Item> previous) {
            this.item = item;
            this.next = next;
            this.previous = previous;
        }
        private Node(Item item) {
            this( item, null, null);
        }
        private Node() {
            this( null, null, null);
        }
    }

    private class DequeIterator implements Iterator<Item> {
        private Node<Item> check = first;
        public boolean hasNext() { return check != null; }
        public Item next() {
            if (!hasNext()) { throw new java.util.NoSuchElementException(); }
            Item item = check.item;
            check = check.next;
            return item;
        }
        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    public Deque() {                            // construct an empty deque
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    public boolean isEmpty() {                  // is the deque empty?
        return (this.size == 0);
    }

    public int size() {                         // return the number of items on the deque
        return this.size;
    }

    public void addFirst(Item item) {           // add the item to the front
        if (item == null) { throw new java.lang.IllegalArgumentException(); };
        Node<Item> oldFirst = this.first;
        if (this.size == 0) {
            this.first = new Node<Item>(item, null, null);
            this.last = this.first;
        }
        else {
            this.first = new Node<Item>(item, oldFirst, null);
            oldFirst.previous = this.first;
        }
        this.size++;
    }

    public void addLast(Item item) {            // add the item to the end
        if (item == null) { throw new java.lang.IllegalArgumentException(); };
        Node<Item> oldLast = this.last;
        if (size == 0) {
            this.last = new Node<Item>(item, null, null);
            this.first = this.last;
        }
        else {
            this.last = new Node<Item>(item, null, oldLast);
            oldLast.next = this.last;
        }
        this.size++;
    }

    public Item removeFirst() {                 // remove and return the item from the front
        if (this.size == 0) { throw new java.util.NoSuchElementException(); }
        Item remove = this.first.item;
        if (this.size == 1) {
            this.first = null;
            this.last = this.first;
        }
        else {
            this.first = this.first.next;
            this.first.previous = null;
        }
        this.size--;
        return remove;
    }

    public Item removeLast() {                  // remove and return the item from the end
        if (this.size == 0) { throw new java.util.NoSuchElementException(); }
        Item remove = this.last.item;
        if (this.size == 1) {
            this.last = null;
            this.first = this.last;
        }
        else {
            this.last = this.last.previous;
            this.last.next = null;
        }
        this.size--;
        return remove;
    }

    public Iterator<Item> iterator() {          // return an iterator over items in order from front to end
        return new DequeIterator();
    }

    public static void main(String[] args) {    // unit testing (optional)
        Deque<Integer> arr = new Deque<Integer>();
        arr.addFirst(1);
        arr.addFirst(2);
        arr.addFirst(3);
        arr.addLast(44);
        StdOut.println("The size: " + arr.size());
        StdOut.println(arr.removeFirst());
        StdOut.println(arr.removeLast());
        StdOut.println(arr.removeLast());
        StdOut.println(arr.removeLast());
        arr.addFirst(111);
        arr.addFirst(112);
        arr.addFirst(113);
        arr.addLast(1144);
        StdOut.println(arr.removeLast());
        StdOut.println(arr.removeLast());
        StdOut.println(arr.removeLast());
        StdOut.println(arr.removeLast());
        StdOut.println(arr.size());
        StdOut.println(arr.isEmpty());
    }
}
