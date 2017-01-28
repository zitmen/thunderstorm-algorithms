package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImageImpl
import cz.cuni.lf1.thunderstorm.test.assertGrayScaleImageEquals
import cz.cuni.lf1.thunderstorm.algorithms.padding.DuplicatePadding
import org.junit.Test

public class WaveletFilterTests {

    @Test
    public fun testFilterPlane1() {
        val expected = GrayScaleImageImpl(arrayOf(
                arrayOf(16.8945, 14.0664, 11.0000, 10.7656, 12.4492),
                arrayOf(13.7773, 11.9453, 11.3203, 13.0390, 15.3085),
                arrayOf(10.3984, 11.1835, 13.0000, 14.8164, 15.6015),
                arrayOf(10.6914, 12.9609, 14.6796, 14.0546, 12.2226),
                arrayOf(13.5507, 15.2343, 15.0000, 11.9335,  9.1055)))

        val result = WaveletFilter(1, 3, 2.0, 5, ::DuplicatePadding).filter(image)

        assertGrayScaleImageEquals(expected, result, 1e-4)
    }

    @Test
    public fun testFilterPlane2() {
        val expected = GrayScaleImageImpl(arrayOf(
                arrayOf(11.5937, 14.8359, 11.2812, 14.3671, 12.5312),
                arrayOf(15.0312, 14.4453, 12.6093, 12.8828, 11.2812),
                arrayOf(11.3593, 12.5703, 13.0000, 13.4296, 14.6406),
                arrayOf(14.7187, 13.1171, 13.3906, 11.5546, 10.9687),
                arrayOf(13.4687, 11.6328, 14.7187, 11.1640, 14.4062)))

        val result = WaveletFilter(2, 3, 2.0, 5, ::DuplicatePadding).filter(image)

        assertGrayScaleImageEquals(expected, result, 1e-4)
    }

    @Test
    public fun testFilterPlane3() {
        val expected = GrayScaleImageImpl(arrayOf(
                arrayOf(14.5000, 16.1640, 11.9687, 11.2890, 12.2500),
                arrayOf(17.1250, 15.2734, 13.8906, 13.2109, 14.8750),
                arrayOf(12.0156, 13.6796, 13.0000, 12.3203, 13.9843),
                arrayOf(11.1250, 12.7890, 12.1093, 10.7265,  8.8750),
                arrayOf(13.7500, 14.7109, 14.0312,  9.8359, 11.5000)))

        val result = WaveletFilter(3, 3, 2.0, 5, ::DuplicatePadding).filter(image)

        assertGrayScaleImageEquals(expected, result, 1e-4)
    }
}

private val image = GrayScaleImageImpl(arrayOf(
        arrayOf(17.0, 23.0,  4.0, 10.0, 11.0),
        arrayOf(24.0,  5.0,  6.0, 12.0, 18.0),
        arrayOf( 1.0,  7.0, 13.0, 19.0, 25.0),
        arrayOf( 8.0, 14.0, 20.0, 21.0,  2.0),
        arrayOf(15.0, 16.0, 22.0,  3.0,  9.0)))