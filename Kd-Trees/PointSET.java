import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> pointSet;

    public PointSET() {                                 // construct an empty set of points
        this.pointSet = new TreeSet<>();
    }

    public boolean isEmpty() {                          // is the set empty?
        return this.pointSet.isEmpty();
    }

    public int size() {                                 // number of points in the set
        return this.pointSet.size();
    }

    public void insert(Point2D p) {                     // add the point to the set (if it is not already in the set)
        if (p == null) {throw new java.lang.IllegalArgumentException();}
        this.pointSet.add(p);
    }

    public boolean contains(Point2D p) {                // does the set contain point p?
        if (p == null) {throw new java.lang.IllegalArgumentException();}
        return this.pointSet.contains(p);
    }

    public void draw() {                                // draw all points to standard draw
        for (Point2D i : this.pointSet) {
            i.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {       // all points that are inside the rectangle (or on the boundary)
        if (rect == null) {throw new java.lang.IllegalArgumentException();}
        TreeSet<Point2D> result = new TreeSet<>();
        for (Point2D i : this.pointSet) {
            if (rect.contains(i)) {
                result.add(i);
            }
        }
        return result;
    }

    public Point2D nearest(Point2D p) {                 // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) {throw new java.lang.IllegalArgumentException();}
        double dist = Double.MAX_VALUE;
        Point2D point = null;
        for (Point2D i : this.pointSet) {
            double temp = p.distanceTo(i);
            if (temp < dist) {
                dist = temp;
                point = i;
            }
        }
        return point;
    }

    public static void main(String[] args) {            // unit testing of the methods (optional)

    }
}