package cz.cuni.lf1.thunderstorm.algorithms.padding

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.extensions.create2DDoubleArray
import cz.cuni.lf1.thunderstorm.datastructures.extensions.createGrayScaleImage

public class CyclicPadding(private val size: Int) : Padding {

    init {
        if (size < 0) {
            throw IllegalArgumentException("Size must be >= 0!")
        }
    }

    public override fun getPadSize(): Int
            = size

    public override fun pad(image: GrayScaleImage): GrayScaleImage {
        val width = image.getWidth()
        val height = image.getHeight()

        val pixels = create2DDoubleArray(height + 2*size, width + 2*size, 0.0)
        val verticalEdge = width - size%width
        val horizontalEdge = height - size%height
        for(y in 0..(pixels.size - 1)) {
            for(x in 0..(pixels[y].size - 1)) {
                pixels[y][x] = image.getValue((y + horizontalEdge)%height, (x + verticalEdge)%width)
            }
        }

        return createGrayScaleImage(pixels)
    }
}