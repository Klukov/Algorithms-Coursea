import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int size;
    private int arrayLength;
    private Item[] queue;


    private class RandomQueueIterator implements Iterator<Item> {
        private Item[] check;
        private int sizeIterator;

        public RandomQueueIterator() {
            this.sizeIterator = size;
            check = (Item[]) new Object[this.sizeIterator];
            for (int i=0; i < this.sizeIterator; i++) {
                this.check[i] = queue[i];
            }
        }

        public boolean hasNext() { return this.sizeIterator != 0; }
        public Item next() {
            if (!hasNext()) { throw new java.util.NoSuchElementException(); }
            int randNum = StdRandom.uniform(this.sizeIterator);
            Item item = check[randNum];
            check[randNum] = check[this.sizeIterator-1];
            check[this.sizeIterator-1] = null;
            this.sizeIterator--;
            return item;
        }
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    public RandomizedQueue() {                  // construct an empty randomized queue
        this.size = 0;
        this.arrayLength = 1;
        queue = (Item[]) new Object[arrayLength];
    }

    public boolean isEmpty() {                  // is the randomized queue empty?
        return (this.size == 0);
    }

    public int size() {                         // return the number of items on the randomized queue
        return this.size;
    }

    public void enqueue(Item item) {            // add the item
        if (item == null) { throw new java.lang.IllegalArgumentException(); };
        if (this.size == this.arrayLength) {
            this.arrayLength = 2*this.arrayLength;
            Item[] newQueue = (Item[]) new Object [this.arrayLength];
            for (int i = 0; i < this.size; i++) {
                newQueue[i] = this.queue[i];
            }
            this.queue = newQueue;
        }
        this.queue[this.size] = item;
        this.size++;
    }

    public Item dequeue() {                     // remove and return a random item
        if (this.size == 0) { throw new java.util.NoSuchElementException(); }
        int randomNum = StdRandom.uniform(this.size);
        Item toDeque = queue[randomNum];
        queue[randomNum] = queue[this.size-1];
        this.size--;
        if (this.size == this.arrayLength/4 && this.size != 0) {
            this.arrayLength = this.arrayLength/2;
            Item[] newQueue = (Item[]) new Object [this.arrayLength];
            for (int i = 0; i < this.size; i++) {
                newQueue[i] = this.queue[i];
            }
            this.queue = newQueue;
        }
        return toDeque;
    }

    public Item sample() {                      // return a random item (but do not remove it)
        if (this.size == 0) { throw new java.util.NoSuchElementException(); }
        int randomNum = StdRandom.uniform(this.size);
        return this.queue[randomNum];
    }

    public Iterator<Item> iterator() {          // return an independent iterator over items in random order
        return new RandomQueueIterator();
    }

    public static void main(String[] args) {    // unit testing (optional)
        RandomizedQueue<Integer> randQueue = new RandomizedQueue<Integer>();
        int n = 10;
        for (int i = 0; i < n; i++ ) {
            randQueue.enqueue(i);
            randQueue.enqueue(i*i);
        }
        StdOut.println();
        while (!randQueue.isEmpty()) {
            int a = randQueue.dequeue();
            StdOut.println(a + "\t\t Size: " + randQueue.size());
            if (a%2 == 0) {
                randQueue.enqueue(a+1);
            }
        }
        randQueue.enqueue(2);
        StdOut.println();
    }

}
