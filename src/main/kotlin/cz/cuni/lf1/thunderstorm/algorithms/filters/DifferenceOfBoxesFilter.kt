package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.algorithms.padding.Padding
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.extensions.minus

public class DifferenceOfBoxesFilters(size1: Int, size2: Int, paddingTypeFactory: (Int) -> Padding) : Filter {

    private val boxFilter1 = BoxFilter(size1, paddingTypeFactory)
    private val boxFilter2 = BoxFilter(size2, paddingTypeFactory)

    override fun filter(image: GrayScaleImage)
        = boxFilter1.filter(image) - boxFilter2.filter(image)
}
