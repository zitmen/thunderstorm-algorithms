package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.algorithms.padding.Padding
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.extensions.create2DDoubleArray
import cz.cuni.lf1.thunderstorm.datastructures.extensions.createGrayScaleImage

public class BoxFilter(size: Int, paddingTypeFactory: (Int) -> Padding) : FilterWithVariables {

    public override val variables = mapOf(
            "I" to emptyFilterVariable(),
            "F" to emptyFilterVariable())

    private val convolutionFilter
            = ConvolutionFilter.createFromSeparableKernel(
                createGrayScaleImage(create2DDoubleArray(size, 1, 1.0/size.toDouble())),
                paddingTypeFactory)

    public override fun filter(image: GrayScaleImage): GrayScaleImage {
        val f = convolutionFilter.filter(image)

        variables["I"]!!.setRefresher { image }
        variables["F"]!!.setRefresher { f }

        return f
    }
}