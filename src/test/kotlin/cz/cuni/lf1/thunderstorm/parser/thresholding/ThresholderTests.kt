package cz.cuni.lf1.thunderstorm.parser.thresholding

import cz.cuni.lf1.thunderstorm.algorithms.filters.BoxFilter
import cz.cuni.lf1.thunderstorm.algorithms.padding.DuplicatePadding
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImageImpl
import cz.cuni.lf1.thunderstorm.test.assertDoubleEquals
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

internal class ThresholderTests {

    @Test
    public fun testSimpleNumber() {
        val thr = Thresholder("120.5", symbolTable).evaluate(image)
        assertEquals(120.5, thr)
    }

    @Test
    public fun testFormula() {
        val thr = Thresholder("mean(I+F)+1", symbolTable).evaluate(image)
        assertEquals(7.0, thr)

        assertFails {
            Thresholder("std(Wave.F)", symbolTable).evaluate(image) //unknown filter
        }
    }

    @Test
    public fun testStd() {
        val thr = Thresholder("std(I)", symbolTable).evaluate(image)
        assertDoubleEquals(1.4142, thr, 1e-4)
    }

    @Test
    public fun testMean() {
        val thr = Thresholder("mean(I)", symbolTable).evaluate(image)
        assertEquals(3.0, thr)
    }

    @Test
    public fun testMedian() {
        val thr = Thresholder("median(I)", symbolTable).evaluate(image)
        assertEquals(3.0, thr)
    }

    @Test
    public fun testMax() {
        val thr = Thresholder("max(I)", symbolTable).evaluate(image)
        assertEquals(5.0, thr)
    }

    @Test
    public fun testMin() {
        val thr = Thresholder("min(I)", symbolTable).evaluate(image)
        assertEquals(1.0, thr)
    }

    @Test
    public fun testSum() {
        val thr = Thresholder("sum(I)", symbolTable).evaluate(image)
        assertEquals(75.0, thr)
    }

    @Test
    public fun testAbs() {
        val thr = Thresholder("sum(abs(-I))", symbolTable).evaluate(image)
        assertEquals(75.0, thr)
    }

    @Test
    public fun testOtherFilter() {
        val thr = Thresholder("min(Box.F)", symbolTable).evaluate(image)
        assertDoubleEquals(1.3333, thr, 1e-4)
    }

    @Test
    public fun testMultiplication1() {
        val thr = Thresholder("sum(2*I)", symbolTable).evaluate(image)
        assertEquals(150.0, thr)
    }

    @Test
    public fun testMultiplication2() {
        val thr = Thresholder("2*sum(I)", symbolTable).evaluate(image)
        assertEquals(150.0, thr)
    }

    @Test
    public fun testExponentiation1() {
        val thr = Thresholder("sum(I^2)", symbolTable).evaluate(image)
        assertEquals(275.0, thr)
    }

    @Test
    public fun testExponentiation2() {
        val thr = Thresholder("sum(I)^2", symbolTable).evaluate(image)
        assertEquals(5625.0, thr)
    }

    @Test
    public fun testExponentiation3() {
        assertFails {
            Thresholder("sum(2^I)", symbolTable).evaluate(image)
        }
    }

    @Test
    public fun testExponentiation4() {
        assertFails {
            Thresholder("sum(I^I)", symbolTable).evaluate(image)
        }
    }

    @Test
    public fun testDivision1() {
        val thr = Thresholder("sum(I/2)", symbolTable).evaluate(image)
        assertEquals(37.5, thr)
    }

    @Test
    public fun testDivision2() {
        val thr = Thresholder("sum(1/I)", symbolTable).evaluate(image)
        assertDoubleEquals(11.4166, thr, 1e-4)
    }

    @Test
    public fun testDivision3() {
        val thr = Thresholder("sum(I)/2", symbolTable).evaluate(image)
        assertEquals(37.5, thr)
    }

    @Test
    public fun testDivision4() {
        val thr = Thresholder("sum(I/I)", symbolTable).evaluate(image)
        assertEquals(25.0, thr)
    }

    @Test
    public fun testDivision5() {
        val thr = Thresholder("sum(I-1)", symbolTable).evaluate(image)
        assertEquals(50.0, thr)
    }
}

private val image = GrayScaleImageImpl(arrayOf(
        arrayOf(1.0, 2.0, 3.0, 4.0, 5.0),
        arrayOf(1.0, 2.0, 3.0, 4.0, 5.0),
        arrayOf(1.0, 2.0, 3.0, 4.0, 5.0),
        arrayOf(1.0, 2.0, 3.0, 4.0, 5.0),
        arrayOf(1.0, 2.0, 3.0, 4.0, 5.0)))

private val symbolTable = hashMapOf(
        Pair("I", { img: GrayScaleImage -> img }),  // input image
        Pair("F", { img: GrayScaleImage -> img }),  // empty filter (default)
        Pair("Box.F", { img: GrayScaleImage -> BoxFilter(3, ::DuplicatePadding).filter(img) })) // box filter