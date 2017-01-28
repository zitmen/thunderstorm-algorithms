package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImageImpl
import cz.cuni.lf1.thunderstorm.algorithms.padding.ZeroPadding
import cz.cuni.lf1.thunderstorm.test.assertGrayScaleImageEquals
import org.junit.Test

public class CompoundWaveletFilterTests {

    @Test
    public fun testFilter() {
        val image = GrayScaleImageImpl(arrayOf(
                arrayOf(17.0, 23.0,  4.0, 10.0, 11.0),
                arrayOf(24.0,  5.0,  6.0, 12.0, 18.0),
                arrayOf( 1.0,  7.0, 13.0, 19.0, 25.0),
                arrayOf( 8.0, 14.0, 20.0, 21.0,  2.0),
                arrayOf(15.0, 16.0, 22.0,  3.0,  9.0)))

        val expected = GrayScaleImageImpl(arrayOf(
                arrayOf( 3.2070,  0.6970, -1.8962, -1.7329,  0.2973),
                arrayOf( 0.3748, -1.3554, -1.7229,  0.2524,  2.8094),
                arrayOf(-2.6577, -1.9432,  0.0000,  2.0448,  3.1655),
                arrayOf(-2.2020, -0.0496,  1.8245,  1.5581,  0.2325),
                arrayOf( 0.7083,  2.3403,  2.4040, -0.0896, -2.2013)))

        val result = CompoundWaveletFilter(3, 2.0, 5, ::ZeroPadding).filter(image)

        assertGrayScaleImageEquals(expected, result, 1e-4)
    }
}