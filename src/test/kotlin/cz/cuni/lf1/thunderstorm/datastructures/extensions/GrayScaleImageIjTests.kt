package cz.cuni.lf1.thunderstorm.datastructures.extensions

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImageImplIj
import cz.cuni.lf1.thunderstorm.test.assertGrayScaleImageEquals
import ij.process.FloatProcessor
import org.junit.Assert.assertArrayEquals
import org.junit.Test

public class GrayScaleImageIjTests {

    @Test
    public fun testConversionToIj() {
        val image = createGrayScaleImage(arrayOf(
                arrayOf(3.0, 6.0),
                arrayOf(2.0, 5.0),
                arrayOf(1.0, 4.0)))

        val expected = FloatProcessor(arrayOf(
                floatArrayOf(3f, 2f, 1f),
                floatArrayOf(6f, 5f, 4f)))

        val result = GrayScaleImageImplIj.convertToFloatProcessor(image)

        assertArrayEquals(
                (expected.pixels as FloatArray).map { it.toInt() }.toIntArray(),
                (result.pixels as FloatArray).map { it.toInt() }.toIntArray())
    }

    @Test
    public fun testConversionFromIj() {
        val image = FloatProcessor(arrayOf(
                floatArrayOf(3f, 2f, 1f),
                floatArrayOf(6f, 5f, 4f)))

        val expected = createGrayScaleImage(arrayOf(
                arrayOf(3.0, 6.0),
                arrayOf(2.0, 5.0),
                arrayOf(1.0, 4.0)))

        val result = GrayScaleImageImplIj(image)

        assertGrayScaleImageEquals(expected, result, 0.0)
    }
}