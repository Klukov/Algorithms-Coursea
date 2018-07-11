import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> lineSegments = new ArrayList<>();

    public BruteCollinearPoints(Point[] points) {       // finds all line segments containing 4 points
        // check data
        if (points == null) { throw new java.lang.IllegalArgumentException(); }
        for (Point i : points) {
            if (i == null) { throw new java.lang.IllegalArgumentException(); }
        }
        // initialization
        int n =  points.length;
        Point[] test = Arrays.copyOf(points, points.length);
        Arrays.sort(test);
        // check for repeated points
        for (int i=0; i<n-1; i++) {
            if (test[i].slopeTo(test[i+1]) == Double.NEGATIVE_INFINITY) {
                throw new java.lang.IllegalArgumentException();
            }
        }
        //searching
        for (int i=0; i<n-3; i++) {
            Point a0 = test[i];
            for (int j=i+1; j<n-2; j++) {
                Point a1 = test[j];
                double slope1 = a0.slopeTo(a1);
                for (int k = j+1; k<n-1; k++) {
                    Point a2 = test[k];
                    double slope2 = a0.slopeTo(a2);
                    if (slope1 != slope2) {continue;}
                    for (int l = k+1; l < n; l++) {
                        Point a3 = test[l];
                        double slope3 = a0.slopeTo(a3);
                        if (slope1 == slope2 && slope1 == slope3) {
                            lineSegments.add(new LineSegment(a0, a3));
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {                     // the number of line segments
        return this.lineSegments.size();
    }

    public LineSegment[] segments()  {                  // the line segments
        LineSegment[] temp = new LineSegment[this.lineSegments.size()];
        temp = this.lineSegments.toArray(temp);
        return temp;
    }
}
