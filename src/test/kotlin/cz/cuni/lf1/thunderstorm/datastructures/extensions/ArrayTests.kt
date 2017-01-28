package cz.cuni.lf1.thunderstorm.datastructures.extensions

import cz.cuni.lf1.thunderstorm.test.assert2DDoubleArrayEquals
import org.junit.Test
import kotlin.test.assertEquals

internal class ArrayTests {

    @Test
    public fun testCreate2DDoubleArray() {
        val expected = arrayOf(
                arrayOf(5.0, 5.0),
                arrayOf(5.0, 5.0),
                arrayOf(5.0, 5.0))

        val result = create2DDoubleArray(3, 2, 5.0)

        assert2DDoubleArrayEquals(expected, result, 0.0)
    }

    @Test
    public fun testCopyDataToPositionTopLeft() {
        val image = createGrayScaleImage(create2DDoubleArray(2, 2, 5.0))

        val expected = arrayOf(
                arrayOf(5.0, 5.0, 0.0),
                arrayOf(5.0, 5.0, 0.0),
                arrayOf(0.0, 0.0, 0.0))

        val result = create2DDoubleArray(3, 3, 0.0)
        result.copyDataToPosition(image, 0, 0)

        assert2DDoubleArrayEquals(expected, result, 0.0)
    }

    @Test
    public fun testCopyDataToPositionBottomRight() {
        val image = createGrayScaleImage(create2DDoubleArray(2, 2, 5.0))

        val expected = arrayOf(
                arrayOf(0.0, 0.0, 0.0),
                arrayOf(0.0, 5.0, 5.0),
                arrayOf(0.0, 5.0, 5.0))

        val result = create2DDoubleArray(3, 3, 0.0)
        result.copyDataToPosition(image, 1, 1)

        assert2DDoubleArrayEquals(expected, result, 0.0)
    }

    @Test
    public fun testFillRectangle() {
        val expected = arrayOf(
                arrayOf(0.0, 0.0, 0.0),
                arrayOf(0.0, 3.0, 0.0),
                arrayOf(0.0, 3.0, 0.0))

        val result = create2DDoubleArray(3, 3, 0.0)
        result.fillRectangle(1, 1, 2, 1, 3.0)

        assert2DDoubleArrayEquals(expected, result, 0.0)
    }

    @Test
    public fun testOddMedian() {
        val result = arrayOf(5.0, 3.0, 3.0, 1.0, 999.0, 6.0, 11.0).median()
        val expected = 5.0

        assertEquals(expected, result)
    }

    @Test
    public fun testEvenMedian() {
        val result = arrayOf(5.0, 3.0, 3.0, 1.0, 999.0, 6.0).median()
        val expected = 4.0

        assertEquals(expected, result)
    }
}