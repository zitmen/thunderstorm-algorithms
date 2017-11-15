package cz.cuni.lf1.thunderstorm.algorithms

import cz.cuni.lf1.thunderstorm.algorithms.detectors.MaxFilterDetector
import cz.cuni.lf1.thunderstorm.algorithms.estimators.CentroidEstimator
import cz.cuni.lf1.thunderstorm.algorithms.filters.BoxFilter
import cz.cuni.lf1.thunderstorm.algorithms.padding.ZeroPadding
import cz.cuni.lf1.thunderstorm.datastructures.Point2DImpl
import cz.cuni.lf1.thunderstorm.datastructures.extensions.createGrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.extensions.createPoint2D
import cz.cuni.lf1.thunderstorm.parser.thresholding.FormulaThreshold
import cz.cuni.lf1.thunderstorm.test.assertListOfPointsEquals
import org.junit.Test

internal class LocalizationPipelineTests {

    @Test
    public fun testPipeline() {
        val image = createGrayScaleImage(arrayOf(
                arrayOf(2.0, 3.0, 3.0, 1.0, 2.0),
                arrayOf(2.0, 1.0, 1.0, 2.0, 1.0),
                arrayOf(4.0, 2.0, 1.0, 9.0, 2.0),
                arrayOf(1.0, 1.0, 1.0, 1.0, 1.0),
                arrayOf(2.0, 3.0, 4.0, 9.0, 1.0),
                arrayOf(2.0, 3.0, 3.0, 1.0, 2.0),
                arrayOf(2.0, 1.0, 4.0, 4.0, 1.0)))

        val expected = listOf(createPoint2D(2.56, 3.65))

        val results = LocalizationPipeline.runLocalization(
                image,
                BoxFilter(3, ::ZeroPadding),
                MaxFilterDetector(1, FormulaThreshold("3.0")),
                CentroidEstimator(2))
            .map { Point2DImpl(it.xPos.getValue(), it.yPos.getValue()) }

        assertListOfPointsEquals(expected, results, 1e-2)
    }
}