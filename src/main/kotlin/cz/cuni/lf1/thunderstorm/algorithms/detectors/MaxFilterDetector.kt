package cz.cuni.lf1.thunderstorm.algorithms.detectors

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.Molecule
import cz.cuni.lf1.thunderstorm.datastructures.extensions.create2DDoubleArray
import cz.cuni.lf1.thunderstorm.datastructures.extensions.createGrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.extensions.createMoleculeDetection
import cz.cuni.lf1.thunderstorm.datastructures.extensions.dilate
import cz.cuni.lf1.thunderstorm.parser.thresholding.FormulaThreshold

public class MaxFilterDetector(
        private val radius: Int,
        private val thresholder: FormulaThreshold) : Detector {

    public override fun detect(image: GrayScaleImage): List<Molecule> {
        val detections = arrayListOf<Molecule>()
        val threshold = thresholder.getValue(image)
        val dilatedImage = image.dilate(createGrayScaleImage(create2DDoubleArray(2*radius + 1, 2*radius + 1, 1.0)))

        for (y in radius..(image.getHeight() - radius - 1)) {
            for (x in radius..(image.getWidth() - radius - 1)) {
                if((image.getValue(y, x) == dilatedImage.getValue(y, x)) && (image.getValue(y, x) >= threshold)) {
                    detections.add(createMoleculeDetection(0.5 + x.toDouble(), 0.5 + y.toDouble()))
                }
            }
        }

        return detections
    }
}