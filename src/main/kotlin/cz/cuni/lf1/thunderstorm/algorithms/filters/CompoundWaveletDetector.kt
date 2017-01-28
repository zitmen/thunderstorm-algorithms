package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.algorithms.padding.DuplicatePadding
import cz.cuni.lf1.thunderstorm.algorithms.padding.Padding
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.RoiImpl
import cz.cuni.lf1.thunderstorm.datastructures.extensions.crop
import cz.cuni.lf1.thunderstorm.datastructures.extensions.minus

public class CompoundWaveletFilter(splineOrder: Int, splineScale: Double, splineSamples: Int, paddingTypeFactory: (Int) -> Padding) : Filter {

    private val w1 = WaveletFilter(1, splineOrder, splineScale, splineSamples, paddingTypeFactory)
    private val w2 = WaveletFilter(2, splineOrder, splineScale, splineSamples, paddingTypeFactory)
    private val margin = w2.kernelSize / 2

    override fun filter(image: GrayScaleImage): GrayScaleImage {
        val v1 = w1.filter(DuplicatePadding(margin).pad(image))
        val v2 = w2.filter(v1)

        return (v1 - v2).crop(RoiImpl(margin, margin, image.getWidth(), image.getHeight()))
    }
}