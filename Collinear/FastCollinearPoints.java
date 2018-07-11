import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    private ArrayList<LineSegment> lineSegments = new ArrayList<>();
    private ArrayList<EndPoint> endPoints = new ArrayList<>();

    private class EndPoint {
        private final Point point;
        private ArrayList<Double> slopes = new ArrayList<>();

        public EndPoint (Point point, double slope) {
            this.point = point;
            //slopes = new ArrayList<>();
            this.slopes.add(slope);
        }

        public Point getPoint() {
            return point;
        }

        public double getSlope (int index) {
            if (index >= slopes.size()) { throw new java.lang.IllegalArgumentException(); }
            return slopes.get(index);
        }
        public int getSlopesSize() {
            return slopes.size();
        }
        public void addSlope(double slope) {
            this.slopes.add(slope);
        }
    }

    public FastCollinearPoints(Point[] points) {        // finds all line segments containing 4 or more points
        // check data
        if (points == null) { throw new java.lang.IllegalArgumentException(); }
        for (Point i : points) {
            if (i == null) { throw new java.lang.IllegalArgumentException(); }
        }
        // initialization
        int n =  points.length;
        Point[] test = Arrays.copyOf(points, n);
        Arrays.sort(test);
        // check for repeated points
        for (int i=0; i<n-1; i++) {
            if (test[i].slopeTo(test[i+1]) == Double.NEGATIVE_INFINITY) {
                throw new java.lang.IllegalArgumentException();
            }
        }
        //searching
        for (int i=0; i<n-1; i++) {
            if (i !=0) { Arrays.sort(test, i, n); }
            Point point = test[i];
            Comparator<Point> comp = point.slopeOrder();
            Arrays.sort(test,i+1, n, comp);
            double referenceSlope = point.slopeTo(test[i+1]);
            int amountOfPoints = 2;
            for (int j=i+2; j<n; j++) {
                double tempSlope = point.slopeTo(test[j]);
                if (tempSlope == referenceSlope) {
                    amountOfPoints++;
                }
                else {
                    if (amountOfPoints >= 4) {
                        addSegment(point,test[j-1],referenceSlope);
                        //this.lineSegments.add(new LineSegment(point,test[j-1]));
                    }
                    referenceSlope = tempSlope;
                    amountOfPoints = 2;
                }
            }
            if (amountOfPoints >= 4) {
                addSegment(point,test[n-1],referenceSlope);
                //this.lineSegments.add(new LineSegment(point,test[n-1]));
            }
        }
    }

    private void addSegment (Point p1, Point p2, Double slope) {
        boolean isToAdd = true;
        for (int i=0; i<this.endPoints.size(); i++) {
            if (this.endPoints.get(i).getPoint().equals(p2)) {
                for (int j=0; j <this.endPoints.get(i).getSlopesSize(); j++) {
                    if (this.endPoints.get(i).getSlope(j) == slope) {
                        isToAdd = false;
                        break;
                    }
                }
                if (isToAdd) {
                    this.endPoints.get(i).addSlope(slope);
                    this.lineSegments.add(new LineSegment(p1,p2));
                    isToAdd = false;
                }
                break;
            }
        }
        if (isToAdd) {
            endPoints.add(new EndPoint(p2, slope));
            this.lineSegments.add(new LineSegment(p1,p2));
        }
    }

    public int numberOfSegments() {                     // the number of line segments
        return this.lineSegments.size();
    }

    public LineSegment[] segments() {                   // the line segments
        LineSegment[] temp = new LineSegment[this.lineSegments.size()];
        temp = this.lineSegments.toArray(temp);
        return temp;
    }
}
