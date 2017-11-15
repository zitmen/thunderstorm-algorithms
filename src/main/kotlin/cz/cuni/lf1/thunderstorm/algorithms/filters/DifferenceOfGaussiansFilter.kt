package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.algorithms.padding.Padding
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.extensions.minus

public class DifferenceOfGaussiansFilter(size: Int, sigma1: Double, sigma2: Double, paddingTypeFactory: (Int) -> Padding)
    : FilterWithVariables {

    private val gaussianFilter1 = GaussianFilter(size, sigma1, paddingTypeFactory)
    private val gaussianFilter2 = GaussianFilter(size, sigma2, paddingTypeFactory)

    public override val variables = mapOf(
            "I" to emptyFilterVariable(),
            "F" to emptyFilterVariable(),
            "G1" to emptyFilterVariable(),
            "G2" to emptyFilterVariable())

    override fun filter(image: GrayScaleImage): GrayScaleImage {
        val g1 = gaussianFilter1.filter(image)
        val g2 = gaussianFilter2.filter(image)
        val dog = g1 - g2

        variables["I"]!!.setRefresher { image }
        variables["F"]!!.setRefresher { dog }
        variables["G1"]!!.setRefresher { g1 }
        variables["G2"]!!.setRefresher { g2 }

        return dog
    }
}
