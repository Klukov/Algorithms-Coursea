import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {
    private static final boolean HORIZONTAL = true;
    private static final boolean VERTICAL = false;

    private class Node2D {
        public Point2D point;
        public boolean axis;
        public Node2D root;
        public Node2D left;
        public Node2D right;

        Node2D (Point2D point) {
            this(point, null, VERTICAL);
        }

        Node2D (Point2D point, Node2D root, boolean axis) {
            this(point, root, axis, null, null);
        }

        Node2D (Point2D point, Node2D root, boolean axis, Node2D left, Node2D right) {
            this.point = point;
            this.root = root;
            this.axis = axis;
            this.left = left;
            this.right = right;
        }
    }

    private Node2D root;
    private int size;

    public KdTree() {                                 // construct an empty set of points
        this.root = null;
        this.size = 0;
    }

    public boolean isEmpty() {                          // is the set empty?
        return (this.size == 0);
    }

    public int size() {                                 // number of points in the set
        return this.size;
    }

    public void insert(Point2D p) {                     // add the point to the set (if it is not already in the set)
        if (p == null) { throw new java.lang.IllegalArgumentException(); }
        if (this.contains(p)) { return; }
        if (this.size == 0) { this.root = new Node2D(p); }
        else { insertion(p, this.root); };
        this.size++;
    }

    private void insertion (Point2D p, Node2D node) {
        if (node.axis == VERTICAL) {
            if ( p.x() > node.point.x() ) {
                if (node.right == null) { node.right = new Node2D(p, node, HORIZONTAL); }
                else { insertion(p, node.right); }
            }
            else {
                if (node.left == null) { node.left = new Node2D(p, node, HORIZONTAL); }
                else { insertion(p, node.left); }
            }
        }
        else { // node.axis == HORIZONTAL
            if ( p.y() > node.point.y() ) {
                if (node.right == null) { node.right = new Node2D(p, node, VERTICAL); }
                else { insertion(p, node.right); }
            }
            else {
                if (node.left == null) { node.left = new Node2D(p, node, VERTICAL); }
                else { insertion(p, node.left); }
            }
        }
    }

    public boolean contains(Point2D p) {                // does the set contain point p?
        if (p == null) { throw new java.lang.IllegalArgumentException(); }
        if (this.size == 0) {return false;}
        Node2D node = this.root;
        while (true) {
            if ( p.equals(node.point) ) {
                return true;
            }
            else {
                if (node.axis == VERTICAL) {
                    if ( p.x() > node.point.x() ) {
                        if (node.right == null) { return false; }
                        else { node = node.right; continue; }
                    }
                    else {
                        if (node.left == null) { return false; }
                        else { node = node.left; continue; }
                    }
                }
                else {
                    if ( p.y() > node.point.y() ) {
                        if (node.right == null) { return false; }
                        else { node = node.right; continue; }
                    }
                    else {
                        if (node.left == null) { return false; }
                        else { node = node.left; continue; }
                    }
                }
            }
        }
    }

    public void draw() {                                // draw all points to standard draw
        Node2D node = this.root;
        if (node == null) {return;}
        // draw root
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.01);
        node.point.draw();
        StdDraw.setPenRadius(0.001);
        StdDraw.line(node.point.x(), 0, node.point.x(), 1);
        if (node.left != null) { drawRecursive(node.left); }
        if (node.right != null) { drawRecursive(node.right); }
    }

    private void drawRecursive(Node2D node) {
        // draw a point
        StdDraw.setPenRadius(0.01);
        if (node.axis == VERTICAL) { StdDraw.setPenColor(StdDraw.RED); }
        else {StdDraw.setPenColor(StdDraw.BLUE);}
        node.point.draw();
        // draw a line
        StdDraw.setPenRadius(0.001);
        Node2D toCheck = node.root;
        if (node.axis == VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            while (toCheck.root != null) {
                if (toCheck.root.root != null) {
                    toCheck = toCheck.root.root;
                    if ((node.point.y() >= node.root.point.y() && node.point.y() <= toCheck.point.y()) ||
                            (node.point.y() <= node.root.point.y() && node.point.y() >= toCheck.point.y())) {
                        StdDraw.line(node.point.x(), node.root.point.y(),node.point.x(), toCheck.point.y());
                        if (node.left != null) { drawRecursive(node.left); }
                        if (node.right != null) { drawRecursive(node.right); }
                        return;
                    }
                } else { break;}
            }
            if ( node.point.y() > node.root.point.y() ) {
                StdDraw.line(node.point.x(), node.root.point.y(),node.point.x(), 1);
            } else { StdDraw.line(node.point.x(), 0,node.point.x(), node.root.point.y()); }
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            while (toCheck.root != null) {
                if (toCheck.root.root != null) {
                    toCheck = toCheck.root.root;
                    if ((node.point.x() >= node.root.point.x() && node.point.x() <= toCheck.point.x()) ||
                            (node.point.x() <= node.root.point.x() && node.point.x() >= toCheck.point.x())) {
                        StdDraw.line(node.root.point.x(), node.point.y(), toCheck.point.x(), node.point.y());
                        if (node.left != null) { drawRecursive(node.left); }
                        if (node.right != null) { drawRecursive(node.right); }
                        return;
                    }
                } else { break;}
            }
            if ( node.point.x() > node.root.point.x() ) {
                StdDraw.line(node.root.point.x(), node.point.y(),1, node.point.y());
            } else { StdDraw.line(node.root.point.x(), node.point.y(),0, node.point.y()); }
        }
        if (node.left != null) { drawRecursive(node.left); }
        if (node.right != null) { drawRecursive(node.right); }
    }

    public Iterable<Point2D> range(RectHV rect) {       // all points that are inside the rectangle (or on the boundary)
        if (rect == null) {throw new java.lang.IllegalArgumentException();}
        ArrayList<Point2D> result = new ArrayList<>();
        findRange(result, this.root, rect, new RectHV(0,0,1,1));
        return result;
    }

    private void findRange(ArrayList<Point2D> result, Node2D node, RectHV query, RectHV nodeRect) {
        if (node == null) {return;}
        if (query.contains(node.point)) { result.add(node.point); }
        if (node.axis == VERTICAL) {
            // left side
            RectHV leftRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), node.point.x(), nodeRect.ymax());
            if (query.intersects(leftRect)) {
                findRange(result, node.left, query, leftRect);
            }
            // right side
            RectHV rightRect = new RectHV(node.point.x(), nodeRect.ymin(), nodeRect.xmax(), nodeRect.ymax());
            if (query.intersects(rightRect)) {
                findRange(result, node.right, query, rightRect);
            }
        }
        else { // is HORIZONTAL
            // bottom side
            RectHV bottomRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(), node.point.y());
            if (query.intersects(bottomRect)) {
                findRange(result, node.left, query, bottomRect);
            }
            // upper side
            RectHV upperRect = new RectHV(nodeRect.xmin(), node.point.y(), nodeRect.xmax(), nodeRect.ymax());
            if (query.intersects(upperRect)) {
                findRange(result, node.right, query, upperRect);
            }
        }
    }
    private Point2D nearest; // helpful variable

    public Point2D nearest(Point2D p) {                 // a nearest neighbor in the set to point p; null if the set is empty
        // check correctness of query
        if (p == null) {throw new java.lang.IllegalArgumentException();}
        if (this.size() == 0) {return null;}

        // set start variables
        Node2D check = this.root;
        this.nearest = new Point2D(check.point.x(), check.point.y()) ;

        //calculate rectangles and distances
        RectHV leftRect = new RectHV(0, 0, check.point.x(), 1);
        RectHV rightRect = new RectHV(check.point.x(), 0, 1, 1);
        double distNearest = this.nearest.distanceSquaredTo(p);
        double distLeftRect = leftRect.distanceSquaredTo(p);
        double distRightRect = rightRect.distanceSquaredTo(p);

        // find nearer point
        if (distLeftRect < distRightRect) {
            findNearest(check.left, leftRect, p);
            //update nearest
            distNearest = this.nearest.distanceSquaredTo(p);
            if (distRightRect <= distNearest) {
                findNearest(check.right, rightRect, p);
            }
        }
        else {
            findNearest(check.right, rightRect, p);
            //update nearest
            distNearest = this.nearest.distanceSquaredTo(p);
            if (distLeftRect <= distNearest ) {
                findNearest(check.left, leftRect, p);
            }
        }
        return this.nearest;
    }

    private void findNearest(Node2D check, RectHV checkRect, Point2D query) {
        // if null, then return
        if (check == null) {return;}

        //check if is equal to query
        if (check.point.equals(query)) {this.nearest = new Point2D(check.point.x(), check.point.y()); return;}

        // calculate distances
        double distNearest = this.nearest.distanceSquaredTo(query);
        double distCheck = check.point.distanceSquaredTo(query);

        // check if Check is nearer than Nearest
        if (distCheck < distNearest) { this.nearest = new Point2D(check.point.x(), check.point.y()); }

        //calculate rectangles and distances
        RectHV leftRect, rightRect;
        Double distLeftRect, distRightRect;
        if (check.axis == VERTICAL) { // Check for vertical
            leftRect = new RectHV(checkRect.xmin(), checkRect.ymin(), check.point.x(), checkRect.ymax());
            rightRect = new RectHV(check.point.x(), checkRect.ymin(), checkRect.xmax(), checkRect.ymax());
            distLeftRect = leftRect.distanceSquaredTo(query);
            distRightRect = rightRect.distanceSquaredTo(query);
        }
        else { // is HORIZONTAL
            leftRect = new RectHV(checkRect.xmin(), checkRect.ymin(), checkRect.xmax(), check.point.y());
            rightRect = new RectHV(checkRect.xmin(), check.point.y(), checkRect.xmax(), checkRect.ymax());
            distLeftRect = leftRect.distanceSquaredTo(query);
            distRightRect = rightRect.distanceSquaredTo(query);
        }

        // search
        if (distLeftRect < distRightRect) {
            // left side
            if (distLeftRect <= distNearest ) {
                findNearest(check.left, leftRect, query);
            } else {return;}
            //update nearest
            distNearest = this.nearest.distanceSquaredTo(query);
            //right side
            if (distRightRect <= distNearest) {
                findNearest(check.right, rightRect, query);
            }
        }
        else {
            // right side
            if (distRightRect <= distNearest) {
                findNearest(check.right, rightRect, query);
            } else {return;}
            //update nearest
            distNearest = this.nearest.distanceSquaredTo(query);
            //left side
            if (distLeftRect <= distNearest ) {
                findNearest(check.left, leftRect, query);
            }
        }
    }

    public static void main(String[] args) {            // unit testing of the methods (optional)

    }
}
