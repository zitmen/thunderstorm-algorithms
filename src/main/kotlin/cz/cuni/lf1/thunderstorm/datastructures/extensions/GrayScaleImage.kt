package cz.cuni.lf1.thunderstorm.datastructures.extensions

import cz.cuni.lf1.thunderstorm.algorithms.padding.Padding
import cz.cuni.lf1.thunderstorm.algorithms.padding.ZeroPadding
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImageImpl

internal fun createGrayScaleImage(data: Array<Array<Double>>)
        = GrayScaleImageImpl(data)

/**
 * Rotate the entire image 90 degrees counter-clockwise
 */
internal fun GrayScaleImage.rotateLeft(): GrayScaleImage {
    val pixels = create2DDoubleArray(this.getWidth(), this.getHeight(), 0.0)
    for (y in 0..(pixels.size - 1)) {
        for (x in 0..(pixels[y].size - 1)) {
            pixels[y][x] = this.getValue(x, pixels.size - 1 - y)
        }
    }
    return createGrayScaleImage(pixels)
}

/**
 * Rotate the entire image 90 degrees clockwise
 */
internal fun GrayScaleImage.rotateRight(): GrayScaleImage {
    val pixels = create2DDoubleArray(this.getWidth(), this.getHeight(), 0.0)
    for (y in 0..(pixels.size - 1)) {
        for (x in 0..(pixels[y].size - 1)) {
            pixels[y][x] = this.getValue(pixels[y].size - 1 - x, y)
        }
    }
    return createGrayScaleImage(pixels)
}

/**
 * Two dimensional convolution
 */
internal fun GrayScaleImage.convolve2D(kernel: GrayScaleImage, paddingTypeFactory: (padSize: Int) -> Padding): GrayScaleImage {
    val kernelWidth = kernel.getWidth()
    val kernelHeight = kernel.getHeight()

    val padding = paddingTypeFactory((max(kernelWidth, kernelHeight)/2.0).ceil().toInt())
    val padSize = padding.getPadSize()
    val paddedImage = padding.pad(this)

    val pixels = create2DDoubleArray(this.getHeight(), this.getWidth(), 0.0)
    for ((imageRow, y) in (padSize..(paddedImage.getHeight() - padSize - 1)).withIndex()) {
        for ((imageCol, x) in (padSize..(paddedImage.getWidth() - padSize - 1)).withIndex()) {
            for ((kernelRow, v) in (-(kernelHeight/2)..(kernelHeight - kernelHeight/2 - 1)).withIndex()) {
                for ((kernelCol, u) in (-(kernelWidth/2)..(kernelWidth - kernelWidth/2 - 1)).withIndex()) {
                    pixels[imageRow][imageCol] += paddedImage.getValue(y - v, x - u)*kernel.getValue(kernelRow, kernelCol)
                }
            }
        }
    }

    return createGrayScaleImage(pixels)
}

/**
 * Gray-scale dilation
 */
internal fun GrayScaleImage.dilate(kernel: GrayScaleImage): GrayScaleImage {
    val xCenter = kernel.getWidth()/2
    val yCenter = kernel.getHeight()/2
    val paddedImage = ZeroPadding(max(xCenter, yCenter)).pad(this)
    val pixels = create2DDoubleArray(this.getHeight(), this.getWidth(), 0.0)
    for (y in yCenter..(yCenter + this.getHeight() - 1)) {
        for (x in xCenter..(xCenter + this.getWidth() - 1)) {
            for (j in 0..(kernel.getHeight() - 1)) {
                for (i in 0..(kernel.getWidth() - 1)) {
                    pixels[y - yCenter][x - xCenter] = max(pixels[y - yCenter][x - xCenter], kernel.getValue(j, i) * paddedImage.getValue(y + j - yCenter, x + i - xCenter))
                }
            }
        }
    }
    return createGrayScaleImage(pixels)
}