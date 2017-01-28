package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImageImpl
import cz.cuni.lf1.thunderstorm.algorithms.padding.DuplicatePadding
import cz.cuni.lf1.thunderstorm.test.assertGrayScaleImageEquals
import org.junit.Test

internal class DifferenceOfBoxesFilterTests {

    @Test
    public fun testFilterB1ltB2() {
        val expected = GrayScaleImageImpl(arrayOf(
                arrayOf(0.0360, 0.0225,  0.0090, -0.0045, -0.0180),
                arrayOf(0.0315, 0.0180,  0.0045, -0.0090, -0.0225),
                arrayOf(0.0270, 0.0135, -0.0000, -0.0135, -0.0270),
                arrayOf(0.0225, 0.0090, -0.0045, -0.0180, -0.0315),
                arrayOf(0.0180, 0.0045, -0.0090, -0.0225, -0.0360)))

        val result = DifferenceOfBoxesFilters(11, 13, ::DuplicatePadding).filter(image)
        assertGrayScaleImageEquals(expected, result, 1e-4)
    }

    @Test
    public fun testFilterB1gtB2() {
        val expected = GrayScaleImageImpl(arrayOf(
                arrayOf(-0.0360, -0.0225, -0.0090, 0.0045, 0.0180),
                arrayOf(-0.0315, -0.0180, -0.0045, 0.0090, 0.0225),
                arrayOf(-0.0270, -0.0135,  0.0000, 0.0135, 0.0270),
                arrayOf(-0.0225, -0.0090,  0.0045, 0.0180, 0.0315),
                arrayOf(-0.0180, -0.0045,  0.0090, 0.0225, 0.0360)))

        val result = DifferenceOfBoxesFilters(13, 11, ::DuplicatePadding).filter(image)

        assertGrayScaleImageEquals(expected, result, 1e-4)
    }

    @Test
    public fun testFilterB1eqB2() {
        val expected = GrayScaleImageImpl(arrayOf(
                arrayOf(0.0, 0.0, 0.0, 0.0, 0.0),
                arrayOf(0.0, 0.0, 0.0, 0.0, 0.0),
                arrayOf(0.0, 0.0, 0.0, 0.0, 0.0),
                arrayOf(0.0, 0.0, 0.0, 0.0, 0.0),
                arrayOf(0.0, 0.0, 0.0, 0.0, 0.0)))

        val result = DifferenceOfBoxesFilters(13, 13, ::DuplicatePadding).filter(image)

        assertGrayScaleImageEquals(expected, result, 0.0)
    }
}

private val image = GrayScaleImageImpl(arrayOf(
        arrayOf(17.0, 23.0,  4.0, 10.0, 11.0),
        arrayOf(24.0,  5.0,  6.0, 12.0, 18.0),
        arrayOf( 1.0,  7.0, 13.0, 19.0, 25.0),
        arrayOf( 8.0, 14.0, 20.0, 21.0,  2.0),
        arrayOf(15.0, 16.0, 22.0,  3.0,  9.0)))