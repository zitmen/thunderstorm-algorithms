package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.datastructures.extensions.createGrayScaleImage
import cz.cuni.lf1.thunderstorm.algorithms.padding.DuplicatePadding
import cz.cuni.lf1.thunderstorm.test.assertGrayScaleImageEquals
import org.junit.Test

internal class BoxFilterTests {
    
    @Test
    public fun testFilter() {
        val image = createGrayScaleImage(arrayOf(
                arrayOf(38.8743, 33.7818, 32.7879, 36.5015, 27.9572),
                arrayOf(58.4699, 67.8308, 70.8481, 76.3634, 56.8981),
                arrayOf(47.2979, 69.7437, 75.9145, 77.4943, 50.5909),
                arrayOf(43.6674, 77.0326, 81.3179, 82.0897, 55.1332),
                arrayOf(27.8264, 54.5190, 66.6193, 50.0506, 26.8428)))

        val result = BoxFilter(5, ::DuplicatePadding).filter(image)

        val expected = createGrayScaleImage(arrayOf(
                arrayOf(46.0487, 47.6876, 46.4464, 45.2052, 43.3028),
                arrayOf(50.2951, 53.5658, 53.2199, 52.8740, 50.3287),
                arrayOf(51.1521, 55.4067, 55.4581, 55.5096, 52.0902),
                arrayOf(52.0091, 57.2476, 57.6963, 58.1451, 53.8516),
                arrayOf(47.6303, 53.0419, 53.5143, 53.9866, 49.0233)))

        assertGrayScaleImageEquals(expected, result, 1e-4)
    }
}