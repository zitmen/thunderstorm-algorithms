package cz.cuni.lf1.thunderstorm.datastructures;

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage;
import ij.process.FloatProcessor;

public class GrayScaleImageImplIj implements GrayScaleImage {

    private FloatProcessor floatProcessor;

    public GrayScaleImageImplIj(FloatProcessor fp) {
        floatProcessor = fp;
    }

    @Override
    public int getWidth() {
        return floatProcessor.getWidth();
    }

    @Override
    public int getHeight() {
        return floatProcessor.getHeight();
    }

    @Override
    public double getValue(int row, int col) {
        return floatProcessor.getPixelValue(col, row);
    }

    public static FloatProcessor convertToFloatProcessor(GrayScaleImage img) {
        FloatProcessor fp = new FloatProcessor(img.getWidth(), img.getHeight());
        for (int r = 0; r < fp.getHeight(); r++) {
            for (int c = 0; c < fp.getWidth(); c++) {
                fp.putPixelValue(c, r, img.getValue(r, c));
            }
        }
        return fp;
    }
}