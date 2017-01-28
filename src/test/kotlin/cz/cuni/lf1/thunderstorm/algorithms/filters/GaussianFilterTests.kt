package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImageImpl
import cz.cuni.lf1.thunderstorm.algorithms.padding.DuplicatePadding
import cz.cuni.lf1.thunderstorm.test.assertGrayScaleImageEquals
import org.junit.Test

public class GaussianFilterTests {

    @Test
    public fun testFilter() {
        val image = GrayScaleImageImpl(arrayOf(
                arrayOf(17.0, 23.0,  4.0, 10.0, 11.0),
                arrayOf(24.0,  5.0,  6.0, 12.0, 18.0),
                arrayOf( 1.0,  7.0, 13.0, 19.0, 25.0),
                arrayOf( 8.0, 14.0, 20.0, 21.0,  2.0),
                arrayOf(15.0, 16.0, 22.0,  3.0,  9.0)))

        val expected = GrayScaleImageImpl(arrayOf(
                arrayOf(14.5248, 13.7266, 13.0018, 12.6182, 12.6214),
                arrayOf(13.6873, 13.3094, 13.0127, 12.9260, 13.0437),
                arrayOf(13.0562, 13.0391, 13.0000, 12.9609, 12.9438),
                arrayOf(12.9563, 13.0740, 12.9873, 12.6906, 12.3127),
                arrayOf(13.3786, 13.3818, 12.9982, 12.2734, 11.4752)))

        val result = GaussianFilter(13, 2.0, ::DuplicatePadding).filter(image)

        assertGrayScaleImageEquals(expected, result, 1e-4)
    }

}