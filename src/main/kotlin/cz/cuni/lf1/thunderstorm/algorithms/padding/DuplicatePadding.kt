package cz.cuni.lf1.thunderstorm.algorithms.padding

import cz.cuni.lf1.thunderstorm.algorithms.padding.Padding
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.extensions.copyDataToPosition
import cz.cuni.lf1.thunderstorm.datastructures.extensions.create2DDoubleArray
import cz.cuni.lf1.thunderstorm.datastructures.extensions.createGrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.extensions.fillRectangle

public class DuplicatePadding(private val size: Int) : Padding {

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
        val newWidth = width + 2*size
        val newHeight = height + 2*size

        val pixels = create2DDoubleArray(newHeight, newWidth, 0.0)

        pixels.copyDataToPosition(image, size, size)  // put the original image into the center

        pixels.fillRectangle(0, 0, size - 1, size - 1, image.getValue(0, 0))    // top left corner of border
        pixels.fillRectangle(0, size + width, size - 1, 2*size + width - 1, image.getValue(0, width - 1)) // top right corner of border
        pixels.fillRectangle(size + height, 0, 2*size + height - 1, size - 1, image.getValue(height - 1, 0)) // bottom left corner of border
        pixels.fillRectangle(size + height, size + width, 2*size + height - 1, 2*size + width - 1, image.getValue(height - 1, width - 1))  // bottom right corner of border

        // horizontal borders
        for(y in 0..(size - 1)) {
            for(x in 0..(width - 1)) {
                pixels[y][x + size] = image.getValue(0, x)
                pixels[newHeight - 1 - y][x + size] = image.getValue(height - 1, x)
            }
        }

        // vertical borders
        for(x in 0..(size - 1)) {
            for(y in 0..(height - 1)) {
                pixels[y + size][x] = image.getValue(y, 0)
                pixels[y + size][newWidth - 1 - x] = image.getValue(y, width - 1)
            }
        }

        return createGrayScaleImage(pixels)
    }
}