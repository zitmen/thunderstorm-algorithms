package cz.cuni.lf1.thunderstorm.parser.syntaxtree

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage

public class RetVal {

    private val value: Double?
    private val vector: Array<Double>?
    private val matrix: GrayScaleImage?

    public constructor(value: Double) {
        this.value = value
        this.vector = null
        this.matrix = null
    }

    public constructor(vector: Array<Double>) {
        this.value = null
        this.vector = vector
        this.matrix = null
    }

    public constructor(matrix: GrayScaleImage) {
        this.value = null
        this.vector = null
        this.matrix = matrix
    }

    public fun isValue() = (value != null)
    public fun isVector() = (vector != null)
    public fun isMatrix() = (matrix != null)

    public fun getValue() = value!!
    public fun getVector() = vector!!
    public fun getMatrix() = matrix!!
}
