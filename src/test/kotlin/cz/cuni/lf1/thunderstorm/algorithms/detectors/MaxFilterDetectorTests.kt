package cz.cuni.lf1.thunderstorm.algorithms.detectors

import cz.cuni.lf1.thunderstorm.datastructures.Point2D
import cz.cuni.lf1.thunderstorm.datastructures.extensions.createGrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.extensions.createPoint2D
import cz.cuni.lf1.thunderstorm.parser.thresholding.FormulaThreshold
import cz.cuni.lf1.thunderstorm.test.assertListOfPointsEquals
import org.junit.Test

internal class MaxFilterDetectorTests {
    
    @Test
    public fun testDetect1() {
        val image = createGrayScaleImage(arrayOf(
                arrayOf(9.0, 9.0, 7.0, 7.0, 6.0),
                arrayOf(4.0, 6.0, 7.0, 5.0, 6.0),
                arrayOf(1.0, 1.0, 1.0, 1.0, 1.0),
                arrayOf(2.0, 3.0, 4.0, 0.0, 1.0),
                arrayOf(2.0, 3.0, 3.0, 1.0, 2.0)))

        val expected = listOf(createPoint2D(2.0, 3.0))

        val result = MaxFilterDetector(1, FormulaThreshold("3.0")).detect(image)

        assertListOfPointsEquals(expected, result, 0.0)
    }

    @Test
    public fun testDetect2() {
        val image = createGrayScaleImage(arrayOf(
                arrayOf(9.0, 9.0, 7.0, 7.0, 6.0),
                arrayOf(4.0, 6.0, 7.0, 5.0, 6.0),
                arrayOf(1.0, 1.0, 1.0, 1.0, 1.0),
                arrayOf(2.0, 3.0, 4.0, 0.0, 1.0),
                arrayOf(2.0, 3.0, 3.0, 1.0, 2.0)))

        val expected = emptyList<Point2D>()

        val result = MaxFilterDetector(1, FormulaThreshold("5.0")).detect(image)

        assertListOfPointsEquals(expected, result, 0.0)
    }

    @Test
    public fun testDetect3() {
        val image = createGrayScaleImage(arrayOf(
                arrayOf(9.0, 9.0, 7.0, 7.0, 6.0),
                arrayOf(4.0, 6.0, 7.0, 5.0, 6.0),
                arrayOf(1.0, 1.0, 1.0, 1.0, 1.0),
                arrayOf(2.0, 3.0, 4.0, 4.0, 1.0),
                arrayOf(2.0, 3.0, 3.0, 1.0, 2.0)))

        val expected = listOf(createPoint2D(2.0, 3.0), createPoint2D(3.0, 3.0))

        val result = MaxFilterDetector(1, FormulaThreshold("3.0")).detect(image)

        assertListOfPointsEquals(expected, result, 0.0)
    }
}