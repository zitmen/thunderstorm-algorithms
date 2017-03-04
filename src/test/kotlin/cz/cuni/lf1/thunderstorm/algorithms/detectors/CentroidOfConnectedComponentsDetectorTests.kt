package cz.cuni.lf1.thunderstorm.algorithms.detectors

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImageImpl
import cz.cuni.lf1.thunderstorm.datastructures.Point2DImpl
import org.junit.Assert.assertEquals
import org.junit.Test

public class CentroidOfConnectedComponentsDetectorTests {

    @Test
    public fun testDetectMoleculeCandidates1() {
        val image = GrayScaleImageImpl(arrayOf(// transposed
                arrayOf(9.0, 4.0, 3.0, 7.0, 4.0),
                arrayOf(4.0, 6.0, 7.0, 2.0, 4.0),
                arrayOf(1.0, 1.0, 1.0, 1.0, 1.0),
                arrayOf(2.0, 3.0, 5.0, 6.0, 8.0),
                arrayOf(2.0, 3.0, 3.0, 3.0, 2.0)))

        val expResult = listOf(Point2DImpl(0.5, 1.5), Point2DImpl(3.0, 3.0))
        val result = CentroidOfConnectedComponentsDetector(false).detect(image, 5.0)

        assertEquals(expResult, result)
    }

    @Test
    fun testDetectMoleculeCandidates2() {
        val image = GrayScaleImageImpl(arrayOf(// transposed
                arrayOf(3.0, 5.0, 3.0, 1.0, 3.0, 5.0, 3.0),
                arrayOf(5.0, 8.0, 5.0, 3.0, 5.0, 8.0, 5.0),
                arrayOf(3.0, 5.0, 3.0, 1.0, 3.0, 5.0, 3.0)))

        val expResult = listOf(Point2DImpl(1.0, 1.0), Point2DImpl(1.0, 5.0))
        val result = CentroidOfConnectedComponentsDetector(true).detect(image, 3.0)

        assertEquals(expResult, result)
    }

    @Test
    fun testDetectMoleculeCandidates3() {
        val image = GrayScaleImageImpl(arrayOf(// transposed
                arrayOf(5.0, 1.0),
                arrayOf(1.0, 5.0)))

        val expResult = listOf(Point2DImpl(0.5, 0.5))
        val result = CentroidOfConnectedComponentsDetector(true).detect(image, 3.0)

        assertEquals(expResult, result)
    }

/* Let's skip this test, because I dont know where are the real results from Matlab...in the CSV is ground-truth.
 * -- five points were not found...and it is ok, because if you look at them in the image, it is impossible to see them :-)
 *
 * [69.25779947916666,181.7811328125]
 * [68.47399739583334,98.31500000000001]
 * [69.75059895833334,102.15853515625001]
 * [151.1509375,46.70306640625]
 * [230.85726562500003,62.75619791666667]
 * [244.14781250000001,106.15860026041668]
 *
 *
    @Test
    fun testDetectMoleculeCandidates4() {
        try {
            image = (FloatProcessor) IJ.openImage("test/resources/tubulins1_00020.tif").getProcessor().convertToFloat();
            FloatProcessor filtered = (new CompoundWaveletFilter(false)).filterImage(image);
            result = (new CentroidOfConnectedComponentsDetector(false, 14.2835)).detectMoleculeCandidates(filtered);
            expResult = CSV.csv2point("test/resources/tubulins1_00020.csv", 1, 2);
            for(Point pt : expResult) { // lets work with the pixels for a moment
                pt.scaleXY(1.0/150.0);  // pixelsize = 150nm
            }
            Collections.sort(result, new Point.XYComparator());
            Collections.sort(expResult, new Point.XYComparator());
            //
            System.out.println("\n============= RESULT ===============");
            for(Point pt : result) System.out.println(pt);
            System.out.println("\n\n======= EXPECTED RESULT ==========");
            for(Point pt : expResult) System.out.println(pt);
            //
            assertEquals(expResult, result); // TODO: approximate!!!
        } catch(IOException ex) {
            fail("Error in CentroidOfConnectedComponentsDetector test with real data: " + ex.getMessage());
        }
    }
*/
/*
    @Test
    fun testDetectMoleculeCandidates2() {
        // seven molecules close together that needs watershed segmentation to resolve them
        val basePath = this.javaClass.protectionDomain.codeSource.location.path
        println(basePath)
        val fp = IJ.openImage(basePath + "7peaksWaveletFiltered.tif").processor.convertToFloat() as FloatProcessor
        val detector = CentroidOfConnectedComponentsDetector(true)
        val detections = detector.detect(fp, 2.0)
        assertEquals(7, detections.size.toLong())
        for (p in detections) {
            val x = p.getX()
            val y = p.getY()
            MathUtils.checkFinite(x)
            MathUtils.checkFinite(y)
            assertTrue("in range", x >= 0 && x <= fp.width)
            assertTrue("in range", y >= 0 && y <= fp.width)
        }
    }
*/
}