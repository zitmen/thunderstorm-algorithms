package cz.cuni.lf1.thunderstorm.datastructures

import cz.cuni.lf1.thunderstorm.datastructures.GrayScaleImage

internal class GrayScaleImageImpl(private val data: Array<Array<Double>>) : GrayScaleImage {

    public override fun getWidth()
            = data.firstOrNull()?.size ?: 0

    public override fun getHeight()
            = data.size

    public override fun getValue(row: Int, column: Int): Double
            = data[row][column]
}