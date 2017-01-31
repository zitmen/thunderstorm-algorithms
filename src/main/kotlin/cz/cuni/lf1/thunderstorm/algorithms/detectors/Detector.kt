package cz.cuni.lf1.thunderstorm.algorithms.detectors

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.Point2D

public interface Detector {

    public fun detect(image: GrayScaleImage, threshold: Double): List<Point2D>
}
