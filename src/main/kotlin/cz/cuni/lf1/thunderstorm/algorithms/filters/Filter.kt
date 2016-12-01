package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage

public interface Filter {

    fun filter(image: GrayScaleImage): GrayScaleImage
}
