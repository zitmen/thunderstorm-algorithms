package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.parser.thresholding.RefreshableVariable

public interface Filter {

    fun filter(image: GrayScaleImage): GrayScaleImage
}

public interface FilterWithVariables : Filter {

    val variables: Map<String, RefreshableVariable<GrayScaleImage?>>
}

public fun FilterWithVariables.asRefreshableVariable()
        = RefreshableVariable<FilterWithVariables>(this)

public fun emptyFilterVariable()
        = RefreshableVariable<GrayScaleImage?>(null)