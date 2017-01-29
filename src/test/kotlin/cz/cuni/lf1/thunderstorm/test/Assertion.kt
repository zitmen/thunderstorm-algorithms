package cz.cuni.lf1.thunderstorm.test

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.Point2D
import cz.cuni.lf1.thunderstorm.datastructures.extensions.abs
import kotlin.test.assertTrue

internal fun assertGrayScaleImageEquals(expected: GrayScaleImage, actual: GrayScaleImage, delta: Double, message: String? = null): Unit {
    assertTrue(expected.getHeight() == actual.getHeight(), message)
    assertTrue(expected.getWidth() == actual.getWidth(), message)

    for (y in 0..(expected.getHeight() - 1)) {
        for (x in 0..(expected.getWidth() - 1)) {
            val diff = (expected.getValue(y, x) - actual.getValue(y, x)).abs()
            assertTrue(diff <= delta, "${message ?: ""} (row=$y, col=$x, diff=$diff)")
        }
    }
}

internal fun assert2DDoubleArrayEquals(expected: Array<Array<Double>>, actual: Array<Array<Double>>, delta: Double, message: String? = null): Unit {
    assertTrue(expected.size == actual.size, message)

    for (y in 0..(expected.size - 1)) {
        assertTrue(expected[y].size == actual[y].size, message)

        for (x in 0..(expected[y].size - 1)) {
            assertTrue((expected[y][x] - actual[y][x]).abs() <= delta, message)
        }
    }
}

internal fun assertListOfPointsEquals(expected: List<Point2D>, actual: List<Point2D>, delta: Double, message: String? = null): Unit {
    assertTrue(expected.size == actual.size, message)

    for (i in 0..(expected.size - 1)) {
        assertTrue((expected[i].getX() - actual[i].getX()).abs() <= delta, message)
        assertTrue((expected[i].getY() - actual[i].getY()).abs() <= delta, message)
    }
}

internal fun assertDoubleEquals(expected: Double, actual: Double, delta: Double, message: String? = null): Unit {
    assertTrue((expected - actual).abs() <= delta, message)
}