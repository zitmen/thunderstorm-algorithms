package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImageImpl
import cz.cuni.lf1.thunderstorm.algorithms.padding.DuplicatePadding
import cz.cuni.lf1.thunderstorm.test.assertGrayScaleImageEquals
import org.junit.Test

public class DifferenceOfGaussiansFilterTests {

    @Test
    public fun testFilterG1ltG2() {
        val expected = GrayScaleImageImpl(arrayOf(
                arrayOf( 2.5340,  0.5733, -2.2044, -1.9335, -0.2225),
                arrayOf( 0.3583, -1.3897, -1.9307,  0.0934,  2.3832),
                arrayOf(-2.9332, -2.0697, -0.0000,  2.0697,  2.9332),
                arrayOf(-2.3832, -0.0934,  1.9307,  1.3897, -0.3583),
                arrayOf( 0.2225,  1.9335,  2.2044, -0.5733, -2.5340)))

        val result = DifferenceOfGaussiansFilter(13, 1.0, 2.0, ::DuplicatePadding).filter(image)
        assertGrayScaleImageEquals(expected, result, 1e-4)
    }

    @Test
    public fun testFilterG1gtG2() {
        val expected = GrayScaleImageImpl(arrayOf(
                arrayOf(-2.5340, -0.5733,  2.2044,  1.9335,  0.2225),
                arrayOf(-0.3583,  1.3897,  1.9307, -0.0934, -2.3832),
                arrayOf( 2.9332,  2.0697,  0.0000, -2.0697, -2.9332),
                arrayOf( 2.3832,  0.0934, -1.9307, -1.3897,  0.3583),
                arrayOf(-0.2225, -1.9335, -2.2044,  0.5733,  2.5340)))

        val result = DifferenceOfGaussiansFilter(13, 2.0, 1.0, ::DuplicatePadding).filter(image)

        assertGrayScaleImageEquals(expected, result, 1e-4)
    }

    @Test
    public fun testFilterG1eqG2() {
        val expected = GrayScaleImageImpl(arrayOf(
                arrayOf(0.0, 0.0, 0.0, 0.0, 0.0),
                arrayOf(0.0, 0.0, 0.0, 0.0, 0.0),
                arrayOf(0.0, 0.0, 0.0, 0.0, 0.0),
                arrayOf(0.0, 0.0, 0.0, 0.0, 0.0),
                arrayOf(0.0, 0.0, 0.0, 0.0, 0.0)))

        val result = DifferenceOfGaussiansFilter(13, 2.0, 2.0, ::DuplicatePadding).filter(image)

        assertGrayScaleImageEquals(expected, result, 0.0)
    }
}

private val image = GrayScaleImageImpl(arrayOf(
        arrayOf(17.0, 23.0,  4.0, 10.0, 11.0),
        arrayOf(24.0,  5.0,  6.0, 12.0, 18.0),
        arrayOf( 1.0,  7.0, 13.0, 19.0, 25.0),
        arrayOf( 8.0, 14.0, 20.0, 21.0,  2.0),
        arrayOf(15.0, 16.0, 22.0,  3.0,  9.0)))