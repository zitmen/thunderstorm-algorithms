package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.algorithms.padding.Padding
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.extensions.createGrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.extensions.gauss
import cz.cuni.lf1.thunderstorm.datastructures.extensions.normalize

public class GaussianFilter(size: Int, sigma: Double, paddingTypeFactory: (Int) -> Padding) : Filter {

    private val convolutionFilter
        = ConvolutionFilter.createFromSeparableKernel(
            createGrayScaleImage(arrayOf(Array(size) { i -> gauss((i - size/2).toDouble(), sigma) }.normalize())),
            paddingTypeFactory)

    override fun filter(image: GrayScaleImage)
        = convolutionFilter.filter(image)
}
