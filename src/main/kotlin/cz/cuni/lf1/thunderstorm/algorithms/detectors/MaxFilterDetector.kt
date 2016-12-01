package cz.cuni.lf1.thunderstorm.algorithms.detectors

import cz.cuni.lf1.thunderstorm.algorithms.detectors.Detector
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.Point2D
import cz.cuni.lf1.thunderstorm.datastructures.extensions.create2DDoubleArray
import cz.cuni.lf1.thunderstorm.datastructures.extensions.createGrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.extensions.createPoint2D
import cz.cuni.lf1.thunderstorm.datastructures.extensions.dilate

public class MaxFilterDetector(
        private val radius: Int,
        private val threshold: Double) : Detector {

    public override fun detect(image: GrayScaleImage): List<Point2D> {
        val detections = arrayListOf<Point2D>()
        val dilatedImage = image.dilate(createGrayScaleImage(create2DDoubleArray(2*radius + 1, 2*radius + 1, 1.0)))

        for (y in radius..(image.getHeight() - radius - 1)) {
            for (x in radius..(image.getWidth() - radius - 1)) {
                if((image.getValue(y, x) == dilatedImage.getValue(y, x)) && (image.getValue(y, x) >= threshold)) {
                    detections.add(createPoint2D(x.toDouble(), y.toDouble()))
                }
            }
        }

        return detections
    }
}