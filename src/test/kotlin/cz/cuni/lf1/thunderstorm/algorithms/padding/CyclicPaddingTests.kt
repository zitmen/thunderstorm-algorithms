package cz.cuni.lf1.thunderstorm.algorithms.padding

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.test.assertGrayScaleImageEquals
import org.junit.Test

internal class CyclicPaddingTests {

    @Test
    public fun testPad() {
        val image = GrayScaleImage(arrayOf(
                arrayOf(8.0, 1.0, 6.0),
                arrayOf(3.0, 5.0, 7.0),
                arrayOf(4.0, 9.0, 2.0)))

        val expected = GrayScaleImage(arrayOf(
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
}