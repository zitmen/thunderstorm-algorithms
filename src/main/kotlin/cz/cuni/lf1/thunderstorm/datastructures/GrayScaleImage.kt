package cz.cuni.lf1.thunderstorm.datastructures

public class GrayScaleImage(
        private val data: Array<Array<Double>>) {

    public fun getWidth()
            = data.firstOrNull()?.size ?: 0

    public fun getHeight()
            = data.size

    public fun getValue(row: Int, column: Int): Double
            = data[row][column]
}