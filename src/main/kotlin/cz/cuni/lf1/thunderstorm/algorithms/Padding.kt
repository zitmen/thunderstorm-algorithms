package cz.cuni.lf1.thunderstorm.algorithms

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage

public interface Padding {

    fun getPadSize(): Int
    fun pad(image: GrayScaleImage): GrayScaleImage
}