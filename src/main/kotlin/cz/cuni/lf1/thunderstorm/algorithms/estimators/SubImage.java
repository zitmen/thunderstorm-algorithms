package cz.cuni.lf1.thunderstorm.algorithms.estimators;

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage;
import cz.cuni.lf1.thunderstorm.datastructures.Molecule;

public class SubImage {

    public Molecule initialEstimate;
    public double[] xgrid;
    public double[] ygrid;
    public double[] values;
    public double detectorX;
    public double detectorY;
    public int size_y;
    public int size_x;

    public SubImage(Molecule initialEstimate, int sizeX, int sizeY, double[] xgrid, double[] ygrid, double[] values, double detectorX, double detectorY) {
        this.initialEstimate = initialEstimate;
        this.size_x = sizeX;
        this.size_y = sizeY;
        this.xgrid = xgrid;
        this.ygrid = ygrid;
        this.values = values;
        this.detectorX = detectorX;
        this.detectorY = detectorY;
    }

    public double getMax() {
        return max(values);
    }

    public double getMin() {
        return min(values);
    }

    public double getSum() {
        return sum(values);
    }

    // note: the function changes the input array!
    public double[] subtract(double[] values) {
        assert (this.values.length == values.length);
        for (int i = 0; i < values.length; i++) {
            values[i] = this.values[i] - values[i];
        }
        return values;
    }

    public double correctXPosition(double pos) {
        return pos + (int) initialEstimate.getXPos().getValue() + 0.5;
    }

    public double correctYPosition(double pos) {
        return pos + (int) initialEstimate.getYPos().getValue() + 0.5;
    }

    private static double max(double[] array) {
        double max = array[0];
        for(int i = 0; i < array.length; i++) {
            if(array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    private static double min(double[] array) {
        double min = array[0];
        for(int i = 0; i < array.length; i++) {
            if(array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    private static double sum(double[] arr) {
        double sum = 0.0;
        for(int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum;
    }

    public static SubImage createSubImage(int fittingRadius, GrayScaleImage image, Molecule initialEstimate) {
        int bigSubImage = 2*fittingRadius+1;
        double[] values = new double[bigSubImage*bigSubImage];
        double[] xgrid = new double[bigSubImage*bigSubImage];
        double[] ygrid = new double[bigSubImage*bigSubImage];

        int xPos = (int) initialEstimate.getXPos().getValue();
        int yPos = (int) initialEstimate.getYPos().getValue();

        int idx = 0;
        for(int y = -fittingRadius; y <= fittingRadius; y++) {
            for(int x = -fittingRadius; x <= fittingRadius; x++) {
                xgrid[idx] = x;
                ygrid[idx] = y;
                values[idx] = image.getValue(yPos + y, xPos + x);
                idx++;
            }
        }
        return new SubImage(initialEstimate, 2*fittingRadius+1, 2*fittingRadius+1, xgrid, ygrid, values, initialEstimate.getXPos().getValue() - xPos, initialEstimate.getYPos().getValue() - yPos);
    }
}