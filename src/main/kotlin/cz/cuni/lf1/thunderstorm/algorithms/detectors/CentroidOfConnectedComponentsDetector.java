package cz.cuni.lf1.thunderstorm.algorithms.detectors;

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage;
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImageImplIj;
import cz.cuni.lf1.thunderstorm.datastructures.Intensity;
import cz.cuni.lf1.thunderstorm.datastructures.Molecule;
import cz.cuni.lf1.thunderstorm.datastructures.extensions.Graph;
import cz.cuni.lf1.thunderstorm.parser.thresholding.FormulaThreshold;
import ij.plugin.filter.MaximumFinder;
import ij.process.ByteProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public final class CentroidOfConnectedComponentsDetector implements Detector {

    private boolean useWatershed;
    private FormulaThreshold thresholder;

    public CentroidOfConnectedComponentsDetector(boolean useWatershed, FormulaThreshold thresholder) {
        this.useWatershed = useWatershed;
        this.thresholder = thresholder;
    }

    @NotNull
    @Override
    public List<Molecule> detect(@NotNull GrayScaleImage grayScaleImage) {
        //keep a local threshold value so the method remains thread safe
        double localThresholdValue = thresholder.getValue(grayScaleImage);
        FloatProcessor image = GrayScaleImageImplIj.convertToFloatProcessor(grayScaleImage);
        FloatProcessor thresholdedImage = (FloatProcessor) image.duplicate();
        threshold(thresholdedImage, localThresholdValue, 0.0f, 255.0f);

        FloatProcessor maskedImage = applyMask(image, thresholdedImage);

        if(useWatershed) {
            ImageJ_MaximumFinder watershedImpl = new ImageJ_MaximumFinder();
            ByteProcessor watershedImage = watershedImpl.findMaxima(maskedImage, 0, ImageProcessor.NO_THRESHOLD, MaximumFinder.SEGMENTED, false, false);
            FloatProcessor thresholdImageANDWatershedImage = applyMask(thresholdedImage, (FloatProcessor) watershedImage.convertToFloat());
            maskedImage = thresholdImageANDWatershedImage;
        }
        // finding a center of gravity (with subpixel precision)
        List<Molecule> detections = new ArrayList<Molecule>();
        for(Graph.ConnectedComponent c : Graph.getConnectedComponents((ImageProcessor) maskedImage, Graph.CONNECTIVITY_8)) {
            Molecule pt = c.centroid();
            pt.setIntensity(Intensity.Companion.fromAdCounts(image.getf((int)Math.round(pt.getXPos().getValue()), (int)Math.round(pt.getYPos().getValue()))));
            detections.add(pt);
        }
        return detections;
    }

    private static void threshold(FloatProcessor image, double threshold, float low_val, float high_val) {
        for (int i = 0, im = image.getWidth(); i < im; i++) {
            for (int j = 0, jm = image.getHeight(); j < jm; j++) {
                if (image.getPixelValue(i, j) >= threshold) {
                    image.setf(i, j, high_val);
                } else {
                    image.setf(i, j, low_val);
                }
            }
        }
    }

    private static FloatProcessor applyMask(FloatProcessor image, FloatProcessor mask) {
        assert (image.getWidth() == mask.getWidth());
        assert (image.getHeight() == mask.getHeight());

        FloatProcessor result = new FloatProcessor(image.getWidth(), image.getHeight(), (float[]) image.getPixelsCopy(), null);
        for (int x = 0, xm = image.getWidth(); x < xm; x++) {
            for (int y = 0, ym = image.getHeight(); y < ym; y++) {
                if (mask.getf(x, y) == 0.0f) {
                    result.setf(x, y, 0.0f);
                }
            }
        }

        return result;
    }
}
