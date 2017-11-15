package cz.cuni.lf1.thunderstorm.datastructures.extensions

import cz.cuni.lf1.thunderstorm.algorithms.padding.Padding
import cz.cuni.lf1.thunderstorm.algorithms.padding.ZeroPadding
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImageImpl
import cz.cuni.lf1.thunderstorm.datastructures.Roi

internal fun createGrayScaleImage(data: Array<Array<Double>>)
        = GrayScaleImageImpl(data)

/**
 * Extract a sub-image, e.g., a fitting region
 */
internal fun GrayScaleImage.extractSubImage(top: Int, left: Int, height: Int, width: Int)
        = createGrayScaleImage(create2DDoubleArray(height, width, { r, c ->
            this.getValue(top + r, left + c)
        }))

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

/**
 * Add two images
 */
internal operator fun GrayScaleImage.plus(img: GrayScaleImage): GrayScaleImage {
    if (getWidth() != img.getWidth() || getHeight() != img.getHeight()) {
        throw IllegalArgumentException("Both images must be of the same size!")
    }
    return GrayScaleImageImpl(create2DDoubleArray(getHeight(), getWidth(), { r, c -> getValue(r, c) + img.getValue(r, c) }))
}

internal operator fun GrayScaleImage.plus(value: Double)
        = GrayScaleImageImpl(create2DDoubleArray(getHeight(), getWidth(), { r, c -> getValue(r, c) + value }))

internal operator fun Double.plus(img: GrayScaleImage)
        = img + this

/**
 * Subtract two images
 */
internal operator fun GrayScaleImage.minus(img: GrayScaleImage): GrayScaleImage {
    if (getWidth() != img.getWidth() || getHeight() != img.getHeight()) {
        throw IllegalArgumentException("Both images must be of the same size!")
    }
    return GrayScaleImageImpl(create2DDoubleArray(getHeight(), getWidth(), { r, c -> getValue(r, c) - img.getValue(r, c) }))
}

internal operator fun GrayScaleImage.minus(value: Double)
        = GrayScaleImageImpl(create2DDoubleArray(getHeight(), getWidth(), { r, c -> getValue(r, c) - value }))

internal operator fun Double.minus(img: GrayScaleImage)
        = GrayScaleImageImpl(create2DDoubleArray(img.getHeight(), img.getWidth(), { r, c -> this - img.getValue(r, c) }))

/**
 * Multiply two images
 */
internal operator fun GrayScaleImage.times(img: GrayScaleImage): GrayScaleImage {
    if (getWidth() != img.getWidth() || getHeight() != img.getHeight()) {
        throw IllegalArgumentException("Both images must be of the same size!")
    }
    return GrayScaleImageImpl(create2DDoubleArray(getHeight(), getWidth(), { r, c -> getValue(r, c) * img.getValue(r, c) }))
}

internal operator fun GrayScaleImage.times(value: Double)
        = GrayScaleImageImpl(create2DDoubleArray(getHeight(), getWidth(), { r, c -> getValue(r, c) * value }))

internal operator fun Double.times(img: GrayScaleImage)
        = img * this

/**
 * Divide two images
 */
internal operator fun GrayScaleImage.div(img: GrayScaleImage): GrayScaleImage {
    if (getWidth() != img.getWidth() || getHeight() != img.getHeight()) {
        throw IllegalArgumentException("Both images must be of the same size!")
    }
    return GrayScaleImageImpl(create2DDoubleArray(getHeight(), getWidth(), { r, c -> getValue(r, c) / img.getValue(r, c) }))
}

internal operator fun GrayScaleImage.div(value: Double)
        = GrayScaleImageImpl(create2DDoubleArray(getHeight(), getWidth(), { r, c -> getValue(r, c) / value }))

internal operator fun Double.div(img: GrayScaleImage)
        = GrayScaleImageImpl(create2DDoubleArray(img.getHeight(), img.getWidth(), { r, c -> this / img.getValue(r, c) }))

/**
 * Modulo two images
 */
internal operator fun GrayScaleImage.rem(img: GrayScaleImage): GrayScaleImage {
    if (getWidth() != img.getWidth() || getHeight() != img.getHeight()) {
        throw IllegalArgumentException("Both images must be of the same size!")
    }
    return GrayScaleImageImpl(create2DDoubleArray(getHeight(), getWidth(), { r, c -> getValue(r, c) % img.getValue(r, c) }))
}

internal operator fun GrayScaleImage.rem(value: Double)
        = GrayScaleImageImpl(create2DDoubleArray(getHeight(), getWidth(), { r, c -> getValue(r, c) % value }))

internal operator fun Double.rem(img: GrayScaleImage)
        = GrayScaleImageImpl(create2DDoubleArray(img.getHeight(), img.getWidth(), { r, c -> this % img.getValue(r, c) }))

/**
 * Power an image
 */
internal fun GrayScaleImage.pow(to: Double)
        = GrayScaleImageImpl(create2DDoubleArray(getHeight(), getWidth(), { r, c -> getValue(r, c).pow(to) }))

/**
 * Logical operators on images
 */
internal fun GrayScaleImage.and(arg: GrayScaleImage): GrayScaleImage {
    if (getWidth() != arg.getWidth() || getHeight() != arg.getHeight()) {
        throw IllegalArgumentException("Both images must be of the same size!")
    }
    return GrayScaleImageImpl(create2DDoubleArray(getHeight(), getWidth(), { r, c -> getValue(r, c).and(arg.getValue(r, c)) }))
}

internal fun GrayScaleImage.or(arg: GrayScaleImage): GrayScaleImage {
    if (getWidth() != arg.getWidth() || getHeight() != arg.getHeight()) {
        throw IllegalArgumentException("Both images must be of the same size!")
    }
    return GrayScaleImageImpl(create2DDoubleArray(getHeight(), getWidth(), { r, c -> getValue(r, c).or(arg.getValue(r, c)) }))
}

internal fun GrayScaleImage.eq(arg: GrayScaleImage): GrayScaleImage {
    if (getWidth() != arg.getWidth() || getHeight() != arg.getHeight()) {
        throw IllegalArgumentException("Both images must be of the same size!")
    }
    return GrayScaleImageImpl(create2DDoubleArray(getHeight(), getWidth(), { r, c -> getValue(r, c).eq(arg.getValue(r, c)) }))
}

internal fun GrayScaleImage.eq(arg: Double)
        = GrayScaleImageImpl(create2DDoubleArray(getHeight(), getWidth(), { r, c -> getValue(r, c).eq(arg) }))

internal fun Double.eq(arg: GrayScaleImage)
        = arg.eq(this)

internal fun GrayScaleImage.neq(arg: GrayScaleImage): GrayScaleImage {
    if (getWidth() != arg.getWidth() || getHeight() != arg.getHeight()) {
        throw IllegalArgumentException("Both images must be of the same size!")
    }
    return GrayScaleImageImpl(create2DDoubleArray(getHeight(), getWidth(), { r, c -> getValue(r, c).neq(arg.getValue(r, c)) }))
}

internal fun GrayScaleImage.neq(arg: Double)
        = GrayScaleImageImpl(create2DDoubleArray(getHeight(), getWidth(), { r, c -> getValue(r, c).neq(arg) }))

internal fun Double.neq(arg: GrayScaleImage)
        = arg.neq(this)

internal fun GrayScaleImage.lt(arg: GrayScaleImage): GrayScaleImage {
    if (getWidth() != arg.getWidth() || getHeight() != arg.getHeight()) {
        throw IllegalArgumentException("Both images must be of the same size!")
    }
    return GrayScaleImageImpl(create2DDoubleArray(getHeight(), getWidth(), { r, c -> getValue(r, c).lt(arg.getValue(r, c)) }))
}

internal fun GrayScaleImage.lt(arg: Double)
        = GrayScaleImageImpl(create2DDoubleArray(getHeight(), getWidth(), { r, c -> getValue(r, c).lt(arg) }))

internal fun Double.lt(arg: GrayScaleImage)
        = GrayScaleImageImpl(create2DDoubleArray(arg.getHeight(), arg.getWidth(), { r, c -> this.lt(arg.getValue(r, c)) }))

internal fun GrayScaleImage.gt(arg: GrayScaleImage): GrayScaleImage {
    if (getWidth() != arg.getWidth() || getHeight() != arg.getHeight()) {
        throw IllegalArgumentException("Both images must be of the same size!")
    }
    return GrayScaleImageImpl(create2DDoubleArray(getHeight(), getWidth(), { r, c -> getValue(r, c).gt(arg.getValue(r, c)) }))
}

internal fun GrayScaleImage.gt(arg: Double)
        = GrayScaleImageImpl(create2DDoubleArray(getHeight(), getWidth(), { r, c -> getValue(r, c).gt(arg) }))

internal fun Double.gt(arg: GrayScaleImage)
        = GrayScaleImageImpl(create2DDoubleArray(arg.getHeight(), arg.getWidth(), { r, c -> this.gt(arg.getValue(r, c)) }))

internal fun GrayScaleImage.min()
        = (0..(getHeight() - 1)).flatMap { r -> (0..(getWidth() - 1)).map { c -> getValue(r, c) } }.min()!!

internal fun GrayScaleImage.max()
        = (0..(getHeight() - 1)).flatMap { r -> (0..(getWidth() - 1)).map { c -> getValue(r, c) } }.max()!!

internal fun GrayScaleImage.sum()
        = (0..(getHeight() - 1)).flatMap { r -> (0..(getWidth() - 1)).map { c -> getValue(r, c) } }.sum()

internal fun GrayScaleImage.variance()
        = (0..(getHeight() - 1)).flatMap { r -> (0..(getWidth() - 1)).map { c -> getValue(r, c) } }.toTypedArray().variance()

internal fun GrayScaleImage.mean()
        = (0..(getHeight() - 1)).flatMap { r -> (0..(getWidth() - 1)).map { c -> getValue(r, c) } }.toTypedArray().mean()

internal fun GrayScaleImage.median()
        = (0..(getHeight() - 1)).flatMap { r -> (0..(getWidth() - 1)).map { c -> getValue(r, c) } }.toTypedArray().median()

internal fun GrayScaleImage.abs()
        = (0..(getHeight() - 1)).flatMap { r -> (0..(getWidth() - 1)).map { c -> getValue(r, c) } }.toTypedArray().abs()

/**
 * Crop an input image to a specified region of interest (ROI)
 */
internal fun GrayScaleImage.crop(roi: Roi): GrayScaleImage {
    if (roi.left < 0 || roi.top < 0 || roi.left + roi.width > getWidth() || roi.top + roi.height > getHeight()) {
        throw IllegalArgumentException("ROI doesn't fit within the image dimensions!")
    }

    val result = create2DDoubleArray(roi.height, roi.width, 0.0)
    for ((j, y) in (roi.top..(roi.top + roi.height - 1)).withIndex()) {
        for ((i, x) in (roi.left..(roi.left + roi.width - 1)).withIndex()) {
            result[j][i] = getValue(y, x)
        }
    }
    return GrayScaleImageImpl(result)
}

/**
 * Apply a binary threshold to the image.
 *
 * Instead of implicitly setting the pixels in thresholded image to 0 and 1,
 * the pixels with their values equal or greater than threshold are set
 * to highValue. The rest is res to lowValue.
 *
 * @param threshold a threshold value
 * @param lowValue value that the pixels with values lesser then threshold are set to
 * @param highValue value that the pixels with values equal or greater then threshold are set to
 */
internal fun GrayScaleImage.binaryThreshold(threshold: Double, lowValue: Double, highValue: Double): GrayScaleImage
        = GrayScaleImageImpl(
            (0..(getWidth() - 1)).map { i -> (0..(getHeight() - 1))
                .map { j -> if (getValue(i, j) >= threshold) highValue else lowValue }
                .toTypedArray() }
            .toTypedArray())

internal fun GrayScaleImage.applyMask(mask: GrayScaleImage): GrayScaleImage {
    if (getWidth() != mask.getWidth() || getHeight() != mask.getHeight()) {
        throw IllegalArgumentException("A mask must have the same dimensions as the input image!")
    }

    return GrayScaleImageImpl(
            (0..(getWidth() - 1)).map { i -> (0..(getHeight() - 1))
                .map { j -> if (mask.getValue(i, j) == 0.0) 0.0 else getValue(i, j) }
                .toTypedArray() }
            .toTypedArray())
}