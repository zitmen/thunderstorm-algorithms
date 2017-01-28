package cz.cuni.lf1.thunderstorm.algorithms.filters

import cz.cuni.lf1.thunderstorm.algorithms.padding.Padding
import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage
import cz.cuni.lf1.thunderstorm.datastructures.extensions.convolve2D
import cz.cuni.lf1.thunderstorm.datastructures.extensions.rotateLeft
import cz.cuni.lf1.thunderstorm.datastructures.extensions.rotateRight

internal class ConvolutionFilter private constructor(
        private val kernel: GrayScaleImage?,
        private val kernelX: GrayScaleImage?,
        private val kernelY: GrayScaleImage?,
        private val paddingTypeFactory: (padSize: Int) -> Padding) : Filter {

    public override fun filter(image: GrayScaleImage): GrayScaleImage {
        if (kernel != null) {
            // With non-separable kernels, the complexity is K*K*N,
            return image.convolve2D(kernel, paddingTypeFactory)
        } else {
            // while with separable kernels of length K, the computational complexity is 2*K*N, where N is number of pixels of the image!
            return image.convolve2D(kernelY!!, paddingTypeFactory).convolve2D(kernelX!!, paddingTypeFactory)
        }
    }

    companion object {
        public fun createFromFullKernel(kernel: GrayScaleImage, paddingTypeFactory: (padSize: Int) -> Padding): ConvolutionFilter {
            return ConvolutionFilter(kernel, null, null, paddingTypeFactory)
        }

        public fun createFromSeparableKernel(kernelX: GrayScaleImage, kernelY: GrayScaleImage, paddingTypeFactory: (padSize: Int) -> Padding): ConvolutionFilter {
            return ConvolutionFilter(null, kernelX, kernelY, paddingTypeFactory)
        }

        public fun createFromSeparableKernel(kernel: GrayScaleImage, paddingTypeFactory: (padSize: Int) -> Padding): ConvolutionFilter {
            if (kernel.getWidth() > 1 && kernel.getHeight() > 1) {
                throw IllegalArgumentException("Separable kernel must be created from column or row vector!")
            }
            if (kernel.getWidth() > 1) {
                // kernel = kernel_x -> to get kernel_y (transposition) it has to be rotated to right
                return ConvolutionFilter(null, kernel, kernel.rotateRight(), paddingTypeFactory)
            } else {
                // kernel = kernel_y -> to get kernel_x (transposition) it has to be rotated to left
                return ConvolutionFilter(null, kernel.rotateLeft(), kernel, paddingTypeFactory)
            }
        }
    }
}
