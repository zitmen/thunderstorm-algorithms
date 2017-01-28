package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.algorithms.padding.Padding
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.extensions.minus

public class LoweredGaussianFilter(size: Int, sigma: Double, paddingTypeFactory: (Int) -> Padding) : Filter {

    private val gaussianFilter = GaussianFilter(size, sigma, paddingTypeFactory)
    private val boxFilter = BoxFilter(size, paddingTypeFactory)

    override fun filter(image: GrayScaleImage)
        = gaussianFilter.filter(image) - boxFilter.filter(image)
}