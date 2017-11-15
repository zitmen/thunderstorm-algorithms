package cz.cuni.lf1.thunderstorm.test

import cz.cuni.lf1.thunderstorm.datastructures.*
import cz.cuni.lf1.thunderstorm.datastructures.extensions.abs
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal fun assertGrayScaleImageEquals(expected: GrayScaleImage, actual: GrayScaleImage, delta: Double, message: String? = null) {
    assertTrue(expected.getHeight() == actual.getHeight(), message)
    assertTrue(expected.getWidth() == actual.getWidth(), message)

    for (y in 0..(expected.getHeight() - 1)) {
        for (x in 0..(expected.getWidth() - 1)) {
            val diff = (expected.getValue(y, x) - actual.getValue(y, x)).abs()
            assertTrue(diff <= delta, "${message ?: ""} (row=$y, col=$x, diff=$diff)")
        }
    }
}

internal fun assert2DDoubleArrayEquals(expected: Array<Array<Double>>, actual: Array<Array<Double>>, delta: Double, message: String? = null) {
    assertTrue(expected.size == actual.size, message)

    for (y in 0..(expected.size - 1)) {
        assertTrue(expected[y].size == actual[y].size, message)

        for (x in 0..(expected[y].size - 1)) {
            assertTrue((expected[y][x] - actual[y][x]).abs() <= delta, message)
        }
    }
}

internal fun assertListOfMoleculesEquals(expected: List<Molecule>, actual: List<Molecule>, delta: Double, message: String? = null) {
    assertTrue(expected.size == actual.size, message)
    (0..(expected.size - 1)).forEach {
        assertMolecule(expected[it], actual[it], delta, message)
    }
}

internal fun assertMolecule(expected: Molecule, actual: Molecule, delta: Double, message: String? = null) {
    assertPhysicalQuantityEquals(expected.xPos, actual.xPos, delta, message)
    assertPhysicalQuantityEquals(expected.yPos, actual.yPos, delta, message)
    assertPhysicalQuantityEquals(expected.zPos, actual.zPos, delta, message)
    assertEquals(expected.params.keys, actual.params.keys)
    expected.params.forEach {
        assertPhysicalQuantityEquals(it.value, actual.params[it.key]!!, delta, message)
    }
}

internal fun assertPhysicalQuantityEquals(expected: PhysicalQuantity, actual: PhysicalQuantity, delta: Double, message: String? = null) {
    assertEquals(expected.getUnit(), actual.getUnit(), message)
    assertDoubleEquals(expected.getValue(), actual.getValue(), delta, message)
}

internal fun assertDoubleEquals(expected: Double, actual: Double, delta: Double, message: String? = null) {
    assertTrue((expected - actual).abs() <= delta, message)
}