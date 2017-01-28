package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.algorithms.padding.Padding
import cz.cuni.lf1.thunderstorm.datastructures.BSpline
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImageImpl
import cz.cuni.lf1.thunderstorm.datastructures.extensions.pow

internal class WaveletFilter(private val plane: Int, private val splineOrder: Int, private val splineScale: Double,
                             private val nSamples: Int, paddingTypeFactory: (Int) -> Padding) : Filter {

    public val kernelSize = (2.pow(plane - 1) * (nSamples - 1)) + 1

    private val filter = ConvolutionFilter.createFromSeparableKernel(
            GrayScaleImageImpl(arrayOf(createKernel())), paddingTypeFactory)

    override fun filter(image: GrayScaleImage)
            = filter.filter(image)

    private fun createKernel(): Array<Double> {
        val spline = BSpline.create(
                splineOrder, splineScale,
                ((-nSamples/2)..(-nSamples/2+nSamples-1))
                        .map(Int::toDouble)
                        .toTypedArray())

        if(plane == 1) {
            return spline
        } else {
            val step = 2.pow(plane - 1)
            val kernel = Array(kernelSize) { 0.0 }
            for(i in 0..(spline.size - 1)) {
                kernel[i*step] = spline[i]
            }
            return kernel
        }
    }
}