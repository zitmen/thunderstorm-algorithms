package cz.cuni.lf1.thunderstorm.algorithms.padding

import cz.cuni.lf1.thunderstorm.datastructures.extensions.createGrayScaleImage
import cz.cuni.lf1.thunderstorm.test.assertGrayScaleImageEquals
import org.junit.Test

internal class CyclicPaddingTests {

    @Test
    public fun testPad1() {
        val image = createGrayScaleImage(arrayOf(
                arrayOf(8.0, 1.0, 6.0),
                arrayOf(3.0, 5.0, 7.0),
                arrayOf(4.0, 9.0, 2.0)))

        val expected = createGrayScaleImage(arrayOf(
            arrayOf(5.0, 7.0, 3.0, 5.0, 7.0, 3.0, 5.0, 7.0, 3.0, 5.0, 7.0, 3.0, 5.0),
            arrayOf(9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0),
            arrayOf(1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0),
            arrayOf(5.0, 7.0, 3.0, 5.0, 7.0, 3.0, 5.0, 7.0, 3.0, 5.0, 7.0, 3.0, 5.0),
            arrayOf(9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0),
            arrayOf(1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0),
            arrayOf(5.0, 7.0, 3.0, 5.0, 7.0, 3.0, 5.0, 7.0, 3.0, 5.0, 7.0, 3.0, 5.0),
            arrayOf(9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0),
            arrayOf(1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0),
            arrayOf(5.0, 7.0, 3.0, 5.0, 7.0, 3.0, 5.0, 7.0, 3.0, 5.0, 7.0, 3.0, 5.0),
            arrayOf(9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0),
            arrayOf(1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0),
            arrayOf(5.0, 7.0, 3.0, 5.0, 7.0, 3.0, 5.0, 7.0, 3.0, 5.0, 7.0, 3.0, 5.0)))

        val result = CyclicPadding(5).pad(image)

        assertGrayScaleImageEquals(expected, result, 0.0)
    }

    @Test
    public fun testPad2() {
        val image = createGrayScaleImage(arrayOf(
                arrayOf(8.0, 1.0, 6.0),
                arrayOf(4.0, 9.0, 2.0)))

        val expected = createGrayScaleImage(arrayOf(
                arrayOf(9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0),
                arrayOf(1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0),
                arrayOf(9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0),
                arrayOf(1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0),
                arrayOf(9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0),
                arrayOf(1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0),
                arrayOf(9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0),
                arrayOf(1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0),
                arrayOf(9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0),
                arrayOf(1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0),
                arrayOf(9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0, 2.0, 4.0, 9.0),
                arrayOf(1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0, 6.0, 8.0, 1.0)))

        val result = CyclicPadding(5).pad(image)

        assertGrayScaleImageEquals(expected, result, 0.0)
    }

    @Test
    public fun testPad3() {
        val image = createGrayScaleImage(arrayOf(
                arrayOf(8.0, 6.0),
                arrayOf(3.0, 7.0),
                arrayOf(4.0, 2.0)))

        val expected = createGrayScaleImage(arrayOf(
                arrayOf(7.0, 3.0, 7.0, 3.0, 7.0, 3.0, 7.0, 3.0, 7.0, 3.0, 7.0, 3.0),
                arrayOf(2.0, 4.0, 2.0, 4.0, 2.0, 4.0, 2.0, 4.0, 2.0, 4.0, 2.0, 4.0),
                arrayOf(6.0, 8.0, 6.0, 8.0, 6.0, 8.0, 6.0, 8.0, 6.0, 8.0, 6.0, 8.0),
                arrayOf(7.0, 3.0, 7.0, 3.0, 7.0, 3.0, 7.0, 3.0, 7.0, 3.0, 7.0, 3.0),
                arrayOf(2.0, 4.0, 2.0, 4.0, 2.0, 4.0, 2.0, 4.0, 2.0, 4.0, 2.0, 4.0),
                arrayOf(6.0, 8.0, 6.0, 8.0, 6.0, 8.0, 6.0, 8.0, 6.0, 8.0, 6.0, 8.0),
                arrayOf(7.0, 3.0, 7.0, 3.0, 7.0, 3.0, 7.0, 3.0, 7.0, 3.0, 7.0, 3.0),
                arrayOf(2.0, 4.0, 2.0, 4.0, 2.0, 4.0, 2.0, 4.0, 2.0, 4.0, 2.0, 4.0),
                arrayOf(6.0, 8.0, 6.0, 8.0, 6.0, 8.0, 6.0, 8.0, 6.0, 8.0, 6.0, 8.0),
                arrayOf(7.0, 3.0, 7.0, 3.0, 7.0, 3.0, 7.0, 3.0, 7.0, 3.0, 7.0, 3.0),
                arrayOf(2.0, 4.0, 2.0, 4.0, 2.0, 4.0, 2.0, 4.0, 2.0, 4.0, 2.0, 4.0),
                arrayOf(6.0, 8.0, 6.0, 8.0, 6.0, 8.0, 6.0, 8.0, 6.0, 8.0, 6.0, 8.0),
                arrayOf(7.0, 3.0, 7.0, 3.0, 7.0, 3.0, 7.0, 3.0, 7.0, 3.0, 7.0, 3.0)))

        val result = CyclicPadding(5).pad(image)

        assertGrayScaleImageEquals(expected, result, 0.0)
    }
}