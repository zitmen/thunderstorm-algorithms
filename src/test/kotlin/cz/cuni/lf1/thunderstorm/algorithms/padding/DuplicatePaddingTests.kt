package cz.cuni.lf1.thunderstorm.algorithms.padding

import cz.cuni.lf1.thunderstorm.datastructures.extensions.createGrayScaleImage
import cz.cuni.lf1.thunderstorm.test.assertGrayScaleImageEquals
import org.junit.Test

internal class DuplicatePaddingTests {

    @Test
    public fun testPad() {
        val image = createGrayScaleImage(arrayOf(
                arrayOf(8.0, 1.0, 6.0),
                arrayOf(3.0, 5.0, 7.0),
                arrayOf(4.0, 9.0, 2.0)))

        val expected = createGrayScaleImage(arrayOf(
                arrayOf(8.0, 8.0, 8.0, 1.0, 6.0, 6.0, 6.0),
                arrayOf(8.0, 8.0, 8.0, 1.0, 6.0, 6.0, 6.0),
                arrayOf(8.0, 8.0, 8.0, 1.0, 6.0, 6.0, 6.0),
                arrayOf(3.0, 3.0, 3.0, 5.0, 7.0, 7.0, 7.0),
                arrayOf(4.0, 4.0, 4.0, 9.0, 2.0, 2.0, 2.0),
                arrayOf(4.0, 4.0, 4.0, 9.0, 2.0, 2.0, 2.0),
                arrayOf(4.0, 4.0, 4.0, 9.0, 2.0, 2.0, 2.0)))

        val result = DuplicatePadding(2).pad(image)

        assertGrayScaleImageEquals(expected, result, 0.0)
    }
}