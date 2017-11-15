package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.algorithms.padding.Padding
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.extensions.minus

public class DifferenceOfBoxesFilters(size1: Int, size2: Int, paddingTypeFactory: (Int) -> Padding)
    : FilterWithVariables {

    private val boxFilter1 = BoxFilter(size1, paddingTypeFactory)
    private val boxFilter2 = BoxFilter(size2, paddingTypeFactory)

    public override val variables = mapOf(
            "I" to emptyFilterVariable(),
            "F" to emptyFilterVariable(),
            "B1" to emptyFilterVariable(),
            "B2" to emptyFilterVariable())

    override fun filter(image: GrayScaleImage): GrayScaleImage {
        val b1 = boxFilter1.filter(image)
        val b2 = boxFilter2.filter(image)
        val f = b1 - b2

        variables["I"]!!.setRefresher { image }
        variables["F"]!!.setRefresher { f }
        variables["B1"]!!.setRefresher { b1 }
        variables["B2"]!!.setRefresher { b2 }

        return f
    }
}
