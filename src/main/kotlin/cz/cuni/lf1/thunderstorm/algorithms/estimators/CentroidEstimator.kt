package cz.cuni.lf1.thunderstorm.algorithms.estimators

import cz.cuni.lf1.thunderstorm.datastructures.Distance
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.Molecule

public class CentroidEstimator(fittingRadius: Int) : Estimator {

    private val fittingGrid = FittingGrid(fittingRadius)

    override fun estimatePosition(image: GrayScaleImage, initialEstimate: Molecule): Molecule {
        var xPos = initialEstimate.xPos.getValue()
        var yPos = initialEstimate.yPos.getValue()
        var sum = 0.0
        for (r in 0..(image.getHeight() - 1)) {
            for (c in 0..(image.getWidth() - 1)) {
                val value = image.getValue(r, c)
                sum += value
                xPos += value * fittingGrid.getX(r, c)
                yPos += value * fittingGrid.getY(r, c)
            }
        }
        xPos /= sum
        yPos /= sum

        return Molecule(Distance.fromPixels(xPos), Distance.fromPixels(yPos))
    }
}