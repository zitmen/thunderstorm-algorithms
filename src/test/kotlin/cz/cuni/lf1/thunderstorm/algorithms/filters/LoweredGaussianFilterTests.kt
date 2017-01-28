package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImageImpl
import cz.cuni.lf1.thunderstorm.algorithms.padding.DuplicatePadding
import cz.cuni.lf1.thunderstorm.test.assertGrayScaleImageEquals
import org.junit.Test

public class LoweredGaussianFilterTests {

    @Test
    public fun testFilter() {
        val image = GrayScaleImageImpl(arrayOf(
                arrayOf(17.0, 23.0,  4.0, 10.0, 11.0),
                arrayOf(24.0,  5.0,  6.0, 12.0, 18.0),
                arrayOf( 1.0,  7.0, 13.0, 19.0, 25.0),
                arrayOf( 8.0, 14.0, 20.0, 21.0,  2.0),
                arrayOf(15.0, 16.0, 22.0,  3.0,  9.0)))

        val expected = GrayScaleImageImpl(arrayOf(
                arrayOf( 0.7674,  0.2533, -0.1876, -0.2871,  0.0000),
                arrayOf( 0.0246, -0.0693, -0.0820,  0.1153,  0.5170),
                arrayOf(-0.5119, -0.2449,  0.0000,  0.2449,  0.5119),
                arrayOf(-0.5170, -0.1153,  0.0820,  0.0693, -0.0246),
                arrayOf(-0.0000,  0.2871,  0.1876, -0.2533, -0.7674)))

        val result = LoweredGaussianFilter(13, 2.0, ::DuplicatePadding).filter(image)

        assertGrayScaleImageEquals(expected, result, 1e-4)
    }

}