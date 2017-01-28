package cz.cuni.lf1.thunderstorm.algorithms.padding

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.extensions.create2DDoubleArray
import cz.cuni.lf1.thunderstorm.datastructures.extensions.createGrayScaleImage

public class ZeroPadding(private val size: Int) : Padding {

    init {
        if (size < 0) {
            throw IllegalArgumentException("Size must be >= 0!")
        }
    }

    public override fun getPadSize(): Int
            = size

    public override fun pad(image: GrayScaleImage): GrayScaleImage {
        val pixels = create2DDoubleArray(image.getWidth() + 2*size, image.getHeight() + 2*size, 0.0)
        for (y in 0..(image.getHeight() - 1)) {
            for (x in 0..(image.getWidth() - 1)) {
                pixels[y + size][x + size] = image.getValue(y, x)
            }
        }
        return createGrayScaleImage(pixels)
    }
}