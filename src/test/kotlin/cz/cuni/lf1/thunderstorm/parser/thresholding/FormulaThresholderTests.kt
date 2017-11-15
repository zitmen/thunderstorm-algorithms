package cz.cuni.lf1.thunderstorm.parser.thresholding

import cz.cuni.lf1.thunderstorm.algorithms.filters.BoxFilter
import cz.cuni.lf1.thunderstorm.algorithms.filters.FilterWithVariables
import cz.cuni.lf1.thunderstorm.algorithms.padding.DuplicatePadding
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImageImpl
import cz.cuni.lf1.thunderstorm.test.assertDoubleEquals
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

internal class FormulaThresholderTests {

    @Test
    public fun testSimpleNumber() {
        val thr = FormulaThreshold("120.5", symbolTable).getValue(image)
        assertEquals(120.5, thr)
    }

    @Test
    public fun testFormula() {
        val thr = FormulaThreshold("mean(I+F)+1", symbolTable).getValue(image)
        assertEquals(7.0, thr)

        assertFails {
            FormulaThreshold("std(Wave.F)", symbolTable).getValue(image)  //unknown filter
        }
    }

    @Test
    public fun testStd() {
        val thr = FormulaThreshold("std(I)", symbolTable).getValue(image)
        assertDoubleEquals(1.4142, thr, 1e-4)
    }

    @Test
    public fun testMean() {
        val thr = FormulaThreshold("mean(I)", symbolTable).getValue(image)
        assertEquals(3.0, thr)
    }

    @Test
    public fun testMedian() {
        val thr = FormulaThreshold("median(I)", symbolTable).getValue(image)
        assertEquals(3.0, thr)
    }

    @Test
    public fun testMax() {
        val thr = FormulaThreshold("max(I)", symbolTable).getValue(image)
        assertEquals(5.0, thr)
    }

    @Test
    public fun testMin() {
        val thr = FormulaThreshold("min(I)", symbolTable).getValue(image)
        assertEquals(1.0, thr)
    }

    @Test
    public fun testSum() {
        val thr = FormulaThreshold("sum(I)", symbolTable).getValue(image)
        assertEquals(75.0, thr)
    }

    @Test
    public fun testAbs() {
        val thr = FormulaThreshold("sum(abs(-I))", symbolTable).getValue(image)
        assertEquals(75.0, thr)
    }

    @Test
    public fun testOtherFilter() {
        val thr = FormulaThreshold("min(Box.F)", symbolTable).getValue(image)
        assertDoubleEquals(1.3333, thr, 1e-4)
    }

    @Test
    public fun testMultiplication1() {
        val thr = FormulaThreshold("sum(2*I)", symbolTable).getValue(image)
        assertEquals(150.0, thr)
    }

    @Test
    public fun testMultiplication2() {
        val thr = FormulaThreshold("2*sum(I)", symbolTable).getValue(image)
        assertEquals(150.0, thr)
    }

    @Test
    public fun testExponentiation1() {
        val thr = FormulaThreshold("sum(I^2)", symbolTable).getValue(image)
        assertEquals(275.0, thr)
    }

    @Test
    public fun testExponentiation2() {
        val thr = FormulaThreshold("sum(I)^2", symbolTable).getValue(image)
        assertEquals(5625.0, thr)
    }

    @Test
    public fun testExponentiation3() {
        assertFails {
            FormulaThreshold("sum(2^I)", symbolTable).getValue(image)
        }
    }

    @Test
    public fun testExponentiation4() {
        assertFails {
            FormulaThreshold("sum(I^I)", symbolTable).getValue(image)
        }
    }

    @Test
    public fun testDivision1() {
        val thr = FormulaThreshold("sum(I/2)", symbolTable).getValue(image)
        assertEquals(37.5, thr)
    }

    @Test
    public fun testDivision2() {
        val thr = FormulaThreshold("sum(1/I)", symbolTable).getValue(image)
        assertDoubleEquals(11.4166, thr, 1e-4)
    }

    @Test
    public fun testDivision3() {
        val thr = FormulaThreshold("sum(I)/2", symbolTable).getValue(image)
        assertEquals(37.5, thr)
    }

    @Test
    public fun testDivision4() {
        val thr = FormulaThreshold("sum(I/I)", symbolTable).getValue(image)
        assertEquals(25.0, thr)
    }

    @Test
    public fun testDivision5() {
        val thr = FormulaThreshold("sum(I-1)", symbolTable).getValue(image)
        assertEquals(50.0, thr)
    }
}

private val image = GrayScaleImageImpl(arrayOf(
        arrayOf(1.0, 2.0, 3.0, 4.0, 5.0),
        arrayOf(1.0, 2.0, 3.0, 4.0, 5.0),
        arrayOf(1.0, 2.0, 3.0, 4.0, 5.0),
        arrayOf(1.0, 2.0, 3.0, 4.0, 5.0),
        arrayOf(1.0, 2.0, 3.0, 4.0, 5.0)))

private val symbolTable = mapOf(
        "" to RefreshableVariable(BoxFilter(3, ::DuplicatePadding) as FilterWithVariables),
        "Box" to RefreshableVariable(BoxFilter(3, ::DuplicatePadding) as FilterWithVariables))