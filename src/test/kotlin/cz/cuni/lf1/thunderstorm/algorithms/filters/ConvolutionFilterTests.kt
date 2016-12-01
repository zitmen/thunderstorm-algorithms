package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.algorithms.padding.ZeroPadding
import cz.cuni.lf1.thunderstorm.datastructures.extensions.createGrayScaleImage
import cz.cuni.lf1.thunderstorm.test.assertGrayScaleImageEquals
import org.junit.Test

internal class ConvolutionFilterTests {

    @Test
    public fun testFilterWithFullKernel() {
        val image = createGrayScaleImage(arrayOf(
                arrayOf(17.0, 24.0,  1.0,  8.0, 15.0),
                arrayOf(23.0,  5.0,  7.0, 14.0, 16.0),
                arrayOf( 4.0,  6.0, 13.0, 20.0, 22.0),
                arrayOf(10.0, 12.0, 19.0, 21.0,  3.0),
                arrayOf(11.0, 18.0, 25.0,  2.0,  9.0)))

        val kernel = createGrayScaleImage(arrayOf(
                arrayOf(0.9649, 0.9572, 0.1419),
                arrayOf(0.1576, 0.4854, 0.4218),
                arrayOf(0.9706, 0.8003, 0.9157)))

        val result = ConvolutionFilter
                .createFromFullKernel(kernel, ::ZeroPadding)
                .filter(image)

        val expected = createGrayScaleImage(arrayOf(
                arrayOf(38.8743, 33.7818, 32.7879, 36.5015, 27.9572),
                arrayOf(58.4699, 67.8308, 70.8481, 76.3634, 56.8981),
                arrayOf(47.2979, 69.7437, 75.9145, 77.4943, 50.5909),
                arrayOf(43.6674, 77.0326, 81.3179, 82.0897, 55.1332),
                arrayOf(27.8264, 54.5190, 66.6193, 50.0506, 26.8428)))

        assertGrayScaleImageEquals(expected, result, 1e-12)
    }

    @Test
    public fun testFilterWithTwoVectorSeparableKernel() {
        val image = createGrayScaleImage(arrayOf(
                arrayOf(17.0, 24.0,  1.0,  8.0, 15.0),
                arrayOf(23.0,  5.0,  7.0, 14.0, 16.0),
                arrayOf( 4.0,  6.0, 13.0, 20.0, 22.0),
                arrayOf(10.0, 12.0, 19.0, 21.0,  3.0),
                arrayOf(11.0, 18.0, 25.0,  2.0,  9.0)))

        val kernelRow = createGrayScaleImage(arrayOf(arrayOf(0.9649, 0.9572, 0.1419)))
        val kernelCol = createGrayScaleImage(arrayOf(arrayOf(0.1576), arrayOf(0.4854), arrayOf(0.4218)))

        val result = ConvolutionFilter
                .createFromSeparableKernel(kernelRow, kernelCol, ::ZeroPadding)
                .filter(image)

        val expected = createGrayScaleImage(arrayOf(
                arrayOf(23.3693, 15.1234,  9.1614, 15.5129, 10.2472),
                arrayOf(31.1756, 21.2718, 20.3867, 30.5288, 18.6994),
                arrayOf(19.3232, 20.3195, 30.9721, 37.1243, 19.8194),
                arrayOf(18.7202, 29.8791, 37.7158, 32.5072, 14.3222),
                arrayOf(22.4628, 34.0064, 30.7285, 17.7034,  6.7875)))

        assertGrayScaleImageEquals(expected, result, 1e-4)
    }

    @Test
    public fun testFilterWithSingleRowVectorSeparableKernel() {
        val image = createGrayScaleImage(arrayOf(
                arrayOf(17.0, 24.0,  1.0,  8.0, 15.0),
                arrayOf(23.0,  5.0,  7.0, 14.0, 16.0),
                arrayOf( 4.0,  6.0, 13.0, 20.0, 22.0),
                arrayOf(10.0, 12.0, 19.0, 21.0,  3.0),
                arrayOf(11.0, 18.0, 25.0,  2.0,  9.0)))

        val kernel = createGrayScaleImage(arrayOf(arrayOf(0.1576, 0.4854, 0.4218)))

        val result = ConvolutionFilter
                .createFromFullKernel(kernel, ::ZeroPadding)
                .filter(image)

        val expected = createGrayScaleImage(arrayOf(
                arrayOf(12.0342, 18.9778, 11.8694,  6.6690, 10.6554),
                arrayOf(11.9522, 13.2316,  7.7132, 12.2698, 13.6716),
                arrayOf( 2.8872,  6.6484, 11.9930, 18.6586, 19.1148),
                arrayOf( 6.7452, 13.0372, 17.5938, 18.6804, 10.3140),
                arrayOf( 8.1762, 17.3170, 20.0426, 12.9342,  5.2122)))

        assertGrayScaleImageEquals(expected, result, 1e-12)
    }

    @Test
    public fun testFilterWithSingleColumnVectorSeparableKernel() {
        val image = createGrayScaleImage(arrayOf(
                arrayOf(17.0, 24.0,  1.0,  8.0, 15.0),
                arrayOf(23.0,  5.0,  7.0, 14.0, 16.0),
                arrayOf( 4.0,  6.0, 13.0, 20.0, 22.0),
                arrayOf(10.0, 12.0, 19.0, 21.0,  3.0),
                arrayOf(11.0, 18.0, 25.0,  2.0,  9.0)))

        val kernel = createGrayScaleImage(arrayOf(arrayOf(0.9572), arrayOf(0.4854), arrayOf(0.8003)))

        val result = ConvolutionFilter
                .createFromFullKernel(kernel, ::ZeroPadding)
                .filter(image)

        val expected = createGrayScaleImage(arrayOf(
                arrayOf(30.2674, 16.4356,  7.1858, 17.2840, 22.5962),
                arrayOf(28.5981, 27.3774, 16.6417, 32.3420, 40.8293),
                arrayOf(29.9205, 18.4003, 30.0991, 41.0134, 26.3552),
                arrayOf(18.5844, 27.8562, 43.5565, 28.1138, 27.6776),
                arrayOf(13.3424, 18.3408, 27.3407, 17.7771,  6.7695)))

        assertGrayScaleImageEquals(expected, result, 1e-12)
    }
}