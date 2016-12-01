package cz.cuni.lf1.thunderstorm.algorithms

import cz.cuni.lf1.thunderstorm.datastructures.Image

public interface IFilter {

    fun filter(image: Image): Image
}
