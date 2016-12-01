package cz.cuni.lf1.thunderstorm.algorithms.padding

import cz.cuni.lf1.thunderstorm.datastructures.extensions.createGrayScaleImage
import cz.cuni.lf1.thunderstorm.test.assertGrayScaleImageEquals
import org.junit.Test

internal class ZeroPaddingTests {

    @Test
    public fun testPad() {
        val image = createGrayScaleImage(arrayOf(
                arrayOf(8.0, 1.0, 6.0),
                arrayOf(3.0, 5.0, 7.0),
                arrayOf(4.0, 9.0, 2.0)))

        val expected = createGrayScaleImage(arrayOf(
                arrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
                arrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
                arrayOf(0.0, 0.0, 8.0, 1.0, 6.0, 0.0, 0.0),
                arrayOf(0.0, 0.0, 3.0, 5.0, 7.0, 0.0, 0.0),
                arrayOf(0.0, 0.0, 4.0, 9.0, 2.0, 0.0, 0.0),
                arrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
                arrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)))

        val result = ZeroPadding(2).pad(image)

        assertGrayScaleImageEquals(expected, result, 0.0)
    }
}