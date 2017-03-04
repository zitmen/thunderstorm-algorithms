package cz.cuni.lf1.thunderstorm.datastructures.extensions.watershed

import cz.cuni.lf1.thunderstorm.algorithms.detectors.ImageJ_MaximumFinder
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImageImpl
import cz.cuni.lf1.thunderstorm.datastructures.extensions.create2DDoubleArray
import ij.plugin.filter.MaximumFinder
import ij.process.ByteProcessor
import ij.process.FloatProcessor
import ij.process.ImageProcessor

/** This ImageJ plug-in filter finds the maxima (or minima) of an image.
 * It can create a mask where the local maxima of the current image are
 * marked (255; unmarked pixels 0).
 * The plug-in can also create watershed-segmented particles: Assume a
 * landscape of inverted heights, i.e., maxima of the image are now water sinks.
 * For each point in the image, the sink that the water goes to determines which
 * particle it belongs to.
 * When finding maxima (not minima), pixels with a level below the lower threshold
 * can be left unprocessed.

 * Except for segmentation, this plugin works with ROIs, including non-rectangular ROIs.
 * Since this plug-in creates a separate output image it processes
 * only single images or slices, no stacks.
 */

object WatershedAlgorithm {

    fun findMaxima(img: GrayScaleImage): GrayScaleImage {
        var typeP = GrayScaleImageImpl(create2DDoubleArray(img.getHeight(), img.getWidth(), 0.0))     //will be a notepad for pixel types
        var globalMin = Double.MAX_VALUE
        var globalMax = Double.MIN_VALUE
        for (y in 0..img.getHeight() - 1) {         //find local minimum/maximum now
            for (x in 0..img.getWidth() - 1) {      //ImageStatistics won't work if we have no ImagePlus
                val v = img.getValue(x, y)
                if (globalMin > v) globalMin = v
                if (globalMax < v) globalMax = v
            }
        }

        val maxPoints = WatershedFunctions.getSortedMaxPoints(img, WatershedFunctions.findLocalMaxima(img, typeP, globalMin), globalMin, globalMax)
        val maxSortingError = 1.1f * (globalMax - globalMin) / 2e9f //sorted sequence may be inaccurate by this value

        /* maximum height difference between points that are not counted as separate maxima */
        val tolerance = 10.0
        typeP = WatershedFunctions.analyzeAndMarkMaxima(img.toFloatProcessor(), typeP, maxPoints, tolerance, maxSortingError) as GrayScaleImageImpl

        // Segmentation required, convert to 8bit (also for 8-bit images, since the calibration
        // may have a negative slope). outIp has background 0, maximum areas 255
        var outIp = WatershedFunctions.make8bit(img, typeP, globalMin, globalMax).toByteProcessor()
        outIp = WatershedFunctions.cleanupMaxima(outIp, typeP.toByteProcessor(), maxPoints, img.getWidth(), img.getHeight())     //eliminate all the small maxima (i.e. those outside MAX_AREA)
        outIp = WatershedFunctions.watershedSegment(outIp)
        outIp = outIp.toGrayScaleImage().cleanupExtraLines().toByteProcessor()       //eliminate lines due to local minima (none in EDM)
        outIp = WatershedFunctions.watershedPostProcess(outIp)                //levels to binary image

        return outIp.toGrayScaleImage()
    }
}

public fun GrayScaleImage.toFloatProcessor(): FloatProcessor {
    val fp = FloatProcessor(getWidth(), getHeight())
    for (y in 0..(getHeight()-1)) {
        for (x in 0..(getWidth()-1)) {
            fp.putPixelValue(x, y, getValue(y, x))
        }
    }
    return fp
}

public fun GrayScaleImage.toByteProcessor(): ByteProcessor {
    val fp = ByteProcessor(getWidth(), getHeight())
    for (y in 0..(getHeight()-1)) {
        for (x in 0..(getWidth()-1)) {
            fp.putPixelValue(x, y, getValue(y, x))
        }
    }
    return fp
}

public fun ImageProcessor.toGrayScaleImage(): GrayScaleImage {
    val arr = create2DDoubleArray(height, width, 0.0)
    for (y in 0..(height-1)) {
        for (x in 0..(width-1)) {
            arr[y][x] = getPixelValue(x, y).toDouble()
        }
    }
    return GrayScaleImageImpl(arr)
}