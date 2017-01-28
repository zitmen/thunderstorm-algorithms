package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.algorithms.padding.Padding
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.extensions.minus

public class DifferenceOfGaussiansFilter(size: Int, sigma1: Double, sigma2: Double, paddingTypeFactory: (Int) -> Padding) : Filter {

    private val gaussianFilter1 = GaussianFilter(size, sigma1, paddingTypeFactory)
    private val gaussianFilter2 = GaussianFilter(size, sigma2, paddingTypeFactory)

    override fun filter(image: GrayScaleImage)
        = gaussianFilter1.filter(image) - gaussianFilter2.filter(image)
}
