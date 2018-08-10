import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private static final double DEFAULT_PIXEL_VALUE = 1000;
    private Picture currentPicture;
    private double[][] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) { throw new java.lang.IllegalArgumentException(); }

        // Create a copy of the picture
        this.currentPicture = new Picture(picture);

        // SET ENERGY
        setEnergy();
    }

    private void setEnergy() {
        // create new matrix with values
        this.energy = new double[this.width()][this.height()];

        // default values
        for (int i=0; i<this.width(); i++) {
            this.energy[i][0] = DEFAULT_PIXEL_VALUE;
            this.energy[i][this.height()-1] = DEFAULT_PIXEL_VALUE;
        }
        for (int i=0; i<this.height(); i++) {
            this.energy[0][i] = DEFAULT_PIXEL_VALUE;
            this.energy[this.width()-1][i] = DEFAULT_PIXEL_VALUE;
        }
        // get red, green, blue
        int[][] red = new int[this.width()][this.height()];
        int[][] green = new int[this.width()][this.height()];
        int[][] blue = new int[this.width()][this.height()];
        for (int i=0; i<this.width(); i++) {
            for (int j=0; j<this.height(); j++) {
                red[i][j] = this.currentPicture.get(i,j).getRed();
                green[i][j] = this.currentPicture.get(i,j).getGreen();
                blue[i][j] = this.currentPicture.get(i,j).getBlue();
            }
        }
        //calculate values
        for (int i=1; i<this.width()-1; i++) {
            for (int j=1; j<this.height()-1; j++) {
                int rx = red[i-1][j] - red[i+1][j];
                int gx = green[i-1][j] - green[i+1][j];
                int bx = blue[i-1][j] - blue[i+1][j];
                int ry = red[i][j-1] - red[i][j+1];
                int gy = green[i][j-1] - green[i][j+1];
                int by = blue[i][j-1] - blue[i][j+1];
                this.energy[i][j] = Math.sqrt((double)(rx*rx + gx*gx + bx*bx + ry*ry + gy*gy + by*by));
            }
        }
    }

    // current picture
    public Picture picture() {
        return new Picture(this.currentPicture);
    }

    // width of current picture
    public int width() {
        return this.currentPicture.width();
    }

    // height of current picture
    public int height() {
        return this.currentPicture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0) { throw new java.lang.IllegalArgumentException(); }
        if ((x > this.width()-1) || (y > this.height()-1)) { throw new java.lang.IllegalArgumentException(); }
        return this.energy[x][y];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] distTo = new double[this.width()][this.height()];
        int[][] edgeTo = new int[this.width()][this.height()];
        int[] result = new int[this.width()];

        // initialize first layer
        for (int y=0; y < this.height(); y++) {
            edgeTo[0][y] = -1;
            distTo[0][y] = this.energy[0][y];
        }

        // set the rest of distances to the infinity
        for (int x=1; x < this.width(); x++) {
            for (int y=0; y < this.height(); y++) {
                distTo[x][y] = Double.MAX_VALUE;
            }
        }

        // Djikstra algorithm for finding shortest path
        for (int x=0; x < this.width()-1; x++) {
            for (int y=0; y < this.height(); y++) {
                horizontalRelaxation(x,y,distTo,edgeTo);
            }
        }

        // finding minimum distance at the end
        double minimum = Double.MAX_VALUE;
        int rowMin = -1;
        for (int y=0; y < this.height(); y++) {
            if (distTo[this.width()-1][y] < minimum) {
                minimum = distTo[this.width()-1][y];
                rowMin = y;
            }
        }

        // write solution into final table
        int index = rowMin;
        for (int x = this.width()-1; x>-1; x--) {
            result[x] = index;
            index = edgeTo[x][index];
        }

        return result;
    }

    private void horizontalRelaxation(int x, int y, double[][] distTo, int[][] edgeTo) {
        int min = y-1;
        int max = y+1;
        // however check for special cases
        if (y == 0) { min = y; }
        if (y == this.height()-1) { max = y; }
        //relax vertices
        for (int nextRow = min; nextRow <= max; nextRow++ ) {
            double energy = distTo[x][y] + this.energy[x+1][nextRow];
            if (distTo[x+1][nextRow] > energy) {
                edgeTo[x+1][nextRow] = y;
                distTo[x+1][nextRow] = energy;
            }
        }
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] distTo = new double[this.width()][this.height()];
        int[][] edgeTo = new int[this.width()][this.height()];
        int[] result = new int[this.height()];

        // initialize first layer
        for (int x=0; x < this.width(); x++) {
            edgeTo[x][0] = -1;
            distTo[x][0] = this.energy[x][0];
        }

        // set the rest of distances to the infinity
        for (int i=0; i < this.width(); i++) {
            for (int j=1; j< this.height(); j++) {
                distTo[i][j] = Double.MAX_VALUE;
            }
        }

        // Djikstra algorithm for finding shortest path
        for (int y=0; y < this.height()-1; y++) {
            for (int x=0; x < this.width(); x++) {
                verticalRelaxation(x,y, distTo, edgeTo);
            }
        }

        // finding minimum distance at the end
        double minimum = Double.MAX_VALUE;
        int rowMin = -1;
        for (int x=0; x < this.width(); x++) {
            if (distTo[x][this.height()-1] < minimum) {
                minimum = distTo[x][this.height()-1];
                rowMin = x;
            }
        }

        // write solution into final table
        int index = rowMin;
        for (int y = this.height()-1; y > -1; y--) {
            result[y] = index;
            index = edgeTo[index][y];
        }

        return result;
    }

    private void verticalRelaxation(int x, int y, double[][] distTo, int[][] edgeTo) {
        int min = x-1;
        int max = x+1;
        // however check for special cases
        if (x == 0) { min = x; }
        if (x == this.width()-1) { max = x; }
        // relax vertices
        for (int nextColumn = min; nextColumn <= max; nextColumn++ ) {
            double energy = distTo[x][y] + this.energy[nextColumn][y+1];
            if (distTo[nextColumn][y+1] > energy) {
                edgeTo[nextColumn][y+1] = x;
                distTo[nextColumn][y+1] = energy;
            }
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        // validate horizontal seam
        if (seam == null) { throw new java.lang.IllegalArgumentException(); }
        if (seam.length != this.width()) { throw new java.lang.IllegalArgumentException(); }
        if (seam[0] >= this.height()) { throw new java.lang.IllegalArgumentException(); }
        if (seam[0] < 0) { throw new java.lang.IllegalArgumentException(); }
        for (int i=1; i < seam.length; i++) {
            if (seam[i] >= this.height()) { throw new java.lang.IllegalArgumentException(); }
            if (seam[i] < 0) { throw new java.lang.IllegalArgumentException(); }
            if (Math.abs(seam[i]-seam[i-1]) > 1) { throw new java.lang.IllegalArgumentException(); }
        }
        // remove horizontal seam
        Picture newPicture = new Picture(this.width(),this.height()-1);
        for (int i=0; i<this.width(); i++) {
            int displacement = 0;
            for (int j=0; j<this.height()-1; j++) {
                if (seam[i] == j) { displacement=1; }
                newPicture.set(i, j, this.currentPicture.get(i,j+displacement));
            }
        }
        this.currentPicture = newPicture;
        setEnergy();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        // validate vertical seam
        if (seam == null) { throw new java.lang.IllegalArgumentException(); }
        if (seam.length != this.height()) { throw new java.lang.IllegalArgumentException(); }
        if (seam[0] >= this.width()) { throw new java.lang.IllegalArgumentException(); }
        if (seam[0] < 0) { throw new java.lang.IllegalArgumentException(); }
        for (int i=1; i < seam.length; i++) {
            if (seam[i] >= this.width()) { throw new java.lang.IllegalArgumentException(); }
            if (seam[i] < 0) { throw new java.lang.IllegalArgumentException(); }
            if (Math.abs(seam[i]-seam[i-1]) > 1) { throw new java.lang.IllegalArgumentException(); }
        }
        // remove vertical seam
        Picture newPicture = new Picture(this.width()-1, this.height());
        for (int j=0; j<this.height(); j++) {
            int displacement = 0;
            for (int i=0; i<this.width()-1; i++) {
                if (seam[j] == i) { displacement=1; }
                newPicture.set(i, j, this.currentPicture.get(i+displacement,j));
            }
        }
        this.currentPicture = newPicture;
        setEnergy();
    }
}
