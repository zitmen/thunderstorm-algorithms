package cz.cuni.lf1.thunderstorm.algorithms

import cz.cuni.lf1.thunderstorm.algorithms.detectors.MaxFilterDetector
import cz.cuni.lf1.thunderstorm.algorithms.estimators.CentroidEstimator
import cz.cuni.lf1.thunderstorm.algorithms.filters.BoxFilter
import cz.cuni.lf1.thunderstorm.algorithms.filters.CompoundWaveletFilter
import cz.cuni.lf1.thunderstorm.algorithms.filters.asRefreshableVariable
import cz.cuni.lf1.thunderstorm.algorithms.padding.DuplicatePadding
import cz.cuni.lf1.thunderstorm.datastructures.Point2DImpl
import cz.cuni.lf1.thunderstorm.datastructures.extensions.createGrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.extensions.createPoint2D
import cz.cuni.lf1.thunderstorm.parser.thresholding.FormulaThreshold
import cz.cuni.lf1.thunderstorm.test.assertListOfPointsEquals
import org.junit.Ignore
import org.junit.Test

internal class LocalizationPipelineTests {

    @Ignore // FIXME
    @Test
    public fun testPipeline() {
        val image = createGrayScaleImage(arrayOf(
                arrayOf(9.0, 9.0, 7.0, 7.0, 6.0),
                arrayOf(4.0, 6.0, 7.0, 5.0, 6.0),
                arrayOf(1.0, 1.0, 1.0, 1.0, 1.0),
                arrayOf(2.0, 3.0, 4.0, 4.0, 1.0),
                arrayOf(2.0, 3.0, 3.0, 1.0, 2.0)))

        val expected = listOf(createPoint2D(2.0, 3.0))

        val filter = CompoundWaveletFilter(3, 2.0, 5, ::DuplicatePadding)
        val thresholder = FormulaThreshold(
                "std(Wave.F1)",
                mapOf("" to filter.asRefreshableVariable(),
                        "Wave" to filter.asRefreshableVariable(),
                        "Box" to BoxFilter(3, ::DuplicatePadding).asRefreshableVariable()))

        val maxFilterResult = MaxFilterDetector(1, thresholder).detect(image) // [x,y] -> { [2,3], [3,3] }

        val results = LocalizationPipeline.runLocalization(
                image,
                filter,
                MaxFilterDetector(1, thresholder),
                CentroidEstimator(1))
            .map { Point2DImpl(it.xPos.getValue(), it.yPos.getValue()) }

        assertListOfPointsEquals(expected, results, 1e-2)
    }
}