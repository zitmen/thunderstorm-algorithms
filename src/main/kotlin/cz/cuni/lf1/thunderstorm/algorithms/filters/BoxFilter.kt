package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.algorithms.Filter
import cz.cuni.lf1.thunderstorm.algorithms.padding.DuplicatePadding
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.extensions.create2DDoubleArray

public class BoxFilter(private val size: Int) : Filter {

    private val convolutionFilter
            = ConvolutionFilter.createFromSeparableKernel(
                GrayScaleImage(create2DDoubleArray(size, 1, 1.0/size.toDouble())),
                ::DuplicatePadding)

    public override fun filter(image: GrayScaleImage): GrayScaleImage
            = convolutionFilter.filter(image)
}