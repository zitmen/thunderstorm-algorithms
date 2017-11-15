package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.algorithms.padding.Padding
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.extensions.minus

public class LoweredGaussianFilter(size: Int, sigma: Double, paddingTypeFactory: (Int) -> Padding) : FilterWithVariables {

    private val gaussianFilter = GaussianFilter(size, sigma, paddingTypeFactory)
    private val boxFilter = BoxFilter(size, paddingTypeFactory)

    public override val variables = mapOf(
            "I" to emptyFilterVariable(),
            "F" to emptyFilterVariable())

    override fun filter(image: GrayScaleImage): GrayScaleImage {
        val f = gaussianFilter.filter(image) - boxFilter.filter(image)

        variables["I"]!!.setRefresher { image }
        variables["F"]!!.setRefresher { f }

        return f
    }
}