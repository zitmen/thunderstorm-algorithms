package cz.cuni.lf1.thunderstorm.algorithms.estimators

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.Molecule
import cz.cuni.lf1.thunderstorm.datastructures.extensions.createMoleculeDetection
import cz.cuni.lf1.thunderstorm.datastructures.extensions.floor

public class CentroidEstimator(private val fittingRadius: Int) : Estimator {

    private val fittingGrid = FittingGrid(fittingRadius)

    override fun estimatePosition(image: GrayScaleImage, initialEstimate: Molecule): Molecule? {
        val xCenter = initialEstimate.xPos.getValue().floor().toInt()
        val yCenter = initialEstimate.yPos.getValue().floor().toInt()

        if (xCenter - fittingRadius < 0 || xCenter + fittingRadius >= image.getWidth() ||
                yCenter - fittingRadius < 0 || yCenter + fittingRadius >= image.getHeight()) {
            return null
        }

        var xPos = 0.0
        var yPos = 0.0

        var sum = 0.0
        for (r in ((yCenter - fittingRadius)..(yCenter + fittingRadius)).withIndex()) {
            for (c in ((xCenter - fittingRadius)..(xCenter + fittingRadius)).withIndex()) {
                val value = image.getValue(r.value, c.value)
                sum += value
                xPos += value * fittingGrid.getX(r.index, c.index)
                yPos += value * fittingGrid.getY(r.index, c.index)
            }
        }
        xPos /= sum
        yPos /= sum

        return createMoleculeDetection(xCenter + 0.5 + xPos, yCenter + 0.5 + yPos)
    }
}