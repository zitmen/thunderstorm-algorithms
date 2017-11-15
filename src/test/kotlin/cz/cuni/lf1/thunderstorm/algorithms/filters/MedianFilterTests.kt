package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.datastructures.extensions.createGrayScaleImage
import cz.cuni.lf1.thunderstorm.test.assertGrayScaleImageEquals
import org.junit.Test

internal class MedianFilterTests {

    @Test
    public fun testCrossFilter() {
        val result = MedianFilter(MedianFilter.Pattern.CROSS, 3).filter(image)

        val expected = createGrayScaleImage(arrayOf(
                arrayOf(3.0, 3.0, 3.0, 3.0, 3.0),
                arrayOf(4.5, 6.0, 7.0, 7.0, 5.0),
                arrayOf(4.5, 6.0, 7.0, 7.0, 5.0),
                arrayOf(4.0, 6.0, 7.0, 7.0, 5.0),
                arrayOf(4.0, 5.5, 5.5, 5.5, 5.0)))

        assertGrayScaleImageEquals(expected, result, 0.0)
    }

    @Test
    public fun testBoxFilter() {
        val result = MedianFilter(MedianFilter.Pattern.BOX, 3).filter(image)

        val expected = createGrayScaleImage(arrayOf(
                arrayOf(4.0, 4.0, 4.5, 4.0, 4.0),
                arrayOf(4.5, 5.0, 6.0, 5.0, 5.0),
                arrayOf(5.5, 6.0, 7.0, 7.0, 6.0),
                arrayOf(4.5, 6.0, 7.0, 6.0, 5.0),
                arrayOf(4.5, 5.5, 6.5, 5.5, 5.0)))

        assertGrayScaleImageEquals(expected, result, 0.0)
    }
}

private val image = createGrayScaleImage(arrayOf(
        arrayOf(3.0, 3.0, 3.0, 3.0, 2.0),
        arrayOf(5.0, 6.0, 7.0, 7.0, 5.0),
        arrayOf(4.0, 6.0, 7.0, 7.0, 5.0),
        arrayOf(4.0, 7.0, 8.0, 8.0, 5.0),
        arrayOf(2.0, 5.0, 6.0, 5.0, 2.0)))