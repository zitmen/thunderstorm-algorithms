package cz.cuni.lf1.thunderstorm.algorithms.detectors

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.Point2D
import cz.cuni.lf1.thunderstorm.datastructures.extensions.applyMask
import cz.cuni.lf1.thunderstorm.datastructures.extensions.binaryThreshold
import cz.cuni.lf1.thunderstorm.datastructures.extensions.watershed.WatershedAlgorithm

public class CentroidOfConnectedComponentsDetector(private val useWatershed: Boolean) : Detector {

    /**
     * Detection algorithm works simply by setting all values lower than a
     * threshold to zero, splitting close peaks by watershed and finding
     * centroids of connected components.
     *
     * In more detail this is how it is done:
     * 1) apply the threshold to get thresholded binary image</li>
     * 2) in the original image, set intensity to zero, where the thresholded
     *    image is zero. Leave the grayscale value otherwise.
     * 3) perform a watershed transform (this is the trick for recognition of more
     *    connected molecules
     * 4) AND the thresholded image with watershed image</li>
     * 5) then we think of the resulting image as an undirected graph with
     *    8-connectivity and find all connected components with the same id
     * 6) finally, positions of molecules are calculated as centroids of components
     *    with the same id
     */
    public override fun detect(image: GrayScaleImage, threshold: Double): List<Point2D> {

        //keep a local threshold value so the method remains thread safe
        val thresholdedImage = image.binaryThreshold(threshold, 0.0, 255.0)
        var maskedImage = image.applyMask(thresholdedImage)

        if(useWatershed) {
            val watershedImage = WatershedAlgorithm.findMaxima(maskedImage)
            val thresholdImageANDWatershedImage = thresholdedImage.applyMask(watershedImage)
            maskedImage = thresholdImageANDWatershedImage
        }
        // finding a center of gravity (with subpixel precision)
        return maskedImage.getConnectedComponents(CONNECTIVITY_8).map { it.centroid() }
    }
}