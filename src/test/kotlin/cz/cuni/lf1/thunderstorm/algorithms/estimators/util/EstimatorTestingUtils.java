package cz.cuni.lf1.thunderstorm.algorithms.estimators.util;

import cz.cuni.lf1.thunderstorm.algorithms.detectors.CentroidOfConnectedComponentsDetector;
import cz.cuni.lf1.thunderstorm.algorithms.estimators.Estimator;
import cz.cuni.lf1.thunderstorm.algorithms.filters.CompoundWaveletFilter;
import cz.cuni.lf1.thunderstorm.algorithms.padding.DuplicatePadding;
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage;
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImageImplIj;
import cz.cuni.lf1.thunderstorm.datastructures.Molecule;
import cz.cuni.lf1.thunderstorm.parser.thresholding.FormulaThreshold;
import ij.IJ;
import ij.process.FloatProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import static cz.cuni.lf1.thunderstorm.datastructures.extensions.MoleculeKt.createMoleculeDetectionNano;
import static org.junit.Assert.*;

public class EstimatorTestingUtils {

    public static List<Pair> testEstimator(Estimator estimator) {
        GrayScaleImage image = new GrayScaleImageImplIj((FloatProcessor) IJ.openImage(EstimatorTestingUtils.class.getClassLoader().getResource("tubulins1_00020.tif").getPath()).getProcessor().convertToFloat());
        GrayScaleImage filtered = (new CompoundWaveletFilter(3, 2.0, 5, DuplicatePadding::new)).filter(image);
        List<Molecule> detections = new CentroidOfConnectedComponentsDetector(true, new FormulaThreshold("16", new HashMap<>())).detect(filtered);
        List<Molecule> fits = new ArrayList<>();
        List<Molecule> detectionsNano = new ArrayList<>();
        List<Molecule> fitsNano = new ArrayList<>();
        for (Molecule detection : detections) {
            Molecule fit = estimator.estimatePosition(image, detection);
            fits.add(fit);
            detectionsNano.add(convertXYToNanoMeters(detection, 150.0));
            fitsNano.add(convertXYToNanoMeters(fit, 150.0));
        }
        Vector<Molecule> ground_truth = null;
        try {
            ground_truth = CSV.csv2psf(EstimatorTestingUtils.class.getClassLoader().getResource("tubulins1_00020.csv").getPath(), 1, 2);
        } catch(Exception ex) {
            fail(ex.getMessage());
        }
        return pairFitsAndDetections2GroundTruths(detectionsNano, fitsNano, ground_truth);
    }

    public static void assertFittingMorePreciseThanDetection(List<Pair> pairs) {
        for(Pair pair : pairs) {
            assertFalse("Result from the estimator should be better than guess from the detector.", dist2(pair.fit, pair.ground_truth) > dist2(pair.detection, pair.ground_truth));
        }
    }

    private static Molecule convertXYToNanoMeters(Molecule fit, double px2nm) {
        return createMoleculeDetectionNano(
                fit.getXPos().getValue() * px2nm,
                fit.getYPos().getValue() * px2nm);
    }

    public static class Pair {

        public Molecule detection;
        public Molecule fit;
        public Molecule ground_truth;

        public Pair(Molecule detection, Molecule fit, Molecule ground_truth) {
            this.detection = detection;
            this.fit = fit;
            this.ground_truth = ground_truth;
        }
    }

    private static Vector<Pair> pairFitsAndDetections2GroundTruths(List<Molecule> detections, List<Molecule> fits, Vector<Molecule> ground_truth) {
        assertNotNull(fits);
        assertNotNull(detections);
        assertNotNull(ground_truth);
        assertFalse(fits.isEmpty());
        assertFalse(detections.isEmpty());
        assertFalse(ground_truth.isEmpty());
        assertEquals("Number of detections should be the same as number of fits!", detections.size(), fits.size());

        Vector<Pair> pairs = new Vector<>();
        int best_fit;
        double best_dist2, dist2;
        for(int i = 0, im = fits.size(); i < im; i++) {
            best_fit = 0;
            best_dist2 = dist2(fits.get(i), ground_truth.elementAt(best_fit));
            for(int j = 1, jm = ground_truth.size(); j < jm; j++) {
                dist2 = dist2(fits.get(i), ground_truth.elementAt(j));
                if(dist2 < best_dist2) {
                    best_dist2 = dist2;
                    best_fit = j;
                }
            }
            pairs.add(new Pair(detections.get(i), fits.get(i), ground_truth.elementAt(best_fit)));
        }

        return pairs;
    }

    private static double dist2(Molecule estimate, Molecule ground_truth) {
        return sqr(estimate.getXPos().getValue() - ground_truth.getXPos().getValue()) +
                sqr(estimate.getYPos().getValue() - ground_truth.getYPos().getValue());
    }

    private static double sqr(double x) {
        return x * x;
    }
}
